package org.gmdev.pdftrick.utils;

import org.imgscalr.Scalr;

import java.awt.*;
import java.awt.image.*;

import static org.gmdev.pdftrick.utils.ImageUtils.Flip.FLIP_HORIZONTAL;
import static org.gmdev.pdftrick.utils.ImageUtils.Flip.FLIP_VERTICAL;

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

    public static BufferedImage adjustImage(BufferedImage bufferedImage, Flip flip, String angle) {
        if (flip == FLIP_HORIZONTAL)
            bufferedImage = Scalr.rotate(bufferedImage, Scalr.Rotation.FLIP_HORZ);
        else if (flip == FLIP_VERTICAL)
            bufferedImage = Scalr.rotate(bufferedImage, Scalr.Rotation.FLIP_VERT);

        if (angle.equals("270"))
            bufferedImage = Scalr.rotate(bufferedImage, Scalr.Rotation.CW_270);
        else if (angle.equals("180"))
            bufferedImage = Scalr.rotate(bufferedImage, Scalr.Rotation.CW_180);
        else if (angle.equals("90"))
            bufferedImage = Scalr.rotate(bufferedImage, Scalr.Rotation.CW_90);

        return bufferedImage;
    }

    public static Image transformGrayToTransparency(BufferedImage image) {
        ImageFilter filter = new RGBImageFilter() {
            public int filterRGB(int x, int y, int rgb) {
                return (rgb << 8) & 0xFF000000;
            }
        };

        ImageProducer imageProducer = new FilteredImageSource(image.getSource(), filter);
        return Toolkit.getDefaultToolkit().createImage(imageProducer);
    }

    /**
     * Apply mask (alpha channel)
     */
    public static BufferedImage applyTransparency(BufferedImage sourceImage, Image mask) {
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

    public enum Flip {
        FLIP_VERTICAL,
        FLIP_HORIZONTAL
    }

}
