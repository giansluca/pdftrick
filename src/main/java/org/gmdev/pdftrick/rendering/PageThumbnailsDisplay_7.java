package org.gmdev.pdftrick.rendering;

import com.itextpdf.kernel.pdf.canvas.parser.EventType;
import com.itextpdf.kernel.pdf.canvas.parser.data.IEventData;
import com.itextpdf.kernel.pdf.canvas.parser.data.ImageRenderInfo;
import com.itextpdf.kernel.pdf.canvas.parser.listener.IEventListener;
import org.gmdev.pdftrick.rendering.imagereader.PdfImageReader;
import org.gmdev.pdftrick.rendering.imagereader.ImageReaderStrategy;

import java.util.Collections;
import java.util.Set;

import static com.itextpdf.kernel.pdf.canvas.parser.EventType.RENDER_IMAGE;

public class PageThumbnailsDisplay_7 implements IEventListener {

    private final int pageNumber;

    public PageThumbnailsDisplay_7(int pageNumber) {
        this.pageNumber = pageNumber;
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
        PdfImageReader pdfImageReader = ImageReaderStrategy.getReader(imageRenderInfo);

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

}
