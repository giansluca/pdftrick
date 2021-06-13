package org.gmdev.pdftrick.rendering.model;

import org.gmdev.pdftrick.rendering.imagereader.ImageReader;

public class PdfTrickImage {

    private final ImageReader imageReader;

    public PdfTrickImage(ImageReader imageReader) {
        this.imageReader = imageReader;
    }

    public ImageReader getImageReader() {
        return imageReader;
    }
}
