package org.gmdev.pdftrick.rendering.imagereader;

import com.itextpdf.io.image.ImageType;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfStream;
import com.itextpdf.kernel.pdf.xobject.PdfImageXObject;
import org.gmdev.pdftrick.manager.PdfTrickBag;
import org.gmdev.pdftrick.utils.ImageUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Optional;

public interface PdfImageReader {

    PdfTrickBag bag = PdfTrickBag.INSTANCE;

    PdfImageXObject getImage();

    Optional<BufferedImage> readImage();

    BufferedImage checkAndApplyMask(BufferedImage bufferedImage);

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

    default BufferedImage checkAndApplyRotations(BufferedImage bufferedImage) {
//        HashMap<Integer, String> pagesRotation = bag.getPagesRotation();
//
//        String flip = "";
//        String rotate = "";
//        Matrix matrix = renderInfo.getImageCTM();
//        String pageRotation = "" + pagesRotation.get(pageNumber);
//
//        // experimental
//        float i11 = matrix.get(Matrix.I11);	// if negative -> horizontal flip
//        float i12 = matrix.get(Matrix.I12);	// if negative -> 90 degree rotation
//        float i21 = matrix.get(Matrix.I21); // if negative -> 270 degree rotation
//        float i22 = matrix.get(Matrix.I22); // if negative -> vertical flip
//
//        // flip and rotation ... from matrix if i11 or i22 is negative i have to flip image
//        if (("" + i11).charAt(0) == '-' ) {
//            flip = "fh";
//        }
//        else if (("" + i22).charAt(0) == '-') {
//            flip = "fv";
//        }
//
//        if (pageRotation.equalsIgnoreCase("270") || ("" + i21).charAt(0) == '-' ) {
//            rotate = "270";
//        }
//        else if (pageRotation.equalsIgnoreCase("180")) {
//            rotate = "180";
//        }
//        else if (pageRotation.equalsIgnoreCase("90") || ("" + i12).charAt(0) == '-') {
//            rotate = "90";
//        }
//
//        bufferedImage = ImageUtils.adjustImage(bufferedImage, flip, rotate);
//
//        return bufferedImage;
        return null;
    }




}
