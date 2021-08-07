package org.gmdev.pdftrick.rendering.imagereader;

import com.itextpdf.kernel.pdf.xobject.PdfImageXObject;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Optional;

public class InlineImageReader implements PdfImageReader {

    private final PdfImageXObject image;

    public InlineImageReader(PdfImageXObject image) {
        this.image = image;
    }

    @Override
    public PdfImageXObject getImage() {
        return image;
    }

    @Override
    public Optional<BufferedImage> readImage() {
        try {
            BufferedImage bufferedImage = image.getBufferedImage();

            return Optional.of(bufferedImage);
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    @Override
    public BufferedImage checkAndApplyMask(BufferedImage bufferedImage) {
        return checkAndApplyMask(bufferedImage, image);
    }


}
