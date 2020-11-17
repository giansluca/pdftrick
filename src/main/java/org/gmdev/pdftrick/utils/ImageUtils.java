package org.gmdev.pdftrick.utils;

import org.imgscalr.Scalr;

import java.awt.*;
import java.awt.image.*;

public class ImageUtils {

    private ImageUtils() {
        throw new AssertionError("ImageUtils class should never be instantiated");
    }

    public static BufferedImage getScaledImage(BufferedImage sourceImage, int w, int h) {
        BufferedImage resizedImg =
                new BufferedImage(w, h,
                        sourceImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : sourceImage.getType()
                );

        Graphics2D graphics = resizedImg.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.drawImage(sourceImage, 0, 0, w, h, null);
        graphics.dispose();

        return resizedImg;
    }

    public static BufferedImage getScaledImageWithScalr(BufferedImage sourceImage, int w, int h) {
        return Scalr.resize(
                sourceImage,
                Scalr.Method.QUALITY,
                Scalr.Mode.FIT_EXACT,
                w,
                h,
                Scalr.OP_ANTIALIAS);
    }

    public static BufferedImage adjustImage(BufferedImage sourceImage, String flip, String angle) {
        BufferedImage buffImg = sourceImage;
        if (flip.equalsIgnoreCase("fh"))
            buffImg = Scalr.rotate(buffImg, Scalr.Rotation.FLIP_HORZ);
        else if (flip.equalsIgnoreCase("fv"))
            buffImg = Scalr.rotate(buffImg, Scalr.Rotation.FLIP_VERT);
        if (angle.equalsIgnoreCase("270"))
            buffImg = Scalr.rotate(buffImg, Scalr.Rotation.CW_270);
        else if (angle.equalsIgnoreCase("180"))
            buffImg = Scalr.rotate(buffImg, Scalr.Rotation.CW_180);
        else if (angle.equalsIgnoreCase("90"))
            buffImg = Scalr.rotate(buffImg, Scalr.Rotation.CW_90);

        sourceImage.flush();
        return buffImg;
    }

    public static Image TransformGrayToTransparency(BufferedImage image) {
        ImageFilter filter = new RGBImageFilter() {
            public final int filterRGB(int x, int y, int rgb) {
                return (rgb << 8) & 0xFF000000;
            }
        };

        ImageProducer imageProducer = new FilteredImageSource(image.getSource(), filter);
        return Toolkit.getDefaultToolkit().createImage(imageProducer);
    }

    /**
     * Apply mask (alpha channel to an image)
     */
    public static BufferedImage ApplyTransparency(BufferedImage sourceImage, Image mask) {
        BufferedImage destImage = new BufferedImage(
                sourceImage.getWidth(), sourceImage.getHeight(),BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = destImage.createGraphics();

        graphics.drawImage(sourceImage, 0, 0, null);
        AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.DST_IN, 1.0F);
        graphics.setComposite(ac);
        graphics.drawImage(mask, 0, 0, null);
        graphics.dispose();

        return destImage;
    }

}
