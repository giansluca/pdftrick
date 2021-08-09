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

public class J2BIGReaderPdf implements PdfImageReader {

    private final PdfImageXObject image;
    private final int reference;
    private final Matrix matrix;
    private final int pageNumber;

    public J2BIGReaderPdf(PdfImageXObject image, int reference, Matrix matrix, int pageNumber) {
        this.image = image;
        this.reference = reference;
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
            BufferedImage bufferedImage = read();
            bufferedImage = checkAndApplyMask(bufferedImage, image);
            bufferedImage = checkAndApplyRotations(bufferedImage, matrix, pageNumber);

            return Optional.of(bufferedImage);
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    private BufferedImage read() throws IOException {
        InputStream in = new ByteArrayInputStream(image.getImageBytes());
        ImageInputStream imageStream = ImageIO.createImageInputStream(in);

        JBIG2ImageReader imageReader = new JBIG2ImageReader(new JBIG2ImageReaderSpi());
        imageReader.setInput(imageStream);
        JBIG2ReadParam param = imageReader.getDefaultReadParam();

        return imageReader.read(0, param);
    }


}
