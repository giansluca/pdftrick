package org.gmdev.pdftrick.rendering.imagereader;

import com.itextpdf.kernel.pdf.xobject.PdfImageXObject;
import com.levigo.jbig2.*;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.*;

public class J2BIGReaderPdf implements PdfImageReader {

    private final PdfImageXObject image;
    private final int ref;

    public J2BIGReaderPdf(PdfImageXObject image, int ref) {
        this.image = image;
        this.ref = ref;
    }

    @Override
    public BufferedImage readImage() {
        try {
            InputStream in = new ByteArrayInputStream(image.getImageBytes());
            ImageInputStream imageStream = ImageIO.createImageInputStream(in);

            JBIG2ImageReader imageReader = new JBIG2ImageReader(new JBIG2ImageReaderSpi());
            imageReader.setInput(imageStream);
            JBIG2ReadParam param = imageReader.getDefaultReadParam();

            return imageReader.read(0, param);
        } catch (IOException e) {
            throw new IllegalStateException("Error getting bufferedImage on a JBIG2 image");
        }
    }


}
