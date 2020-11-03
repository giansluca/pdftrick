package org.gmdev.pdftrick.render.tasks;

import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.canvas.parser.PdfDocumentContentParser;
import org.gmdev.pdftrick.factory.PdfTrickBag;
import org.gmdev.pdftrick.render.PageThumbnailsDisplay;
import org.gmdev.pdftrick.serviceprocessor.ServiceRequest;

import java.util.concurrent.atomic.AtomicBoolean;

public class RenderPageThumbnailsTask implements ServiceRequest {

    private static final PdfTrickBag bag = PdfTrickBag.getPdfTrickBag();

    private final int pageNumber;
    private final String pdfFilePath;
    private final AtomicBoolean exited;

    public RenderPageThumbnailsTask(int pageNumber) {
        this.pageNumber = pageNumber;
        pdfFilePath = bag.getResultFile();
        exited = new AtomicBoolean(false);
    }

    @Override
    public void process() throws Exception {
        PdfReader pdfReader = new PdfReader(pdfFilePath);
        PdfDocument pdfDocument = new PdfDocument(pdfReader);
        PdfDocumentContentParser contentParser = new PdfDocumentContentParser(pdfDocument);

        PageThumbnailsDisplay pageThumbnailsDisplay = new PageThumbnailsDisplay(pageNumber);
        contentParser.processContent(pageNumber, pageThumbnailsDisplay);
    }

    public void stop() {
        exited.set(true);
    }

    public boolean isExited() {
        return exited.get();
    }

}
