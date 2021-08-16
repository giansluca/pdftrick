package org.gmdev.pdftrick.rendering.tasks;

import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.canvas.parser.PdfDocumentContentParser;
import org.gmdev.pdftrick.manager.PdfTrickBag;
import org.gmdev.pdftrick.rendering.PageThumbnailsDisplay_7;
import org.gmdev.pdftrick.serviceprocessor.ServiceRequest;
import org.gmdev.pdftrick.swingmanager.SwingInvoker;
import org.gmdev.pdftrick.swingmanager.WaitPanel;
import org.gmdev.pdftrick.utils.Messages;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

public class PageThumbnailsDisplayTask_7 implements ServiceRequest {

    private static final PdfTrickBag bag = PdfTrickBag.INSTANCE;
    public static final String NO_PICTURES = "NoPicsImg";

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

        Properties messages = bag.getMessagesProps();
        JPanel centerPanel = bag.getUserInterface().getCenter().getCenterPanel();

        WaitPanel.setLoadingThumbnailsWaitPanel();

        PageThumbnailsDisplay_7 pageThumbnailsDisplay = new PageThumbnailsDisplay_7(pageNumber);
        processPage(pageThumbnailsDisplay);

        // TODO migration to Itext 7
//        String infoUnsupported = "";
//        String infoAvailable;
//        if (pageThumbnailsDisplay.getUnsupportedImages() > 0)
//            infoUnsupported = MessageFormat.format(messages.getProperty("d_msg_02"), pageNumber);
//
//        if (pageThumbnailsDisplay.getImageNumber() == 0) {
//            setNoImagePage();
//            infoAvailable = MessageFormat.format(messages.getProperty("d_msg_03"), pageNumber);
//        } else {
//            String availableMessage = pageThumbnailsDisplay.getImageNumber() > 1
//                    ? messages.getProperty("t_msg_15")
//                    : messages.getProperty("t_msg_16");
//
//            infoAvailable = String.format(
//                    "%s %s", pageThumbnailsDisplay.getImageNumber(), availableMessage);
//        }
//
//        Messages.append("INFO", String.format("%s %s", infoUnsupported, infoAvailable));
//

        //WaitPanel.removeWaitPanel(); // TODO commented to avoid double removing
        exited.set(true);
    }

    private void processPage(PageThumbnailsDisplay_7 pageThumbnailsDisplay) throws IOException {
        PdfReader reader = new PdfReader(pdfFilePath.toString());
        PdfDocument document = new PdfDocument(reader);
        PdfDocumentContentParser contentParser = new PdfDocumentContentParser(document);

        contentParser.processContent(pageNumber, pageThumbnailsDisplay);

        reader.close();
        document.close();
    }

    private void setNoImagePage() {
        Properties messages = bag.getMessagesProps();
        JPanel centerPanel = bag.getUserInterface().getCenter().getCenterPanel();

        SwingInvoker.invokeLater(() -> {
            JLabel noImageLabel = new JLabel(messages.getProperty("t_msg_07"));
            noImageLabel.setHorizontalAlignment(JLabel.CENTER);
            noImageLabel.setVerticalAlignment(JLabel.CENTER);
            noImageLabel.setFont(new Font("Verdana", Font.BOLD, 20));
            noImageLabel.setName(NO_PICTURES);
            centerPanel.setLayout(new GridBagLayout());
            centerPanel.add(noImageLabel);
            centerPanel.revalidate();
            centerPanel.repaint();
        });
    }

    public void stop() {
        exited.set(true);
    }

    public boolean isExited() {
        return exited.get();
    }

}
