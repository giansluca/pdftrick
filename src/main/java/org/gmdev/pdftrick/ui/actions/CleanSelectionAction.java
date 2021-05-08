package org.gmdev.pdftrick.ui.actions;

import java.awt.event.*;
import java.util.*;

import javax.swing.*;

import org.gmdev.pdftrick.manager.*;
import org.gmdev.pdftrick.rendering.tasks.PageThumbnailsDisplayTask;
import org.gmdev.pdftrick.utils.Messages;

public class CleanSelectionAction extends AbstractAction {

    private static final PdfTrickBag bag = PdfTrickBag.INSTANCE;

    @Override
    public void actionPerformed(ActionEvent event) {
        if(!isAppFree()) return;

        bag.getUserInterface().getCenter().cleanSelection();
        bag.cleanSelectedImagesHashMap();
        bag.cleanInlineSelectedImagesHashMap();

        JTextField selectedImagesField = bag.getUserInterface().getRight().getSelectedImagesField();
        selectedImagesField.setText("");
    }

    public boolean isAppFree() {
        Properties messagesProps = bag.getMessagesProps();
        TasksContainer tasksContainer = bag.getTasksContainer();

        var imagesExtractionTask = tasksContainer.getImagesExtractionTask();
        if (imagesExtractionTask != null && imagesExtractionTask.isRunning()) {
            Messages.append("WARNING", messagesProps.getProperty("t_msg_02"));
            return false;
        }

        PageThumbnailsDisplayTask pageThumbnailsDisplayTask = tasksContainer.getPageThumbnailsDisplayTask();
        if (pageThumbnailsDisplayTask != null && pageThumbnailsDisplayTask.isRunning()) {
            Messages.append("WARNING", messagesProps.getProperty("t_msg_23"));
            return false;
        }

        var showPdfCoverThumbnailsTask = tasksContainer.getPdfPageDisplayTask();
        if (showPdfCoverThumbnailsTask != null && showPdfCoverThumbnailsTask.isRunning()) {
            return false;
        }

        if (bag.getSelectedImages().size() == 0 && bag.getInlineSelectedImages().size() == 0) {
            Messages.append("INFO", messagesProps.getProperty("t_msg_24"));
            return false;
        }

        return true;
    }


}
