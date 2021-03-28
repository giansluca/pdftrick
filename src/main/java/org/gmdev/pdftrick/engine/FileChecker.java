package org.gmdev.pdftrick.engine;

import java.awt.Color;
import java.io.*;
import java.text.MessageFormat;
import java.util.*;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.*;

import org.gmdev.pdftrick.manager.PdfTrickBag;
import org.gmdev.pdftrick.swingmanager.SwingInvoker;
import org.gmdev.pdftrick.utils.*;

import com.itextpdf.text.exceptions.BadPasswordException;
import com.itextpdf.text.pdf.*;

public class FileChecker {

    private static final PdfTrickBag bag = PdfTrickBag.INSTANCE;
    private static final int MAX_SIZE = 256;

    private String pdfPassword;
    private boolean checkEncryption = false;
    private boolean userProtection = false;
    private boolean ownerProtection = false;

    public boolean isValid() {
        Properties messages = bag.getMessagesProps();
        File uploadedFile = bag.getUploadedFile();

        if (!checkPdfTypeFile(uploadedFile, messages)) return false;
        if (!checkFileSize(uploadedFile, messages)) return false;

        SwingInvoker.invokeAndWait(() -> checkEncryption = checkEncryption(uploadedFile, messages));
        if (!checkEncryption) return false;

        return checkNumberOfImages(uploadedFile, messages);
    }

    private boolean checkPdfTypeFile(File uploadedFile, Properties messages) {
        String fileName = uploadedFile.getName();
        String content = this.readFile(uploadedFile);
        if (!content.substring(0, 4).equalsIgnoreCase("%PDF")) {
            Messages.append("WARNING", MessageFormat.format(messages.getProperty("d_msg_04"), fileName));
            return false;
        }

        return true;
    }

    private String readFile(File uploadedFile) {
        String line;
        try {
            FileReader in = new FileReader(uploadedFile);
            BufferedReader reader = new BufferedReader(in);
            line = reader.readLine();
            in.close();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }

        return line;
    }

    private boolean checkFileSize(File uploadedFile, Properties messages) {
        long fileSize = uploadedFile.length();
        long fileSizeKB = fileSize / 1024;
        long fileSizeMB = fileSizeKB / 1024;
        if (fileSizeMB > MAX_SIZE) {
            Messages.append("WARNING", messages.getProperty("t_msg_20"));
            return false;
        }

        return true;
    }

    private boolean checkNumberOfImages(File uploadedFile, Properties messages) {
        try {
            PdfReader pdfReader = pdfPassword != null
                    ? new PdfReader(uploadedFile.getPath(), pdfPassword.getBytes())
                    : new PdfReader(uploadedFile.getPath());

            for (int i = 0; i < pdfReader.getXrefSize(); i++) {
                PdfObject pdfObject = pdfReader.getPdfObject(i);
                if (pdfObject == null || !pdfObject.isStream()) continue;

                PdfStream pdfStream = (PdfStream) pdfObject;
                PdfObject pdfSubtype = pdfStream.get(PdfName.SUBTYPE);

                if (pdfSubtype != null &&
                        pdfSubtype.toString().equals(PdfName.IMAGE.toString())) return true;
            }
            pdfReader.close();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }

        Messages.append("WARNING", messages.getProperty("t_msg_21"));
        return false;
    }

    private boolean checkEncryption(File uploadedFile, Properties messages) {
        boolean check = true;

        checkProtection(uploadedFile);
        if (userProtection || ownerProtection) {
            int attempt = 1;
            String result = askAndChekPwd(uploadedFile, messages, attempt);

            if (result.equalsIgnoreCase("no")) {
                while (result.equalsIgnoreCase("no") && attempt < 3) {
                    attempt++;
                    result = askAndChekPwd(uploadedFile, messages, attempt);
                    check = false;
                }
            }

            if (result.equalsIgnoreCase("abort")) {
                Messages.append("WARNING", messages.getProperty("t_msg_22"));
                check = false;
            }

            if (result.equalsIgnoreCase("ok")) check = true;
        }

        bag.setPdfPassword(pdfPassword);
        return check;
    }

