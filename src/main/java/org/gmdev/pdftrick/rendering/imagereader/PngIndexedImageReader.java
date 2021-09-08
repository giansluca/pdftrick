package org.gmdev.pdftrick.rendering.imagereader;

import com.itextpdf.io.codec.PngWriter;
import com.itextpdf.kernel.geom.Matrix;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.xobject.PdfImageXObject;

import org.apache.commons.imaging.ImageReadException;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

public class PngIndexedImageReader implements PdfImageReader {

    private final PdfImageXObject imageXObject;
    private final Matrix matrix;
    private final int pageNumber;
    private final int imageNumber;

    public PngIndexedImageReader(PdfImageXObject imageXObject, Matrix matrix, int pageNumber, int imageNumber) {
        this.imageXObject = imageXObject;
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
        } catch (IOException | ImageReadException e) {
            return Optional.empty();
        }
    }

    /**
     * Read a png image with if all other method fails
     */
    private BufferedImage read() throws IOException, ImageReadException {
        byte[] imageBytes = imageXObject.getPdfObject().getBytes();
        float width = imageXObject.getWidth();
        float height = imageXObject.getHeight();
        int pngBitDepth = imageXObject.getPdfObject().getAsNumber(PdfName.BitsPerComponent).intValue();
        PdfArray decode = imageXObject.getPdfObject().getAsArray(PdfName.Decode);

        int stride = ((int) width * pngBitDepth + 7) / 8;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PngWriter png = new PngWriter(outputStream);

        if (decode != null) {
            if (pngBitDepth == 1) {
                // if the decode array is 1,0, then we need to invert the image
                if (decode.getAsNumber(0).intValue() == 1 && decode.getAsNumber(1).intValue() == 0) {
                    int len = imageBytes.length;
                    for (int t = 0; t < len; ++t) {
                        imageBytes[t] ^= 0xff;
                    }
                } else {
                    // if the decode array is 0,1, do nothing.  It's possible that the array could be 0,0 or 1,1 - but that would be silly, so we'll just ignore that case
                }
            } else {
                // todo: add decode transformation for other depths
            }
        }

        int pngColorType = 0;
        png.writeHeader((int) width, (int) height, pngBitDepth, pngColorType);
        png.writeData(imageBytes, stride);
        png.writeEnd();
        byte[] decodedBytes = outputStream.toByteArray();

        InputStream in = new ByteArrayInputStream(decodedBytes);
        ImageInputStream inputStream = ImageIO.createImageInputStream(in);
        return ImageIO.read(inputStream);
    }


}
