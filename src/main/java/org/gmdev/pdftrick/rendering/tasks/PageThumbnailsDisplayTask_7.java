package org.gmdev.pdftrick.rendering.tasks;

import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.canvas.parser.PdfDocumentContentParser;
import org.gmdev.pdftrick.manager.PdfTrickBag;
import org.gmdev.pdftrick.rendering.PageThumbnailsDisplay_7;
import org.gmdev.pdftrick.serviceprocessor.ServiceRequest;
import org.gmdev.pdftrick.swingmanager.WaitPanel;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

public class PageThumbnailsDisplayTask_7 implements ServiceRequest {

    private static final PdfTrickBag bag = PdfTrickBag.INSTANCE;

    private final int pageNumber;
    private final Path pdfFilePath;
    private final AtomicBoolean exited;

    public PageThumbnailsDisplayTask_7(int pageNumber) {
        this.pageNumber = pageNumber;
        pdfFilePath = bag.getSavedFilePath();
        exited = new AtomicBoolean(false);
    }

    @Override
    public void process() throws IOException {
        // TODO this class is a test for migration to Itext 7

        Properties messages = bag.getMessagesProps();
        JPanel centerPanel = bag.getUserInterface().getCenter().getCenterPanel();

        WaitPanel.setLoadingThumbnailsWaitPanel();

        PdfReader reader = new PdfReader(pdfFilePath.toString());
        PdfDocument document = new PdfDocument(reader);
        PdfDocumentContentParser contentParser = new PdfDocumentContentParser(document);

        PageThumbnailsDisplay_7 pageThumbnailsDisplay7 = new PageThumbnailsDisplay_7(pageNumber);
        contentParser.processContent(pageNumber, pageThumbnailsDisplay7);

        reader.close();
        document.close();
        //WaitPanel.removeWaitPanel(); // TODO commented to avoid double removing
        exited.set(true);
    }

    public void stop() {
        exited.set(true);
    }

    public boolean isExited() {
        return exited.get();
    }

}
