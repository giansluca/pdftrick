package org.gmdev.pdftrick.rendering;

import com.itextpdf.kernel.pdf.canvas.parser.EventType;
import com.itextpdf.kernel.pdf.canvas.parser.data.*;
import com.itextpdf.kernel.pdf.canvas.parser.listener.IEventListener;
import org.gmdev.pdftrick.rendering.imagereader.*;
import org.gmdev.pdftrick.rendering.tasks.UpdateCenterPanelTask;
import org.gmdev.pdftrick.swingmanager.SwingInvoker;

import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static com.itextpdf.kernel.pdf.canvas.parser.EventType.RENDER_IMAGE;

public class PageThumbnailsDisplay implements IEventListener {

    private final int pageNumber;
    private int imageNumber;
    private int unsupportedImages;

    public PageThumbnailsDisplay(int pageNumber) {
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

        ImageAttributes imageAttributes = new ImageAttributes(
                bufferedImage,
                pdfImageReader.getExtension(),
                pdfImageReader.getKey());

        BufferedImage scaledBufferedImage = pdfImageReader.scaleImage(bufferedImage);

        SwingInvoker.invokeAndWait(
                new UpdateCenterPanelTask(
                        imageAttributes,
                        scaledBufferedImage)
        );

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
    }

    public int getImageNumber() {
        return imageNumber;
    }

    public int getUnsupportedImages() {
        return unsupportedImages;
    }

}
