package org.gmdev.pdftrick.rendering.imagereader;

import com.itextpdf.io.image.ImageType;
import com.itextpdf.kernel.geom.Matrix;
import com.itextpdf.kernel.pdf.canvas.parser.data.ImageRenderInfo;
import com.itextpdf.kernel.pdf.xobject.PdfImageXObject;

import java.io.IOException;

public class ImageReaderStrategy {

    public static PdfImageReader getReader(ImageRenderInfo imageRenderInfo, int pageNumber) {
        PdfImageXObject image = imageRenderInfo.getImage();
        Matrix matrix = imageRenderInfo.getImageCtm();

        if (imageRenderInfo.isInline())
            return new InlineImageReader(image, matrix, pageNumber);

        int reference = image.getPdfObject().getIndirectReference().getObjNumber();
        try {
            if (ImageType.JBIG2 == image.identifyImageType())
                return new J2BIGReaderPdf(image, reference, matrix, pageNumber);

            if (image.getBufferedImage() != null)
                return new DefaultReaderPdf(image, reference, matrix, pageNumber);

        } catch (IOException e) {
            return new JPEGColorSpaceCMYKReaderPdf(image, reference, matrix, pageNumber);
        } catch (com.itextpdf.io.IOException e) {
            return new PNGIndexedReader(image, reference, matrix, pageNumber);
        }

        throw new IllegalStateException("Unsupported strategy");
    }

}
