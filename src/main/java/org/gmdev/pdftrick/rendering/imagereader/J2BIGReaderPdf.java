package org.gmdev.pdftrick.rendering.imagereader;

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
    private final int ref;

    public J2BIGReaderPdf(PdfImageXObject image, int ref) {
        this.image = image;
        this.ref = ref;
    }

    @Override
    public PdfImageXObject getImage() {
        return image;
    }

    @Override
    public Optional<BufferedImage> readImage() {
        try {
            InputStream in = new ByteArrayInputStream(image.getImageBytes());
            ImageInputStream imageStream = ImageIO.createImageInputStream(in);

            JBIG2ImageReader imageReader = new JBIG2ImageReader(new JBIG2ImageReaderSpi());
            imageReader.setInput(imageStream);
            JBIG2ReadParam param = imageReader.getDefaultReadParam();

            BufferedImage bufferedImage = imageReader.read(0, param);

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
