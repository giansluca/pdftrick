package org.gmdev.pdftrick.rendering.imagereader;

import com.itextpdf.kernel.pdf.xobject.PdfImageXObject;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class InlineImageReader implements PdfImageReader {

    private final PdfImageXObject image;

    public InlineImageReader(PdfImageXObject image) {
        this.image = image;
    }

    @Override
    public BufferedImage readImage() {
        try {
            return image.getBufferedImage();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }


}
