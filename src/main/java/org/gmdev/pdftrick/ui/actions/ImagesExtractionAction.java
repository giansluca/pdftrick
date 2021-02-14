package org.gmdev.pdftrick.ui.actions;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.io.File;
import java.nio.file.Path;
import java.util.*;

import javax.swing.*;

import org.gmdev.pdftrick.manager.*;
import org.gmdev.pdftrick.swingmanager.ModalWarningPanel;
import org.gmdev.pdftrick.tasks.*;
import org.gmdev.pdftrick.ui.custom.CustomFileChooser;
import org.gmdev.pdftrick.utils.*;

/**
 * Action called when 'Get img' button is clicked
 */
public class ImagesExtractionAction extends AbstractAction {

    private static final PdfTrickBag bag = PdfTrickBag.INSTANCE;

    @Override
    public void actionPerformed(ActionEvent event) {
        if (!isAppFree()) return;

        Properties messages = bag.getMessagesProps();
        Container contentPanel = bag.getUserInterface().getContentPane();
        TasksContainer tasksContainer = bag.getTasksContainer();

        File pdfFile = bag.getPdfFilePath().toFile();
        if (!pdfFile.exists()) {
            Messages.append("INFO", messages.getProperty("t_msg_04"));
            return;
        }

        Set<String> normalImageKeys = bag.getSelectedImages().keySet();
        Set<String> inlineImageKeys = bag.getInlineSelectedImages().keySet();
        if (normalImageKeys.size() == 0 && inlineImageKeys.size() == 0) {
            Messages.append("INFO", messages.getProperty("t_msg_03"));
            return;
        }

        CustomFileChooser chooseFolderToSave = new CustomFileChooser();
        chooseFolderToSave.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooseFolderToSave.setDialogTitle(Constants.JFC_EXTRACT_TITLE);
        if (chooseFolderToSave.showSaveDialog(contentPanel) != JFileChooser.APPROVE_OPTION)
            return;

        String extractionFolder;
        if (bag.getOs().equals(SetupUtils.WIN_OS))
            extractionFolder = chooseFolderToSave.getSelectedFile().getAbsolutePath();
        else
            extractionFolder = chooseFolderToSave.getCurrentDirectory().getAbsolutePath();

        bag.setExtractionFolderPath(Path.of(extractionFolder));

        ImagesExtractionTask imagesExtractionTask = new ImagesExtractionTask();
        tasksContainer.setImagesExtractionTask(imagesExtractionTask);

        Thread imagesExtractionThread = new Thread(imagesExtractionTask);
        tasksContainer.setImagesExtractionThread(imagesExtractionThread);

        imagesExtractionThread.start();
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
            ModalWarningPanel.displayLoadingPdfThumbnailsWarning();
            return false;
        }

        return true;
    }


}
