package org.gmdev.pdftrick.render.tasks;

import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.canvas.parser.PdfDocumentContentParser;
import org.gmdev.pdftrick.manager.PdfTrickBag;
import org.gmdev.pdftrick.render.PageThumbnailsDisplay;
import org.gmdev.pdftrick.serviceprocessor.ServiceRequest;
import org.gmdev.pdftrick.swingmanager.WaitPanel;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

public class RenderPageThumbnailsTask implements ServiceRequest {

    private static final PdfTrickBag bag = PdfTrickBag.INSTANCE;

    private final int pageNumber;
    private final Path pdfFilePath;
    private final AtomicBoolean exited;

    public RenderPageThumbnailsTask(int pageNumber) {
        this.pageNumber = pageNumber;
        pdfFilePath = bag.getSavedFilePath();
        exited = new AtomicBoolean(false);
    }

    @Override
    public void process() throws IOException {
        Properties messages = bag.getMessagesProps();
        JPanel centerPanel = bag.getUserInterface().getCenter().getCenterPanel();

        WaitPanel.setLoadingThumbnailsWaitPanel();

        PdfReader reader = new PdfReader(pdfFilePath.toString());
        PdfDocument document = new PdfDocument(reader);
        PdfDocumentContentParser contentParser = new PdfDocumentContentParser(document);

        PageThumbnailsDisplay pageThumbnailsDisplay = new PageThumbnailsDisplay(pageNumber);
        contentParser.processContent(pageNumber, pageThumbnailsDisplay);

        reader.close();
        document.close();

        // FIXME close reader and document here ?

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
