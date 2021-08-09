package org.gmdev.pdftrick.rendering.imagereader;

import com.itextpdf.kernel.geom.Matrix;
import com.itextpdf.kernel.pdf.xobject.PdfImageXObject;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Optional;

public class InlineImageReader implements PdfImageReader {

    private final PdfImageXObject image;
    private final Matrix matrix;
    private final int pageNumber;

    public InlineImageReader(PdfImageXObject image, Matrix matrix, int pageNumber) {
        this.image = image;
        this.matrix = matrix;
        this.pageNumber = pageNumber;
    }

    @Override
    public PdfImageXObject getImage() {
        return image;
    }

    @Override
    public Optional<BufferedImage> readImage() {
        try {
            BufferedImage bufferedImage = image.getBufferedImage();
            bufferedImage = checkAndApplyMask(bufferedImage, image);
            bufferedImage = checkAndApplyRotations(bufferedImage, matrix, pageNumber);

            return Optional.of(bufferedImage);
        } catch (IOException e) {
            return Optional.empty();
        }
    }


}
