package org.gmdev.pdftrick.render.tasks;

import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.canvas.parser.PdfDocumentContentParser;
import org.gmdev.pdftrick.manager.PdfTrickBag;
import org.gmdev.pdftrick.render.PageThumbnailsDisplay;
import org.gmdev.pdftrick.serviceprocessor.ServiceRequest;
import org.gmdev.pdftrick.swingmanager.WaitPanel;

import javax.swing.*;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

public class RenderPageThumbnailsTask implements ServiceRequest {

    private static final PdfTrickBag bag = PdfTrickBag.getBag();

    private final int pageNumber;
    private final String pdfFilePath;
    private final AtomicBoolean exited;

    public RenderPageThumbnailsTask(int pageNumber) {
        this.pageNumber = pageNumber;
        pdfFilePath = bag.getPdfFilePath();
        exited = new AtomicBoolean(false);
    }

    @Override
    public void process() throws IOException {
        Properties messages = bag.getMessages();
        JPanel centerPanel = bag.getUserInterface().getCenter().getCenterPanel();

        WaitPanel.setLoadingThumbnailsWaitPanel();

        PdfReader pdfReader = new PdfReader(pdfFilePath);
        PdfDocument pdfDocument = new PdfDocument(pdfReader);
        PdfDocumentContentParser contentParser = new PdfDocumentContentParser(pdfDocument);

        PageThumbnailsDisplay pageThumbnailsDisplay = new PageThumbnailsDisplay(pageNumber);
        contentParser.processContent(pageNumber, pageThumbnailsDisplay);

        pdfReader.close();
        pdfDocument.close();

        // TODO other logic ....

        WaitPanel.removeWaitPanel();
        exited.set(true);
    }

    public void stop() {
        exited.set(true);
    }

    public boolean isExited() {
        return exited.get();
    }

}