    private void checkProtection(File file) {
        try {
            PdfReader pdfReader = new PdfReader(file.getPath());
            if (pdfReader.isEncrypted()) ownerProtection = true;
            pdfReader.close();
        } catch (BadPasswordException e) {
            userProtection = true;
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private String askAndChekPwd(File uploadedFile, Properties messages, int attempt) {
        ImageIcon imageIcon = new ImageIcon(FileLoader.loadFileAsUrl(Constants.PDFTRICK_ICO));

        JDialog userPwdDialog = new JDialog((JDialog) null, true);
        userPwdDialog.setTitle(Constants.PWD_DIALOG);
        userPwdDialog.setIconImage(imageIcon.getImage());
        userPwdDialog.setSize(620, 80);
        userPwdDialog.setResizable(false);
        userPwdDialog.setLocationRelativeTo(null);
        userPwdDialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        userPwdDialog.setLayout(null);

        JPasswordField userPwdField = new JPasswordField();
        userPwdField.setBounds(25, 25, 500, 20);
        userPwdField.setEchoChar('*');

        JLabel userPwdLabel = new JLabel();
        userPwdLabel.setBounds(30, 5, 500, 18);
        userPwdLabel.setText(MessageFormat.format(messages.getProperty("d_msg_06"), uploadedFile.getName()));

        JButton okButton = new JButton("OK");
        okButton.setBounds(530, 25, 70, 20);
        okButton.setOpaque(true);

        OkCloseAction okCloseAction = new OkCloseAction(messages, uploadedFile, attempt);
        Thread okCloseActionThread = new Thread(okCloseAction);

        okButton.addActionListener(actionEvent -> {
            userPwdDialog.setVisible(false);
            userPwdDialog.dispose();
            okCloseAction.pwd = String.valueOf(userPwdField.getPassword());
            okCloseActionThread.start();
        });

        userPwdField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void removeUpdate(DocumentEvent e) {
                setBorder();
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                setBorder();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                setBorder();
            }

            public void setBorder() {
                Border borderOrange = BorderFactory.createLineBorder(Color.ORANGE);
                if (userPwdField.getPassword().length > 0)
                    okButton.setBorder(borderOrange);
                else
                    okButton.setBorder(new JButton().getBorder());
            }
        });

        userPwdDialog.getRootPane().setDefaultButton(okButton);
        userPwdDialog.add(userPwdField);
        userPwdDialog.add(userPwdLabel);
        userPwdDialog.add(okButton);
        userPwdDialog.setVisible(true);

        if (okCloseActionThread.isAlive()) {
            try {
                okCloseActionThread.join();
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
        }

        return okCloseAction.check;
    }

    public class OkCloseAction implements Runnable {
        private volatile String check = "abort";
        private volatile String pwd;
        private final Properties messages;
        private final File uploadedFile;
        private final int attempt;

        public OkCloseAction(Properties messages, File uploadedFile, int attempt) {
            this.messages = messages;
            this.uploadedFile = uploadedFile;
            this.attempt = attempt;
        }

        @Override
        public void run() {
            PdfReader reader;
            try {
                if (userProtection || ownerProtection) {
                    reader = new PdfReader(uploadedFile.getPath(), pwd.getBytes());

                    if (reader.isEncrypted()) {
                        if (reader.isOpenedWithFullPermissions()) {
                            check = "ok";
                            pdfPassword = pwd;
                        } else {
                            check = "no";
                            Messages.appendLater("WARNING", MessageFormat.format(
                                    messages.getProperty("d_msg_08"), attempt, uploadedFile.getName()));
                        }
                    }
                    reader.close();
                }
            } catch (BadPasswordException e) {
                check = "no";
                Messages.appendLater("WARNING", MessageFormat.format(
                        messages.getProperty("d_msg_07"), attempt, uploadedFile.getName()));
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
    }


}
