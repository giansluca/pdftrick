package org.gmdev.pdftrick.ui.actions;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.border.Border;

import org.gmdev.pdftrick.manager.*;
import org.gmdev.pdftrick.render.ImageAction;
import org.gmdev.pdftrick.tasks.PageThumbnailsDisplayTask;
import org.gmdev.pdftrick.utils.Messages;

import static org.gmdev.pdftrick.tasks.PageThumbnailsDisplayTask.*;

/**
 * Action called when click 'Clean' button
 */
public class CleanSelectionAction extends AbstractAction {

    private static final PdfTrickBag bag = PdfTrickBag.INSTANCE;

    @Override
    public void actionPerformed(ActionEvent event) {
        Properties messagesProps = bag.getMessagesProps();
        JPanel centerPanel = bag.getUserInterface().getCenter().getCenterPanel();
        TasksContainer tasksContainer = bag.getTasksContainer();

        var imagesExtractionTask = tasksContainer.getImagesExtractionTask();
        if (imagesExtractionTask != null && imagesExtractionTask.isRunning()) {
            Messages.append("WARNING", messagesProps.getProperty("t_msg_02"));
            return;
        }

        PageThumbnailsDisplayTask pageThumbnailsDisplayTask = tasksContainer.getPageThumbnailsDisplayTask();
        if (pageThumbnailsDisplayTask != null && pageThumbnailsDisplayTask.isRunning()) {
            Messages.append("WARNING", messagesProps.getProperty("t_msg_23"));
            return;
        }

        var showPdfCoverThumbnailsTask = tasksContainer.getPdfCoverThumbnailsDisplayTask();
        if (showPdfCoverThumbnailsTask != null && showPdfCoverThumbnailsTask.isRunning()) {
            return;
        }

        if (bag.getSelectedImages().size() == 0 && bag.getInlineSelectedImages().size() == 0) {
            Messages.append("INFO", messagesProps.getProperty("t_msg_24"));
            return;
        }

        Border borderGray = BorderFactory.createLineBorder(Color.gray);
        Component[] components = centerPanel.getComponents();
        for (Component c : components) {
            if (!(c instanceof JLabel)) continue;

            JLabel picLabel = (JLabel) c;
            String name = picLabel.getName() != null ? picLabel.getName() : "";

            if (name.equals(NO_PICTURES)) continue;

            picLabel.setBorder(borderGray);
            picLabel.setOpaque(true);
            picLabel.setBackground(Color.WHITE);
            MouseListener[] mouseListeners = (picLabel.getListeners(MouseListener.class));

            if (mouseListeners.length > 0) {
                ImageAction imageAction = (ImageAction) mouseListeners[0];
                imageAction.setSelected(false);
            }
        }

        bag.cleanSelectedImagesHashMap();
        bag.cleanInlineSelectedImagesHashMap();

        JTextField selectedImagesField = bag.getUserInterface().getRight().getSelectedImagesField();
        selectedImagesField.setText("");
    }


}
