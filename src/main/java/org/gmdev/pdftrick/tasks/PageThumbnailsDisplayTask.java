package org.gmdev.pdftrick.tasks;

import java.awt.*;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.*;

import org.gmdev.pdftrick.engine.PageThumbnailsListener;
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

        PageThumbnailsListener pageThumbnailsListener = new PageThumbnailsListener(pageNumber);
        processPage(pageThumbnailsListener);

        String infoUnsupported = "";
        String infoAvailable;
        if (pageThumbnailsListener.getUnsupportedImages() > 0)
            infoUnsupported = MessageFormat.format(messages.getProperty("d_msg_02"), pageNumber);

        if (pageThumbnailsListener.getImageNumber() == 0) {
            setNoImagePage();
            infoAvailable = MessageFormat.format(messages.getProperty("d_msg_03"), pageNumber);
        } else {
            String availableMessage = pageThumbnailsListener.getImageNumber() > 1
                    ? messages.getProperty("t_msg_15")
                    : messages.getProperty("t_msg_16");

            infoAvailable = String.format(
                    "%s %s", pageThumbnailsListener.getImageNumber(), availableMessage);
        }

        Messages.append("INFO", String.format("%s %s", infoUnsupported, infoAvailable));
        WaitPanel.removeWaitPanel();
        running.set(false);
    }

    private void processPage(PageThumbnailsListener pageThumbnailsListener) {
        try {
            PdfReader pdfReader = new PdfReader(bag.getPdfFilePath().toString());
            PdfReaderContentParser pdfParser = new PdfReaderContentParser(pdfReader);

            pdfParser.processContent(pageNumber, pageThumbnailsListener);

            pdfReader.close();
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
