package org.gmdev.pdftrick.ui.actions;

import java.awt.Color;
import java.awt.event.*;
import java.util.Map;

import javax.swing.*;
import javax.swing.border.Border;

import org.gmdev.pdftrick.manager.PdfTrickBag;
import org.gmdev.pdftrick.rendering.imagereader.ImageAttributes;

public class ImageAction implements MouseListener {

    private static final PdfTrickBag bag = PdfTrickBag.INSTANCE;

    private final JLabel imageLabel;
    private final ImageAttributes imageAttributes;
    private boolean selected;
    private final Map<String, ImageAttributes> selectedImages;

    public ImageAction(JLabel imageLabel, ImageAttributes imageAttributes, boolean selected) {
        this.imageLabel = imageLabel;
        this.imageAttributes = imageAttributes;
        this.selected = selected;
        this.selectedImages = bag.getSelectedImages();
    }

    private void manageClick() {
        JTextField selectedImagesField = bag.getUserInterface().getRight().getSelectedImagesField();
        Border borderGray = BorderFactory.createLineBorder(Color.gray);
        Border borderOrange = BorderFactory.createMatteBorder(2, 2, 2, 2, Color.orange);

        if (!selected) {
            imageLabel.setBorder(borderOrange);
            selectedImages.put(imageAttributes.getKey(), imageAttributes);
            selected = true;

            selectedImagesField.setText(String.format("Selected %s", selectedImages.size()));
        } else {
            imageLabel.setBorder(borderGray);
            imageLabel.setOpaque(false);
            imageLabel.setBackground(Color.WHITE);

            selectedImages.remove(imageAttributes.getKey());
            selected = false;

            if (selectedImages.size() == 0)
                selectedImagesField.setText("");
            else
                selectedImagesField.setText(String.format("Selected %s", selectedImages.size()));
        }
    }


    @Override
    public void mouseClicked(MouseEvent e) {
        manageClick();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

}
