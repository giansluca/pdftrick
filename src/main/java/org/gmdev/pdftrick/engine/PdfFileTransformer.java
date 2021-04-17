package org.gmdev.pdftrick.engine;

import java.io.*;
import java.util.*;

import org.gmdev.pdftrick.manager.PdfTrickBag;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

public class PdfFileTransformer {

    private static final PdfTrickBag bag = PdfTrickBag.INSTANCE;

    public File saveUploadedFile(File uploadedFile) {
        String pdfPassword = bag.getPdfPassword();
        File savedFile = bag.getSavedFilePath().toFile();
        HashMap<Integer, String> rotationFromPages = bag.getPagesRotationPages();

        try {
            InputStream in = new FileInputStream(uploadedFile);
            OutputStream out = new FileOutputStream(savedFile);

            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, out);
            document.open();
            PdfContentByte pdfContent = writer.getDirectContent();

            PdfReader reader;
            if (pdfPassword != null)
                reader = new PdfReader(in, pdfPassword.getBytes());
            else
                reader = new PdfReader(in);

            for (int i = 1; i <= reader.getNumberOfPages(); i++) {
                int rotation = reader.getPageRotation(i);

                Rectangle pageSize = reader.getPageSize(i);
                if (rotation == 270 || rotation == 90)
                    pageSize = new Rectangle(pageSize.getHeight(), pageSize.getWidth());

                document.setPageSize(pageSize);
                writer.setCropBoxSize(pageSize);

                document.newPage();
                PdfImportedPage page = writer.getImportedPage(reader, i);

                if (rotation == 270) {
                    pdfContent.addTemplate(page, 0f, 1.0f, -1.0f, 0f, reader.getPageSizeWithRotation(i).getWidth(), 0);
                    rotationFromPages.put(i, "" + rotation);
                } else if (rotation == 180) {
                    pdfContent.addTemplate(page, -1f, 0f, 0f, -1f, 0f, 0f);
                    rotationFromPages.put(i, "" + rotation);
                } else if (rotation == 90) {
                    pdfContent.addTemplate(page, 0f, -1f, 1f, 0f, 0f, reader.getPageSizeWithRotation(i).getHeight());
                    rotationFromPages.put(i, "" + rotation);
                } else {
                    pdfContent.addTemplate(page, 1f, 0f, 0f, 1f, 0f, 0f);
                }
            }
            document.close();
            out.close();
            in.close();
            reader.close();
        } catch (DocumentException | IOException e) {
            throw new IllegalStateException(e);
        }

        return savedFile;
    }

}
