package org.gmdev.pdftrick.engine;

import java.io.*;
import java.nio.file.Path;
import java.util.*;
import java.util.List;

import org.gmdev.pdftrick.manager.PdfTrickBag;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

public class PdfFileTransformer {

    private static final PdfTrickBag bag = PdfTrickBag.INSTANCE;

    public File mergePdf(File uploadedFile, Path pdfFilePath) {
        String pfdPassword = bag.getPdfPassword();
        File outFile = pdfFilePath.toFile();

        List<StreamPwdContainer> list = new ArrayList<>();
        try {
            StreamPwdContainer container = new StreamPwdContainer();
            container.setIn(new FileInputStream(uploadedFile));

            container.setPwd(Objects.requireNonNullElse(pfdPassword, ""));

            list.add(container);
            OutputStream out = new FileOutputStream(outFile);
            doMerge(list, out);
        } catch (IOException | DocumentException e) {
            throw new IllegalStateException(e);
        }

        return outFile;
    }

    private void doMerge(List<StreamPwdContainer> list, OutputStream outputStream) throws DocumentException, IOException {
        HashMap<Integer, String> rotationFromPages = bag.getPagesRotationPages();
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, outputStream);
        document.open();
        PdfContentByte cb = writer.getDirectContent();

        int z = 0;
        for (StreamPwdContainer boom : list) {

            InputStream in = boom.getIn();
            PdfReader reader;

            if (!boom.getPwd().equalsIgnoreCase("")) {
                reader = new PdfReader(in, boom.getPwd().getBytes());
            } else {
                reader = new PdfReader(in);
            }

            for (int i = 1; i <= reader.getNumberOfPages(); i++) {
                z++;
                int rotation = reader.getPageRotation(i);

                //set size
                Rectangle pageSize_ = reader.getPageSize(i);
                Rectangle pageSize;

                if (rotation == 270 || rotation == 90) {
                    pageSize = new Rectangle(pageSize_.getHeight(), pageSize_.getWidth());
                } else {
                    pageSize = pageSize_;
                }

                document.setPageSize(pageSize);
                writer.setCropBoxSize(pageSize);

                document.newPage();

                // import the page from source pdf
                PdfImportedPage page = writer.getImportedPage(reader, i);

                // add the page to the destination pdf
                if (rotation == 270) {
                    cb.addTemplate(page, 0, 1.0f, -1.0f, 0, reader.getPageSizeWithRotation(i).getWidth(), 0);
                    rotationFromPages.put(z, "" + rotation);
                } else if (rotation == 180) {
                    cb.addTemplate(page, -1f, 0, 0, -1f, 0, 0);
                    rotationFromPages.put(z, "" + rotation);
                } else if (rotation == 90) {
                    cb.addTemplate(page, 0, -1f, 1f, 0, 0, reader.getPageSizeWithRotation(i).getHeight());
                    rotationFromPages.put(z, "" + rotation);
                } else {
                    cb.addTemplate(page, 1f, 0, 0, 1f, 0, 0);
                }
            }
            in.close();
        }
        outputStream.flush();
        document.close();
        outputStream.close();
    }

    public static class StreamPwdContainer {
        public InputStream in;
        public String pwd;

        public InputStream getIn() {
            return in;
        }

        public void setIn(InputStream in) {
            this.in = in;
        }

        public String getPwd() {
            return pwd;
        }

        public void setPwd(String pwd) {
            this.pwd = pwd;
        }
    }

}
