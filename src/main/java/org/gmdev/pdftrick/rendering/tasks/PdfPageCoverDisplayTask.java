package org.gmdev.pdftrick.rendering.tasks;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;

import org.gmdev.pdftrick.manager.PdfTrickBag;
import org.gmdev.pdftrick.ui.actions.PdfPageAction;
import org.gmdev.pdftrick.serviceprocessor.Stoppable;
import org.gmdev.pdftrick.swingmanager.SwingInvoker;
import org.gmdev.pdftrick.utils.*;

public class PdfPageCoverDisplayTask implements Runnable, Stoppable {

    private static final Comparator<File> comparator = (file1, file2) -> {
        Integer num1 = Integer.parseInt(
                file1.getName().substring(6).replace(".png", "").trim());
        Integer num2 = Integer.parseInt(
                file2.getName().substring(6).replace(".png", "").trim());
        return (num1.compareTo(num2));
    };

    private static final PdfTrickBag bag = PdfTrickBag.INSTANCE;

    private final AtomicBoolean running = new AtomicBoolean(false);
    private final Instant start = Instant.now();
    private Duration interval = Duration.ofSeconds(1);

    public void stop() {
        running.set(false);
    }

    public boolean isRunning() {
        return running.get();
    }

    @Override
    public void run() {
        running.set(true);

        Properties messages = bag.getMessagesProps();
        Path thumbnailsFolderPath = bag.getThumbnailsFolderPath();

        Messages.appendNoNewLine("INFO", messages.getProperty("t_msg_08"));
        int numberOfPages = bag.getNumberOfPages();

        int renderedCount = 0;
        while (renderedCount < numberOfPages && running.get()) {
            shouldPrintDot();

            File[] renderedImages = getPdfRenderedPages(thumbnailsFolderPath);

            if (renderedImages == null || renderedImages.length <= renderedCount) continue;
            if (!nextExpectedImageExists(renderedImages, renderedCount)) continue;
            if (lockFileExists(thumbnailsFolderPath, renderedCount)) continue;

            BufferedImage image = getImage(renderedImages[renderedCount]);
            if (image == null) continue;

            BufferedImage resizedImage = resizeImage(image);
            appendImageToLeftPanel(resizedImage);
            resizedImage.flush();

            renderedCount++;
        }

        if (running.get()) {
            Messages.appendNewLine();
            Messages.append("INFO", messages.getProperty("t_msg_10"));
            attachListenersToImages();
        }

        running.set(false);
    }

    private void shouldPrintDot() {
        Instant nextDotTime = start.plus(interval);

        Instant now = Instant.now();
        if (now.isAfter(nextDotTime)) {
            Messages.appendInline(".");
            interval = interval.plus(Duration.ofSeconds(1));
        }
    }

    private File[] getPdfRenderedPages(Path thumbnailsFolderPath) {
        File imgFolder = thumbnailsFolderPath.toFile();
        if (!imgFolder.exists())
            throw new IllegalStateException("Corrupted image folder");

        File[] renderedPdfThumbnails = imgFolder
                .listFiles((dir, name) -> name.toLowerCase().endsWith(".png"));
        if (renderedPdfThumbnails == null) return null;

        Arrays.sort(renderedPdfThumbnails, comparator);
        return renderedPdfThumbnails;
    }

    private boolean nextExpectedImageExists(File[] renderedImages, int renderedCount) {
        if (renderedImages[renderedCount] == null) return false;
        return renderedImages[renderedCount].getName().endsWith("_" + (renderedCount + 1) + ".png");
    }

    private boolean lockFileExists(Path thumbnailsFolderPath, int renderedCount) {
        File lockFile = new File(thumbnailsFolderPath +
                File.separator + "image_" + (renderedCount + 1) + ".png.lock");
        return lockFile.exists();
    }

    private BufferedImage getImage(File imageFile) {
        if (!imageFile.exists() || !imageFile.canRead() || !(imageFile.length() > 0)) return null;

        try (FileInputStream in = new FileInputStream(imageFile)) {
            return ImageIO.read(in);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private BufferedImage resizeImage(BufferedImage image) {
        BufferedImage resizedImage;
        if (image.getWidth() > image.getHeight())
            resizedImage = ImageUtils.getScaledImage(image, 170, 126);
        else
            resizedImage = ImageUtils.getScaledImage(image, 170, 228);

        image.flush();
        return resizedImage;
    }

    private void appendImageToLeftPanel(BufferedImage resizedImage) {
        JPanel leftPanel = bag.getUserInterface().getLeft().getLeftPanel();

        SwingInvoker.invokeAndWait(() -> {
            Border border = BorderFactory.createLineBorder(Color.gray);
            JLabel imageLabel = new JLabel(new ImageIcon(resizedImage));
            imageLabel.setPreferredSize(new Dimension(176, 236));
            imageLabel.setBorder(border);
            leftPanel.add(imageLabel);
            leftPanel.revalidate();
            leftPanel.repaint();
        });
    }

    private void attachListenersToImages() {
        JPanel leftPanel = bag.getUserInterface().getLeft().getLeftPanel();

        SwingInvoker.invokeLater(() -> {
            Component[] comps = leftPanel.getComponents();
            for (int z = 0; z < comps.length; z++) {
                JLabel picLabel = (JLabel) comps[z];
                picLabel.addMouseListener(new PdfPageAction(z + 1));
                picLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
            bag.getUserInterface().getCenter().stopWaitIcon();
        });
    }


}
