package org.gmdev.pdftrick.rendering.imagereader;

import com.itextpdf.io.image.ImageType;
import com.itextpdf.kernel.geom.Matrix;
import com.itextpdf.kernel.pdf.canvas.parser.data.ImageRenderInfo;
import com.itextpdf.kernel.pdf.xobject.PdfImageXObject;

import java.io.IOException;

public class ImageReaderStrategy {

    public static PdfImageReader getReader(ImageRenderInfo imageRenderInfo, int pageNumber, int imageNumber) {
        PdfImageXObject image = imageRenderInfo.getImage();
        Matrix matrix = imageRenderInfo.getImageCtm();

        if (imageRenderInfo.isInline())
            return new InlineImageReader(image, matrix, pageNumber, imageNumber);

        int reference = image.getPdfObject().getIndirectReference().getObjNumber();
        try {
            if (ImageType.JBIG2 == image.identifyImageType())
                return new J2BIGImageReader(image, reference, matrix, pageNumber, imageNumber);

            if (image.getBufferedImage() != null)
                return new DefaultImageReader(image, reference, matrix, pageNumber, imageNumber);
        } catch (IOException e) {
            return new JpegCMYKImageReader(image, reference, matrix, pageNumber, imageNumber);
        } catch (com.itextpdf.io.IOException e) {
            return new PngIndexedImageReader(image, reference, matrix, pageNumber);
        }

        throw new IllegalStateException("Unsupported strategy");
    }

}
