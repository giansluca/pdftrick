package org.gmdev.pdftrick.engine;

import java.io.*;
import java.text.MessageFormat;
import java.util.*;

import org.gmdev.pdftrick.manager.PdfTrickBag;
import org.gmdev.pdftrick.utils.*;

import com.itextpdf.text.exceptions.BadPasswordException;
import com.itextpdf.text.pdf.*;

import static org.gmdev.pdftrick.engine.PasswordChecker.Result.OK;

public class FileChecker {

    private static final PdfTrickBag bag = PdfTrickBag.INSTANCE;
    private static final int MAX_SIZE = 256;

    private boolean userProtection = false;
    private boolean ownerProtection = false;

    public boolean isValid() {
        Properties messages = bag.getMessagesProps();
        File uploadedFile = bag.getUploadedFile();

        if (!checkPdfTypeFile(uploadedFile, messages)) return false;
        if (!checkFileSize(uploadedFile, messages)) return false;

        boolean canAccess = canAccess(uploadedFile);
        if (!canAccess) return false;

        return checkNumberOfImages(uploadedFile, messages);
    }

    private boolean checkPdfTypeFile(File uploadedFile, Properties messages) {
        String fileName = uploadedFile.getName();
        String content = this.readFile(uploadedFile);
        if (content.isEmpty() || !content.substring(0, 4).equalsIgnoreCase("%PDF")) {
            Messages.append("WARNING", MessageFormat.format(messages.getProperty("d_msg_04"), fileName));
            return false;
        }

        return true;
    }

    private String readFile(File uploadedFile) {
        FileReader in = null;
        String line;
        try {
            in = new FileReader(uploadedFile);
            BufferedReader reader = new BufferedReader(in);
            line = reader.readLine();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ignored) {}
            }
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
        PdfReader pdfReader = null;
        try {
            pdfReader = bag.getPdfPassword() != null
                    ? new PdfReader(uploadedFile.getPath(), bag.getPdfPassword().getBytes())
                    : new PdfReader(uploadedFile.getPath());

            for (int i = 0; i < pdfReader.getXrefSize(); i++) {
                PdfObject pdfObject = pdfReader.getPdfObject(i);
                if (pdfObject == null || !pdfObject.isStream()) continue;

                PdfStream pdfStream = (PdfStream) pdfObject;
                PdfObject pdfSubtype = pdfStream.get(PdfName.SUBTYPE);

                if (pdfSubtype != null &&
                        pdfSubtype.toString().equals(PdfName.IMAGE.toString())) return true;
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        } finally {
            if (pdfReader != null) pdfReader.close();
        }

        Messages.append("WARNING", messages.getProperty("t_msg_21"));
        return false;
    }

    private boolean canAccess(File uploadedFile) {
        verifyProtection(uploadedFile);
        if (!userProtection && !ownerProtection) return true;

        PasswordChecker passwordChecker = new PasswordChecker();
        return passwordChecker.check() == OK;
    }

    private void verifyProtection(File file) {
        PdfReader pdfReader = null;
        try {
            pdfReader = new PdfReader(file.getPath());
            if (pdfReader.isEncrypted()) ownerProtection = true;
        } catch (BadPasswordException e) {
            userProtection = true;
        } catch (IOException e) {
            throw new IllegalStateException(e);
        } finally {
            if (pdfReader != null) pdfReader.close();
        }
    }


}
