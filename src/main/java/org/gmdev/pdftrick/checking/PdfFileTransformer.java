package org.gmdev.pdftrick.checking;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.*;
import org.gmdev.pdftrick.manager.PdfTrickBag;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class PdfFileTransformer {

    private static final PdfTrickBag bag = PdfTrickBag.INSTANCE;

    public File saveUploadedFile(File uploadedFile) {
        String pdfPassword = bag.getPdfPassword();
        File toSaveFile = bag.getSavedFilePath().toFile();

        try {
            PdfReader reader;
            if (pdfPassword != null) {
                ReaderProperties readerProperties = new ReaderProperties()
                        .setPassword(pdfPassword.getBytes(StandardCharsets.UTF_8));

                reader = new PdfReader(uploadedFile.getPath(), readerProperties)
                        .setUnethicalReading(true);
            }
            else reader = new PdfReader(uploadedFile);

            PdfWriter writer = new PdfWriter(toSaveFile);
            PdfDocument pdfDocument = new PdfDocument(reader, writer);

            bag.setNumberOfPages(pdfDocument.getNumberOfPages());

            for (int pageNumber = 1; pageNumber <= pdfDocument.getNumberOfPages(); pageNumber++) {
                PdfPage page = pdfDocument.getPage(pageNumber);
                Rectangle pageSize = page.getPageSize();

                int rotation = page.getRotation();
                if (rotation == 270 || rotation == 90)
                    pageSize = new Rectangle(pageSize.getHeight(), pageSize.getWidth());

                page.setCropBox(pageSize);

                setPageRotation(pageNumber, rotation);
            }

            pdfDocument.close();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }

        return toSaveFile;
    }

    private void setPageRotation(int pageNumber, int rotation) {
        HashMap<Integer, String> pagesRotation = bag.getPagesRotation();

        if (rotation == 90 || rotation == 180 || rotation == 270)
            pagesRotation.put(pageNumber, String.valueOf(rotation));
    }

}
