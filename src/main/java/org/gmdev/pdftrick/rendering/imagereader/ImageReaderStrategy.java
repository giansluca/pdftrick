package org.gmdev.pdftrick.rendering.imagereader;

import com.itextpdf.io.image.ImageType;
import com.itextpdf.kernel.pdf.canvas.parser.data.ImageRenderInfo;
import com.itextpdf.kernel.pdf.xobject.PdfImageXObject;

import java.io.IOException;

public class ImageReaderStrategy {

    public static ImageReader getReader(ImageRenderInfo imageRenderInfo) {
        if (imageRenderInfo.isInline()) {
            System.out.println("INLINE");
            return null;
        }

        PdfImageXObject image = imageRenderInfo.getImage();
        int imageRef = image.getPdfObject().getIndirectReference().getObjNumber();
        try {
            if (ImageType.JBIG2 == image.identifyImageType())
                return new J2BIGReader(image, imageRef);

            if (image.getBufferedImage() != null)
                return new DefaultReader(image, imageRef);

        } catch (IOException e) {
            // TODO CustomImageReader.readCMYK_JPG(imageByteArray);
            System.out.println("Unsupported Image Type");
        } catch (com.itextpdf.io.IOException e) {
            //TODO CustomImageReader.readIndexedPNG(renderInfo.getRef().getNumber(),bag.getSavedFilePath());
            System.out.println("No indexed color space supported");
        }

        return null;
    }

}
