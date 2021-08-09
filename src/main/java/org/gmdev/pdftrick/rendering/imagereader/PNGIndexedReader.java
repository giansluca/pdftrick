package org.gmdev.pdftrick.rendering.imagereader;

import com.itextpdf.io.codec.PngWriter;
import com.itextpdf.kernel.geom.Matrix;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.filters.DoNothingFilter;
import com.itextpdf.kernel.pdf.filters.FilterHandlers;
import com.itextpdf.kernel.pdf.filters.IFilterHandler;
import com.itextpdf.kernel.pdf.xobject.PdfImageXObject;

import org.apache.commons.imaging.ImageReadException;
import org.gmdev.pdftrick.manager.PdfTrickBag;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class PNGIndexedReader implements PdfImageReader {

    private final PdfImageXObject image;
    private final int reference;
    private final Matrix matrix;
    private final int pageNumber;

    private static final PdfTrickBag bag = PdfTrickBag.INSTANCE;

    public PNGIndexedReader(PdfImageXObject image, int reference, Matrix matrix, int pageNumber) {
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
        } catch (IOException | ImageReadException e) {
            return Optional.empty();
        }
    }

    /**
     * Read a png image with if all other method fails
     */
    private BufferedImage read() throws IOException, ImageReadException {
        byte[] imageBytes = image.getPdfObject().getBytes();
        float width = image.getWidth();
        float height = image.getHeight();
        int pngBitDepth = image.getPdfObject().getAsNumber(PdfName.BitsPerComponent).intValue();
        PdfArray decode = image.getPdfObject().getAsArray(PdfName.Decode);

        Map<PdfName, IFilterHandler> filters = new HashMap<>(FilterHandlers.getDefaultFilterHandlers());
        filters.put(PdfName.JBIG2Decode, new DoNothingFilter());
        byte[] decodedBytes = PdfReader.decodeBytes(imageBytes, image.getPdfObject(), filters);

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
        decodedBytes = outputStream.toByteArray();

        InputStream in = new ByteArrayInputStream(decodedBytes);
        ImageInputStream inputStream = ImageIO.createImageInputStream(in);
        return ImageIO.read(inputStream);
    }


}
