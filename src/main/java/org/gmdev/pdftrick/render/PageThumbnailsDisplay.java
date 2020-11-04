package org.gmdev.pdftrick.render;

import com.itextpdf.kernel.pdf.canvas.parser.EventType;
import com.itextpdf.kernel.pdf.canvas.parser.data.IEventData;
import com.itextpdf.kernel.pdf.canvas.parser.data.ImageRenderInfo;
import com.itextpdf.kernel.pdf.canvas.parser.listener.IEventListener;
import com.itextpdf.kernel.pdf.xobject.PdfImageXObject;

import java.util.Collections;
import java.util.Set;

import static com.itextpdf.kernel.pdf.canvas.parser.EventType.RENDER_IMAGE;

public class PageThumbnailsDisplay implements IEventListener {

    private final int pageNumber;

    public PageThumbnailsDisplay(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    @Override
    public Set<EventType> getSupportedEvents() {
        return Collections.singleton(RENDER_IMAGE);
    }

    @Override
    public void eventOccurred(IEventData eventData, EventType eventType) {
        if (!(eventData instanceof ImageRenderInfo)) return;
        display((ImageRenderInfo) eventData);
    }

    private void display(ImageRenderInfo imageRenderInfo) {
        System.out.println("page: " + pageNumber);
        PdfImageXObject image = imageRenderInfo.getImage();
        System.out.println(image.identifyImageType());
    }

}
