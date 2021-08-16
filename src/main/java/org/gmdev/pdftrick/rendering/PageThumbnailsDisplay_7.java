package org.gmdev.pdftrick.rendering;

import com.itextpdf.kernel.pdf.canvas.parser.EventType;
import com.itextpdf.kernel.pdf.canvas.parser.data.IEventData;
import com.itextpdf.kernel.pdf.canvas.parser.data.ImageRenderInfo;
import com.itextpdf.kernel.pdf.canvas.parser.listener.IEventListener;
import com.itextpdf.kernel.pdf.xobject.PdfImageXObject;
import org.gmdev.pdftrick.rendering.imagereader.PdfImageReader;
import org.gmdev.pdftrick.rendering.imagereader.ImageReaderStrategy;
import org.gmdev.pdftrick.rendering.tasks.UpdateCenterPanelTask;
import org.gmdev.pdftrick.swingmanager.SwingInvoker;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static com.itextpdf.kernel.pdf.canvas.parser.EventType.RENDER_IMAGE;

public class PageThumbnailsDisplay_7 implements IEventListener {

    private final int pageNumber;
    private int imageNumber;
    private int unsupportedImages;

    public PageThumbnailsDisplay_7(int pageNumber) {
        this.pageNumber = pageNumber;
        this.imageNumber = 0;
        this.unsupportedImages = 0;
    }

    @Override
    public Set<EventType> getSupportedEvents() {
        return Collections.singleton(RENDER_IMAGE);
    }

    @Override
    public void eventOccurred(IEventData eventData, EventType eventType) {
        if (eventType != RENDER_IMAGE) return;
        display((ImageRenderInfo) eventData);
    }

    private void display(ImageRenderInfo imageRenderInfo) {
        PdfImageReader pdfImageReader = ImageReaderStrategy.getReader(imageRenderInfo, pageNumber, ++imageNumber);

        Optional<BufferedImage> bufferedImageMaybe = pdfImageReader.readImage();
        if (bufferedImageMaybe.isEmpty()) {
            unsupportedImages++;
            return;
        }

        BufferedImage bufferedImage = bufferedImageMaybe.get();
        BufferedImage scaledBufferedImage = pdfImageReader.scaleImage(bufferedImage);

        //writeImage(bufferedImage, pdfImageReader.getImageObject());
        SwingInvoker.invokeAndWait(new UpdateCenterPanelTask(pdfImageReader, scaledBufferedImage));

// code for extractions with itext 7
//        try {
//            PdfReader reader = new PdfReader("filepath");
//            PdfDocument pdfDoc = new PdfDocument(reader);
//
//            PdfObject obj = pdfDoc.getPdfObject(objNumber);
//            if (obj != null && obj.isStream()) {
//                //byte[] bytes = ((PdfStream) obj).getBytes();
//                PdfImageXObject xObject = new PdfImageXObject((PdfStream) obj);
//                BufferedImage bufferedImage = xObject.getBufferedImage();
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        // TODO this class is a test for migration to Itext 7
    }

    private void writeImage(BufferedImage bufferedImage, PdfImageXObject image) {
        try {
            String imageType = image.identifyImageFileExtension();
            String imagePath = String.format(
                    "/Users/gians/Desktop/pdftrick-pdf-for-test/out/%s.%s",
                    imageNumber, imageType);

            ImageIO.write(
                    bufferedImage,
                    imageType,
                    new File(imagePath)
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
