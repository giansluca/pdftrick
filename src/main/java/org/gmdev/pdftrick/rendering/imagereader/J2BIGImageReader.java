package org.gmdev.pdftrick.rendering.imagereader;

import com.itextpdf.kernel.geom.Matrix;
import com.itextpdf.kernel.pdf.xobject.PdfImageXObject;
import com.levigo.jbig2.*;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.*;
import java.util.Optional;

public class J2BIGImageReader implements PdfImageReader {

    private final PdfImageXObject imageXObject;
    private final int reference;
    private final Matrix matrix;
    private final int pageNumber;
    private final int imageNumber;

    public J2BIGImageReader(PdfImageXObject imageXObject,
                            int reference,
                            Matrix matrix,
                            int pageNumber,
                            int imageNumber) {

        this.imageXObject = imageXObject;
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
    public PdfImageXObject getImageXObject() {
        return imageXObject;
    }

    @Override
    public String getExtension() {
        return imageXObject.identifyImageFileExtension();
    }

    @Override
    public Optional<BufferedImage> readImage() {
        try {
            BufferedImage bufferedImage = read();
            bufferedImage = checkAndApplyMask(bufferedImage, imageXObject);
            bufferedImage = checkAndApplyRotations(bufferedImage, matrix, pageNumber);

            return Optional.of(bufferedImage);
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    private BufferedImage read() throws IOException {
        InputStream in = new ByteArrayInputStream(imageXObject.getImageBytes());
        ImageInputStream imageStream = ImageIO.createImageInputStream(in);

        JBIG2ImageReader imageReader = new JBIG2ImageReader(new JBIG2ImageReaderSpi());
        imageReader.setInput(imageStream);
        JBIG2ReadParam param = imageReader.getDefaultReadParam();

        return imageReader.read(0, param);
    }


}
