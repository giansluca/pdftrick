package org.gmdev.pdftrick.rendering.tasks;

import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.canvas.parser.PdfDocumentContentParser;
import org.gmdev.pdftrick.manager.PdfTrickBag;
import org.gmdev.pdftrick.rendering.PageThumbnailsManager;
import org.gmdev.pdftrick.swingmanager.*;
import org.gmdev.pdftrick.utils.Messages;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

public class PageThumbnailsDisplayTask implements Runnable {

    private static final PdfTrickBag bag = PdfTrickBag.INSTANCE;
    public static final String NO_PICTURES = "NoPicsImg";

    private final int pageNumber;
    private final Path pdfFilePath;
    private final AtomicBoolean running;

    public PageThumbnailsDisplayTask(int pageNumber) {
        this.pageNumber = pageNumber;
        this.pdfFilePath = bag.getSavedFilePath();
        this.running = new AtomicBoolean(false);
    }

    public boolean isRunning() {
        return running.get();
    }

    @Override
    public void run() {
        running.set(true);

        Properties messages = bag.getMessagesProps();
        WaitPanel.setLoadingThumbnailsWaitPanel();

        PageThumbnailsManager pageThumbnailsManager = new PageThumbnailsManager(pageNumber);
        processPage(pageThumbnailsManager);

        String infoUnsupported = "";
        String infoAvailable;
        if (pageThumbnailsManager.getUnsupportedImages() > 0)
            infoUnsupported = MessageFormat.format(messages.getProperty("d_msg_02"), pageNumber);

        if (pageThumbnailsManager.getImageNumber() == 0) {
            setNoImagePage();
            infoAvailable = MessageFormat.format(messages.getProperty("d_msg_03"), pageNumber);
        } else {
            String availableMessage = pageThumbnailsManager.getImageNumber() > 1
                    ? messages.getProperty("t_msg_15")
                    : messages.getProperty("t_msg_16");

            infoAvailable = String.format(
                    "%s %s", pageThumbnailsManager.getImageNumber(), availableMessage);
        }

        Messages.append("INFO", String.format("%s %s", infoUnsupported, infoAvailable));

        WaitPanel.removeWaitPanel();
        running.set(false);
    }

    private void processPage(PageThumbnailsManager pageThumbnailsManager) {
        try {
            PdfReader reader = new PdfReader(pdfFilePath.toString());
            PdfDocument document = new PdfDocument(reader);
            PdfDocumentContentParser contentParser = new PdfDocumentContentParser(document);

            contentParser.processContent(pageNumber, pageThumbnailsManager);

            reader.close();
            document.close();
        } catch (IOException e) {
            throw new IllegalStateException("Error parsing page");
        }
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


}
