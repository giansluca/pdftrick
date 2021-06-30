package org.gmdev.pdftrick.rendering.imagereader;

import com.itextpdf.io.image.ImageType;
import com.itextpdf.kernel.pdf.canvas.parser.data.ImageRenderInfo;
import com.itextpdf.kernel.pdf.xobject.PdfImageXObject;

import java.io.IOException;

public class ImageReaderStrategy {

    public static PdfImageReader getReader(ImageRenderInfo imageRenderInfo) {
        if (imageRenderInfo.isInline()) {
            System.out.println("INLINE");
            return null;
        }

        PdfImageXObject image = imageRenderInfo.getImage();
        int ref = image.getPdfObject().getIndirectReference().getObjNumber();
        try {
            if (ImageType.JBIG2 == image.identifyImageType())
                return new J2BIGReaderPdf(image, ref);

            if (image.getBufferedImage() != null)
                return new DefaultReaderPdf(image, ref);

        } catch (IOException e) {
            return new JPEGColorSpaceCMYKReaderPdf(image, ref);
        } catch (com.itextpdf.io.IOException e) {
            //TODO CustomImageReader.readIndexedPNG(renderInfo.getRef().getNumber(),bag.getSavedFilePath());
            System.out.println("No indexed color space supported");
        }

        return null;
    }

}
