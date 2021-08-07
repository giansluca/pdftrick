package org.gmdev.pdftrick.utils.external;

import java.awt.Image;
import java.awt.color.*;
import java.awt.color.ICC_Profile;
import java.awt.image.*;
import java.io.*;
import java.nio.file.Path;
import java.util.*;

import javax.imageio.*;
import javax.imageio.stream.ImageInputStream;

import org.apache.commons.imaging.*;
import org.apache.commons.imaging.common.bytesource.*;
import org.apache.commons.imaging.formats.jpeg.JpegImageParser;
import org.apache.commons.imaging.formats.jpeg.segments.*;

import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.codec.PngWriter;
import com.itextpdf.text.pdf.parser.PdfImageObject;
import com.levigo.jbig2.*;
import org.gmdev.pdftrick.utils.*;

import static org.gmdev.pdftrick.utils.Constants.*;

public class CustomImageReader {

    private static final int COLOR_TYPE_RGB = 1;
    private static final int COLOR_TYPE_CMYK = 2;
    private static final int COLOR_TYPE_YCCK = 3;
    private static int colorType = COLOR_TYPE_RGB;
    private static boolean hasAdobeMarker = false;

    public static BufferedImage readJBIG2(PdfImageObject image) {
        BufferedImage buffImg = null;
        PdfDictionary dic = image.getDictionary();
        PdfDictionary decodedic = dic.getAsDict(PdfName.DECODEPARMS);
        PdfStream globalStream = decodedic.getAsStream(PdfName.JBIG2GLOBALS);

        try {
            byte[] byteArrayGlobal = PdfReader.getStreamBytes((PRStream) globalStream);

            InputStream in = new ByteArrayInputStream(image.getImageAsBytes());
            ImageInputStream stream = ImageIO.createImageInputStream(in);

            InputStream inG = new ByteArrayInputStream(byteArrayGlobal);
            ImageInputStream streamG = ImageIO.createImageInputStream(inG);

            JBIG2ImageReader reader = new JBIG2ImageReader(new JBIG2ImageReaderSpi());
            reader.setInput(stream);
            JBIG2Globals globals = reader.processGlobals(streamG);
            reader.setGlobals(globals);
            ImageReadParam param = reader.getDefaultReadParam();
            buffImg = reader.read(0, param);

            in.close();
            inG.close();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        return buffImg;
    }

    /**
     * Read a png image with if all other method fails
     */
    public static BufferedImage readIndexedPNG(int ref, Path pdfFile) throws IOException, ImageReadException {
        PdfReader reader = new PdfReader(pdfFile.toString());
        PRStream stream = (PRStream) reader.getPdfObject(ref);
        PdfDictionary dic = stream;
        byte[] content = PdfReader.getStreamBytesRaw(stream);

        int width = dic.getAsNumber(PdfName.WIDTH).intValue();
        int height = dic.getAsNumber(PdfName.HEIGHT).intValue();
        int pngBitDepth = dic.getAsNumber(PdfName.BITSPERCOMPONENT).intValue();

        PdfObject colorspace = dic.getDirectObject(PdfName.COLORSPACE);
        PdfArray decode = dic.getAsArray(PdfName.DECODE);
        PdfArray carray = (PdfArray) colorspace;
        PdfObject id2 = carray.getDirectObject(3);

        byte[] palette = null;
        if (id2 instanceof PdfString) {
            palette = id2.getBytes();
        } else if (id2 instanceof PRStream) {
            palette = PdfReader.getStreamBytes(((PRStream) id2));
        }

        Map<PdfName, FilterHandlers.FilterHandler> handlers = new HashMap<>(FilterHandlers.getDefaultFilterHandlers());
        byte[] imageBytes = PdfReader.decodeBytes(content, dic, handlers);

        int stride = (width * pngBitDepth + 7) / 8;
        ByteArrayOutputStream ms = new ByteArrayOutputStream();
        PngWriter png = new PngWriter(ms);

        if (decode != null) {
            if (pngBitDepth == 1) {
                // if the decode array is 1,0, then we need to invert the image
                if (decode.getAsNumber(0).intValue() == 1 && decode.getAsNumber(1).intValue() == 0) {
                    int len = imageBytes.length;
                    for (int t = 0; t < len; ++t) {
                        imageBytes[t] ^= 0xff;
                    }
                } else {
                    // if the decode array is 0,1, do nothing.
                    // It's possible that the array could be 0,0 or 1,1,
                    // but that would be silly, so we'll just ignore that case
                }
            } else {
                // TODO: add decode transformation for other depths
            }
        }

        int pngColorType = 0;
        png.writeHeader(width, height, pngBitDepth, pngColorType);

        if (palette != null) {
            png.writePalette(palette);
        }
        png.writeData(imageBytes, stride);
        png.writeEnd();

        imageBytes = ms.toByteArray();

        InputStream in = new ByteArrayInputStream(imageBytes);
        ImageInputStream ima_stream = ImageIO.createImageInputStream(in);

        BufferedImage buffImg = null;
        BufferedImage buffPic = ImageIO.read(ima_stream);

        // isValid if image contains a mask image ... experimental for this type of image
        BufferedImage buffMask = null;
        PRStream maskStream = (PRStream) dic.getAsStream(PdfName.SMASK);
        if (maskStream != null) {
            PdfImageObject maskImage = new PdfImageObject(maskStream);
            buffMask = maskImage.getBufferedImage();

            Image img = ImageUtils.transformGrayToTransparency(buffMask);
            buffImg = ImageUtils.applyTransparency(buffPic, img);
        } else {
            buffImg = buffPic;
        }

        reader.close();
        ms.close();
        in.close();

        return buffImg;
    }

    /**
     * Read a JPG image with CMYK ICC profile
     */
    public static BufferedImage readCMYK_JPG(byte[] imageByteArray) throws IOException, ImageReadException {
        colorType = COLOR_TYPE_RGB;
        hasAdobeMarker = false;

        InputStream in = new ByteArrayInputStream(imageByteArray);
        ImageInputStream stream = ImageIO.createImageInputStream(in);
        Iterator<ImageReader> iter = ImageIO.getImageReaders(stream);

        ImageReader reader = iter.next();
        reader.setInput(stream);

        BufferedImage image;
        ICC_Profile profile;

        colorType = COLOR_TYPE_CMYK;
        checkAdobeMarker(imageByteArray);
        profile = Imaging.getICCProfile(imageByteArray);

        WritableRaster raster = (WritableRaster) reader.readRaster(0, null);

        if (colorType == COLOR_TYPE_YCCK) {
            convertYcckToCmyk(raster);
        }

        if (hasAdobeMarker) {
            //convertInvertedColors(raster);
        }

        image = convertCmykToRgb(raster, profile);

        in.close();
        reader.dispose();

        return image;
    }

    /**
     * Check if the images has Adobe byte marker and if is a YCCK type
     */
    private static void checkAdobeMarker(byte[] imageByteArray) throws IOException, ImageReadException {
        JpegImageParser parser = new JpegImageParser();
        ByteSource byteSource = new ByteSourceArray(imageByteArray);
        List<Segment> segments = parser.readSegments(byteSource, new int[]{0xffee}, true);
        if (segments != null && segments.size() >= 1) {
            //UnknownSegment app14Segment = (UnknownSegment) segments.get(0);
            //App14Segment app14Segment = (App14Segment) segments.get(0);
            GenericSegment app14Segment = (GenericSegment) segments.get(0);
            byte[] data = app14Segment.getSegmentData();

            if (data.length >= 12 && data[0] == 'A' && data[1] == 'd' && data[2] == 'o' && data[3] == 'b' && data[4] == 'e') {
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
    private static void convertYcckToCmyk(WritableRaster raster) {
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
    private static void convertInvertedColors(WritableRaster raster) {
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
                intToBigEndian(ICC_Profile.icSigDisplayClass, profileData, ICC_Profile.icHdrDeviceClass);
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
    private static void intToBigEndian(int value, byte[] array, int index) {
        array[index] = (byte) (value >> 24);
        array[index + 1] = (byte) (value >> 16);
        array[index + 2] = (byte) (value >> 8);
        array[index + 3] = (byte) (value);
    }

}
