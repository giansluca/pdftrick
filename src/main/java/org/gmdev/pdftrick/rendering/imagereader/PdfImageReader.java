package org.gmdev.pdftrick.rendering.imagereader;

import com.itextpdf.io.image.ImageType;
import com.itextpdf.kernel.geom.Matrix;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfStream;
import com.itextpdf.kernel.pdf.xobject.PdfImageXObject;
import org.gmdev.pdftrick.manager.PdfTrickBag;
import org.gmdev.pdftrick.utils.ImageUtils;
import org.gmdev.pdftrick.utils.ImageUtils.Flip;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;

import static org.gmdev.pdftrick.utils.Constants.*;
import static org.gmdev.pdftrick.utils.ImageUtils.Flip.FLIP_HORIZONTAL;
import static org.gmdev.pdftrick.utils.ImageUtils.Flip.FLIP_VERTICAL;

public interface PdfImageReader {

    PdfTrickBag bag = PdfTrickBag.INSTANCE;

    String getKey();

    PdfImageXObject getImageObject();

    Optional<BufferedImage> readImage();

    default BufferedImage checkAndApplyMask(BufferedImage bufferedImage, PdfImageXObject image) {
        if (image.identifyImageType() == ImageType.JPEG) return bufferedImage;

        PdfStream maskStream = image.getPdfObject().getAsStream(PdfName.SMask);
        if (maskStream == null) return bufferedImage;

        try {
            PdfImageXObject maskImage = new PdfImageXObject(maskStream);
            BufferedImage bufferedImageMask = maskImage.getBufferedImage();
            Image transparency = ImageUtils.transformGrayToTransparency(bufferedImageMask);

            return ImageUtils.applyTransparency(bufferedImage, transparency);
        } catch (IOException e) {
            return bufferedImage;
        }
    }

    default BufferedImage checkAndApplyRotations(BufferedImage bufferedImage, Matrix matrix, int pageNumber) {
        HashMap<Integer, String> pagesRotation = bag.getPagesRotation();

        Flip flip = null;
        String rotate = "";
        String pageRotation = pagesRotation.get(pageNumber);

        // experimental
        float i11 = matrix.get(Matrix.I11);	// if negative -> horizontal flip
        float i12 = matrix.get(Matrix.I12);	// if negative -> 90 degree rotation
        float i21 = matrix.get(Matrix.I21); // if negative -> 270 degree rotation
        float i22 = matrix.get(Matrix.I22); // if negative -> vertical flip

        // flip, from matrix if i11 or i22 is negative i have to flip image
        if (i11 < 0)
            flip = FLIP_HORIZONTAL;
        else if (i22 < 0)
            flip = FLIP_VERTICAL;

        // rotation, from matrix if i21 or i12 is negative i have to rotate image
        if ((pageRotation != null && pageRotation.equalsIgnoreCase("270")) || i21 < 0)
            rotate = "270";
        else if (pageRotation != null && pageRotation.equalsIgnoreCase("180"))
            rotate = "180";
        else if ((pageRotation != null && pageRotation.equalsIgnoreCase("90")) || i12 < 0)
            rotate = "90";

        return ImageUtils.adjustImage(bufferedImage, flip, rotate);
    }

    default BufferedImage scaleImage(BufferedImage bufferedImage) {
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();

        if (width > THUMBNAIL_MAX_SIZE_WIDTH || height > THUMBNAIL_MAX_SIZE_HEIGHT) {
            double faktor;
            if (width > height) {
                faktor = 160 / (double) width;
                int scaledWidth = (int) Math.round(faktor * width);
                int scaledHeight = (int) Math.round(faktor * height);
                bufferedImage = ImageUtils.getScaledImageWithScalr(bufferedImage, scaledWidth, scaledHeight);
            } else {
                faktor = 160 / (double) height;
                int scaledWidth = (int) Math.round(faktor * width);
                int scaledHeight = (int) Math.round(faktor * height);
                bufferedImage = ImageUtils.getScaledImageWithScalr(bufferedImage, scaledWidth, scaledHeight);
            }
        }

        return bufferedImage;
    }




}
