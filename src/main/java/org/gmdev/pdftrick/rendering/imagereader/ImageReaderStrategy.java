package org.gmdev.pdftrick.rendering.imagereader;

import com.itextpdf.io.image.ImageType;
import com.itextpdf.kernel.geom.Matrix;
import com.itextpdf.kernel.pdf.canvas.parser.data.ImageRenderInfo;
import com.itextpdf.kernel.pdf.xobject.PdfImageXObject;

import java.io.IOException;

public class ImageReaderStrategy {

    public static PdfImageReader getReader(ImageRenderInfo imageRenderInfo, int pageNumber, int imageNumber) {
        PdfImageXObject imageXObject = imageRenderInfo.getImage();
        Matrix matrix = imageRenderInfo.getImageCtm();

        if (imageRenderInfo.isInline())
            return new InlineImageReader(imageXObject, matrix, pageNumber, imageNumber);

        int reference = imageXObject.getPdfObject().getIndirectReference().getObjNumber();
        try {
            if (ImageType.JBIG2 == imageXObject.identifyImageType())
                return new J2BIGImageReader(imageXObject, reference, matrix, pageNumber, imageNumber);

            if (imageXObject.getBufferedImage() != null)
                return new DefaultImageReader(imageXObject, reference, matrix, pageNumber, imageNumber);
        } catch (IOException e) {
            return new JpegCMYKImageReader(imageXObject, reference, matrix, pageNumber, imageNumber);
        } catch (com.itextpdf.io.IOException e) {
            return new PngIndexedImageReader(imageXObject, reference, matrix, pageNumber);
        }

        throw new IllegalStateException("Unsupported strategy");
    }

}
