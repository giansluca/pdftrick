package org.gmdev.pdftrick.rendering.tasks;

import org.gmdev.pdftrick.manager.PdfTrickBag;
import org.gmdev.pdftrick.rendering.imagereader.PdfImageReader;
import org.gmdev.pdftrick.ui.actions.ImageAction;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Map;

import static org.gmdev.pdftrick.utils.Constants.THUMBNAIL_MAX_SIZE_HEIGHT;
import static org.gmdev.pdftrick.utils.Constants.THUMBNAIL_MAX_SIZE_WIDTH;

public class UpdateCenterPanelTask implements Runnable {

    private static final PdfTrickBag bag = PdfTrickBag.INSTANCE;

    private final Border borderGray = BorderFactory.createLineBorder(Color.gray);
    private final Border borderOrange = BorderFactory.createMatteBorder(2, 2, 2, 2, Color.orange);

    private final PdfImageReader pdfImageReader;
    private final BufferedImage bufferedImage;
    private final JPanel centerPanel;
    private final Map<String, PdfImageReader> selectedImages;

    public UpdateCenterPanelTask(PdfImageReader pdfImageReader, BufferedImage bufferedImage) {
        this.pdfImageReader = pdfImageReader;
        this.bufferedImage = bufferedImage;
        this.centerPanel = bag.getUserInterface().getCenter().getCenterPanel();
        this.selectedImages = bag.getSelectedImagesV2();
    }

    @Override
    public void run() {
        JLabel imageLabel = new JLabel(new ImageIcon(bufferedImage));
        imageLabel.setPreferredSize(
                new Dimension(THUMBNAIL_MAX_SIZE_WIDTH, THUMBNAIL_MAX_SIZE_HEIGHT));

        boolean selected = false;
        if (selectedImages.containsKey(pdfImageReader.getKey())) {
            imageLabel.setBorder(borderOrange);
            selected = true;
        } else {
            imageLabel.setBorder(borderGray);
        }

        // TODO
//        imageLabel.addMouseListener(new ImageAction(imageLabel, imageAttrs, isInlineImg, selected));
//        imageLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
//
//        centerPanel.add(imageLabel);
//        centerPanel.revalidate();
//        centerPanel.repaint();

    }


}
