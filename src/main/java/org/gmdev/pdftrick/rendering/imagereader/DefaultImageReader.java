package org.gmdev.pdftrick.rendering.imagereader;

import com.itextpdf.kernel.geom.Matrix;
import com.itextpdf.kernel.pdf.xobject.PdfImageXObject;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Optional;

public class DefaultImageReader implements PdfImageReader {

    private final PdfImageXObject image;
    private final int reference;
    private final Matrix matrix;
    private final int pageNumber;
    private final int imageNumber;

    public DefaultImageReader(PdfImageXObject image,
                              int reference,
                              Matrix matrix,
                              int pageNumber,
                              int imageNumber) {

        this.image = image;
        this.reference = reference;
        this.matrix = matrix;
        this.pageNumber = pageNumber;
        this.imageNumber = imageNumber;
    }

    @Override
    public String getKey() {
        return String.format("%s-%s", imageNumber, pageNumber);
    }

    @Override
    public PdfImageXObject getImageObject() {
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
