package org.gmdev.pdftrick.engine;

import com.itextpdf.text.exceptions.BadPasswordException;
import com.itextpdf.text.pdf.PdfReader;
import org.gmdev.pdftrick.manager.PdfTrickBag;
import org.gmdev.pdftrick.swingmanager.SwingInvoker;
import org.gmdev.pdftrick.utils.*;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.text.MessageFormat;
import java.util.Properties;
import java.util.function.Consumer;

import static org.gmdev.pdftrick.engine.PasswordChecker.Result.*;

public class PasswordChecker {

    private static final PdfTrickBag bag = PdfTrickBag.INSTANCE;
    private static final int MAX_ATTEMPTS = 3;

    private PasswordChekResult passwordCheckResult = new PasswordChekResult(null, DEFAULT);
    private int attempt = 1;
    private final File uploadedFile;
    private final Properties messages;

    public PasswordChecker(File uploadedFile) {
        this.uploadedFile = uploadedFile;
        this.messages = bag.getMessagesProps();
    }

    public Consumer<PasswordChekResult> passwordCheckCallback = result -> {
        attempt++;
        passwordCheckResult = result;

        if (passwordCheckResult.result == OK)
            bag.setPdfPassword(passwordCheckResult.pdfPassword);
    };

    public Result check() {
        askPassword();
        while (passwordCheckResult.result == KO && attempt <= MAX_ATTEMPTS)
            askPassword();

        return passwordCheckResult.result;
    }

    private void askPassword() {
        SwingInvoker.invokeAndWait(() -> loadPasswordForm(passwordCheckCallback));
    }

    private void loadPasswordForm(Consumer<PasswordChekResult> passwordCheckCallback ) {
        ImageIcon imageIcon = new ImageIcon(FileLoader.loadFileAsUrl(Constants.PDFTRICK_ICO));

        JDialog passwordDialog = new JDialog((JDialog) null, true);
        passwordDialog.setTitle(Constants.PWD_DIALOG);
        passwordDialog.setIconImage(imageIcon.getImage());
        passwordDialog.setSize(620, 80);
        passwordDialog.setResizable(false);
        passwordDialog.setLocationRelativeTo(null);
        passwordDialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        passwordDialog.setLayout(null);

        JPasswordField userPwdField = new JPasswordField();
        userPwdField.setBounds(25, 25, 500, 20);
        userPwdField.setEchoChar('*');

        JLabel userPwdLabel = new JLabel();
        userPwdLabel.setBounds(30, 5, 500, 18);
        userPwdLabel.setText(MessageFormat.format(messages.getProperty("d_msg_06"), uploadedFile.getName()));

        JButton okButton = new JButton("OK");
        okButton.setBounds(530, 25, 70, 20);
        okButton.setOpaque(true);

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

        okButton.addActionListener(event -> {
            passwordDialog.setVisible(false);
            passwordDialog.dispose();
            String typedPassword = String.valueOf(userPwdField.getPassword());
            boolean gotAccess = canAccessWithPassword(typedPassword);

            if(gotAccess)
                passwordCheckCallback.accept(new PasswordChekResult(typedPassword, OK));
            else
                passwordCheckCallback.accept(new PasswordChekResult(null, KO));
        });

        passwordDialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                passwordCheckCallback.accept(new PasswordChekResult(null, ABORT));
                Messages.append("INFO", "Abort");
            }
        });

        passwordDialog.getRootPane().setDefaultButton(okButton);
        passwordDialog.add(userPwdField);
        passwordDialog.add(userPwdLabel);
        passwordDialog.add(okButton);
        passwordDialog.setVisible(true);
    }

    private boolean canAccessWithPassword(String typedPassword) {
        PdfReader reader = null;
        try {
            reader = new PdfReader(uploadedFile.getPath(), typedPassword.getBytes());
            boolean openedWithFullPermissions = reader.isOpenedWithFullPermissions();

            if (!openedWithFullPermissions)
                Messages.append("WARNING", MessageFormat.format(
                        messages.getProperty("d_msg_08"), attempt, uploadedFile.getName()));

            return openedWithFullPermissions;
        } catch (BadPasswordException e) {
            Messages.append("WARNING", MessageFormat.format(
                    messages.getProperty("d_msg_07"), attempt, uploadedFile.getName()));
            return false;
        } catch (IOException e) {
            throw new IllegalStateException(e);
        } finally {
            if (reader != null) reader.close();
        }
    }

    private static class PasswordChekResult {
        public PasswordChekResult(String pdfPassword, Result result) {
            this.pdfPassword = pdfPassword;
            this.result = result;
        }

        private final String pdfPassword;
        private final Result result;
    }

    public enum Result {
        DEFAULT, OK, KO, ABORT
    }




}
