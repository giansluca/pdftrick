package org.gmdev.pdftrick.rendering.imagereader;

import com.itextpdf.kernel.geom.Matrix;
import com.itextpdf.kernel.pdf.xobject.PdfImageXObject;
import org.apache.commons.imaging.*;
import org.apache.commons.imaging.common.bytesource.*;
import org.apache.commons.imaging.formats.jpeg.JpegImageParser;
import org.apache.commons.imaging.formats.jpeg.segments.*;
import org.gmdev.pdftrick.utils.FileLoader;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.color.ColorSpace;
import java.awt.color.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;

import static org.gmdev.pdftrick.utils.Constants.GENERIC_ICC_FILE;

public class JpegCMYKImageReader implements PdfImageReader {

    private static final int COLOR_TYPE_RGB = 1;
    private static final int COLOR_TYPE_CMYK = 2;
    private static final int COLOR_TYPE_YCCK = 3;
    
    private final PdfImageXObject image;
    private final int reference;
    private final Matrix matrix;
    private final int pageNumber;
    private final int imageNumber;

    private int colorType = COLOR_TYPE_RGB;
    private boolean hasAdobeMarker = false;

    public JpegCMYKImageReader(PdfImageXObject image,
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
            BufferedImage bufferedImage = read();
            bufferedImage = checkAndApplyMask(bufferedImage, image);
            bufferedImage = checkAndApplyRotations(bufferedImage, matrix, pageNumber);

            return Optional.of(bufferedImage);
        } catch (IOException | ImageReadException e) {
            return Optional.empty();
        }
    }

    /**
     * Read a JPG image with CMYK ICC profile
     */
    private BufferedImage read() throws IOException, ImageReadException {
        byte[] imageByteArray = image.getImageBytes();
        InputStream in = new ByteArrayInputStream(imageByteArray);
        ImageInputStream imageStream = ImageIO.createImageInputStream(in);
        Iterator<javax.imageio.ImageReader> ite = ImageIO.getImageReaders(imageStream);

        ImageReader reader = ite.next();
        reader.setInput(imageStream);

        colorType = COLOR_TYPE_CMYK;
        checkAdobeMarker(imageByteArray);
        ICC_Profile profile = Imaging.getICCProfile(imageByteArray);

        WritableRaster raster = (WritableRaster) reader.readRaster(0, null);
        if (colorType == COLOR_TYPE_YCCK)
            convertYcckToCmyk(raster);

        if (hasAdobeMarker) {
            System.out.println("Adobe marker");
            // convertInvertedColors(raster);
        }

        BufferedImage image = convertCmykToRgb(raster, profile);

        in.close();
        reader.dispose();

        return image;
    }


    /**
     * Check if the images has Adobe byte marker and if is a YCCK type
     */
    private void checkAdobeMarker(byte[] imageByteArray) throws IOException, ImageReadException {
        JpegImageParser parser = new JpegImageParser();
        ByteSource byteSource = new ByteSourceArray(imageByteArray);
        List<Segment> segments = parser.readSegments(byteSource, new int[]{0xffee}, true);

        if (segments != null && segments.size() >= 1) {
            //UnknownSegment app14Segment = (UnknownSegment) segments.get(0);
            //App14Segment app14Segment = (App14Segment) segments.get(0);
            GenericSegment app14Segment = (GenericSegment) segments.get(0);
            byte[] data = app14Segment.getSegmentData();

            if (data.length >= 12
                    && data[0] == 'A'
                    && data[1] == 'd'
                    && data[2] == 'o'
                    && data[3] == 'b'
                    && data[4] == 'e') {

                hasAdobeMarker = true;
                byte[] data_2 = app14Segment.getSegmentData();
                int transform = data_2[11] & 0xff;

                if (transform == 2)
                    colorType = COLOR_TYPE_YCCK;
            }
        }
    }

    /**
     * Convert image profile from Ycck to Cmyk
     */
    private void convertYcckToCmyk(WritableRaster raster) {
        int height = raster.getHeight();
        int width = raster.getWidth();
        int stride = width * 4;
        int[] pixelRow = new int[stride];

        for (int h = 0; h < height; h++) {
            raster.getPixels(0, h, width, 1, pixelRow);
            for (int x = 0; x < stride; x += 4) {
                int y = pixelRow[x];
                int cb = pixelRow[x + 1];
                int cr = pixelRow[x + 2];

                int c = (int) (y + 1.402 * cr - 178.956);
                int m = (int) (y - 0.34414 * cb - 0.71414 * cr + 135.95984);
                y = (int) (y + 1.772 * cb - 226.316);

                if (c < 0) c = 0;
                else if (c > 255) c = 255;
                if (m < 0) m = 0;
                else if (m > 255) m = 255;
                if (y < 0) y = 0;
                else if (y > 255) y = 255;

                pixelRow[x] = 255 - c;
                pixelRow[x + 1] = 255 - m;
                pixelRow[x + 2] = 255 - y;
            }
            raster.setPixels(0, h, width, 1, pixelRow);
        }
    }

    /**
     * Invert pixel color if the image has a adobe marker ... not used now
     */
    @SuppressWarnings("unused")
    private  void convertInvertedColors(WritableRaster raster) {
        int height = raster.getHeight();
        int width = raster.getWidth();
        int stride = width * 4;
        int[] pixelRow = new int[stride];

        for (int h = 0; h < height; h++) {
            raster.getPixels(0, h, width, 1, pixelRow);

            for (int x = 0; x < stride; x++)
                pixelRow[x] = 255 - pixelRow[x];

            raster.setPixels(0, h, width, 1, pixelRow);
        }
    }

    /**
     * Convert image from Cmyk to Rgb profile
     */
    private static BufferedImage convertCmykToRgb(Raster cmykRaster, ICC_Profile cmykProfile) {
        if (cmykProfile == null) {
            try (InputStream in = FileLoader.loadFileAsStream(GENERIC_ICC_FILE)) {
                cmykProfile = ICC_Profile.getInstance(in);
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }

        if (cmykProfile.getProfileClass() != ICC_Profile.CLASS_DISPLAY) {
            byte[] profileData = cmykProfile.getData();

            if (profileData[ICC_Profile.icHdrRenderingIntent] == ICC_Profile.icPerceptual) {
                intToBigEndian(profileData);
                cmykProfile = ICC_Profile.getInstance(profileData);
            }
        }

        ICC_ColorSpace cmykCS = new ICC_ColorSpace(cmykProfile);
        BufferedImage rgbImage = new BufferedImage(cmykRaster.getWidth(), cmykRaster.getHeight(), BufferedImage.TYPE_INT_RGB);
        WritableRaster rgbRaster = rgbImage.getRaster();

        ColorSpace rgbCS = rgbImage.getColorModel().getColorSpace();

        ColorConvertOp cmykToRgb = new ColorConvertOp(cmykCS, rgbCS, null);
        cmykToRgb.filter(cmykRaster, rgbRaster);

        return rgbImage;
    }

    /**
     * Correct too bright problem in rgb conversion
     */
    private static void intToBigEndian(byte[] array) {
        array[ICC_Profile.icHdrDeviceClass] = (byte) (ICC_Profile.icSigDisplayClass >> 24);
        array[ICC_Profile.icHdrDeviceClass + 1] = (byte) (ICC_Profile.icSigDisplayClass >> 16);
        array[ICC_Profile.icHdrDeviceClass + 2] = (byte) (ICC_Profile.icSigDisplayClass >> 8);
        array[ICC_Profile.icHdrDeviceClass + 3] = (byte) (ICC_Profile.icSigDisplayClass);
    }


}
