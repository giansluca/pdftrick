package org.gmdev.pdftrick.rendering.model;

import org.gmdev.pdftrick.rendering.imagereader.PdfImageReader;

public class PdfTrickImage {

    private final PdfImageReader pdfImageReader;

    public PdfTrickImage(PdfImageReader pdfImageReader) {
        this.pdfImageReader = pdfImageReader;
    }

    public PdfImageReader getImageReader() {
        return pdfImageReader;
    }
}
