package org.gmdev.pdftrick.rendering.tasks;

import java.awt.*;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.*;

import org.gmdev.pdftrick.rendering.PageThumbnailsDisplay;
import org.gmdev.pdftrick.manager.PdfTrickBag;
import org.gmdev.pdftrick.swingmanager.*;
import org.gmdev.pdftrick.utils.Messages;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;

public class PageThumbnailsDisplayTask implements Runnable {

    private static final PdfTrickBag bag = PdfTrickBag.INSTANCE;
    public static final String NO_PICTURES = "NoPicsImg";

    private final int pageNumber;
    private final AtomicBoolean running = new AtomicBoolean(false);

    public PageThumbnailsDisplayTask(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public boolean isRunning() {
        return running.get();
    }

    @Override
    public void run() {
        running.set(true);
        Properties messages = bag.getMessagesProps();
        WaitPanel.setLoadingThumbnailsWaitPanel();

        PageThumbnailsDisplay pageThumbnailsDisplay = new PageThumbnailsDisplay(pageNumber);
        processPage(pageThumbnailsDisplay);

        String infoUnsupported = "";
        String infoAvailable;
        if (pageThumbnailsDisplay.getUnsupportedImages() > 0)
            infoUnsupported = MessageFormat.format(messages.getProperty("d_msg_02"), pageNumber);

        if (pageThumbnailsDisplay.getImageNumber() == 0) {
            setNoImagePage();
            infoAvailable = MessageFormat.format(messages.getProperty("d_msg_03"), pageNumber);
        } else {
            String availableMessage = pageThumbnailsDisplay.getImageNumber() > 1
                    ? messages.getProperty("t_msg_15")
                    : messages.getProperty("t_msg_16");

            infoAvailable = String.format(
                    "%s %s", pageThumbnailsDisplay.getImageNumber(), availableMessage);
        }

        Messages.append("INFO", String.format("%s %s", infoUnsupported, infoAvailable));
        WaitPanel.removeWaitPanel();
        running.set(false);
    }

    private void processPage(PageThumbnailsDisplay pageThumbnailsDisplay) {
        try {
            PdfReader reader = new PdfReader(bag.getSavedFilePath().toString());
            PdfReaderContentParser pdfParser = new PdfReaderContentParser(reader);

            pdfParser.processContent(pageNumber, pageThumbnailsDisplay);

            reader.close();
        } catch (IOException e) {
            throw new IllegalStateException(e);
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
