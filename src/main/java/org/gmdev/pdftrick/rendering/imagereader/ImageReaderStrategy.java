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
            return new InlineImageReader(image);

        int ref = image.getPdfObject().getIndirectReference().getObjNumber();
        try {
            if (ImageType.JBIG2 == image.identifyImageType())
                return new J2BIGReaderPdf(image, ref);

            if (image.getBufferedImage() != null)
                return new DefaultReaderPdf(image, ref);

        } catch (IOException e) {
            return new JPEGColorSpaceCMYKReaderPdf(image, ref);
        } catch (com.itextpdf.io.IOException e) {
            return new PNGIndexedReader(image, ref);
        }

        throw new IllegalStateException("Unsupported strategy");
    }

}
