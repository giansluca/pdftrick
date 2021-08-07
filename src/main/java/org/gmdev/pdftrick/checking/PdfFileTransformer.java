package org.gmdev.pdftrick.checking;

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

            for (int pageNumber = 1; pageNumber <= reader.getNumberOfPages(); pageNumber++) {
                int rotation = reader.getPageRotation(pageNumber);

                Rectangle pageSize = reader.getPageSize(pageNumber);
                if (rotation == 270 || rotation == 90)
                    pageSize = new Rectangle(pageSize.getHeight(), pageSize.getWidth());

                document.setPageSize(pageSize);
                writer.setCropBoxSize(pageSize);
                document.newPage();

                addPageToPdf(pdfContent, rotation, pageNumber, writer, reader);
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

    private void addPageToPdf(PdfContentByte pdfContent,
                              int rotation,
                              int pageNumber,
                              PdfWriter writer,
                              PdfReader reader) {

        HashMap<Integer, String> pagesRotation = bag.getPagesRotation();
        PdfImportedPage page = writer.getImportedPage(reader, pageNumber);

        switch(rotation) {
            case 90:
                pdfContent.addTemplate(page, 0, -1, 1, 0, 0, reader.getPageSizeWithRotation(pageNumber).getHeight());
                pagesRotation.put(pageNumber, String.valueOf(rotation));
                break;
            case 180:
                pdfContent.addTemplate(page, -1, 0, 0, -1, 0, 0);
                pagesRotation.put(pageNumber, String.valueOf(rotation));
                break;
            case 270:
                pdfContent.addTemplate(page, 0, 1, -1, 0, reader.getPageSizeWithRotation(pageNumber).getWidth(), 0);
                pagesRotation.put(pageNumber, String.valueOf(rotation));
                break;
            default:
                pdfContent.addTemplate(page, 1, 0, 0, 1, 0, 0);
        }
    }


}
