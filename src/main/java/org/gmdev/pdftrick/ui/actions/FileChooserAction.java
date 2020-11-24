package org.gmdev.pdftrick.ui.actions;

import java.awt.Container;
import java.awt.event.*;
import java.io.File;
import java.util.Properties;

import javax.swing.*;

import org.gmdev.pdftrick.manager.*;
import org.gmdev.pdftrick.swingmanager.ModalWarningPanel;
import org.gmdev.pdftrick.tasks.FileChooserTask;
import org.gmdev.pdftrick.tasks.PageThumbnailsDisplayTask;
import org.gmdev.pdftrick.ui.custom.CustomFileChooser;
import org.gmdev.pdftrick.ui.panels.LeftPanel;
import org.gmdev.pdftrick.utils.*;

import static org.gmdev.pdftrick.utils.Constants.JFC_OPEN_TITLE;
import static org.gmdev.pdftrick.utils.SetupUtils.WIN_OS;

public class FileChooserAction extends AbstractAction {

    private static final PdfTrickBag BAG = PdfTrickBag.INSTANCE;
    private static final String ACTION_NAME = "Open";

    public FileChooserAction() {
        ImageIcon open_icon = new ImageIcon(FileLoader.loadFileAsUrl(Constants.OPEN_FILE_ICO));
        super.putValue(NAME, ACTION_NAME);
        super.putValue(SMALL_ICON, open_icon);
        if (BAG.getOs().equals(WIN_OS))
            super.putValue(
                    ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
        else
            super.putValue(
                    ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.META_DOWN_MASK));
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        LeftPanel leftPanel = BAG.getUserInterface().getLeft();
        Container contentPanel = BAG.getUserInterface().getContentPane();
        Properties messagesProps = BAG.getMessagesProps();
        TasksContainer tasksContainer = BAG.getTasksContainer();

        CustomFileChooser fileOpen = new CustomFileChooser();
        fileOpen.setMultiSelectionEnabled(true);
        fileOpen.setDialogTitle(JFC_OPEN_TITLE);

        int ret = fileOpen.showOpenDialog(contentPanel);
        if (ret != JFileChooser.APPROVE_OPTION) return;

        var dragAndDropTask = tasksContainer.getDragAndDropTask();
        var fileChooserTask = tasksContainer.getFileChooserTask();
        if ((dragAndDropTask != null && dragAndDropTask.isRunning()) ||
                (fileChooserTask != null && fileChooserTask.isRunning())) {

            leftPanel.resetLeftPanelFileDropBorder();
            Messages.append("WARNING", messagesProps.getProperty("tmsg_01"));
            return;
        }

        var showPdfCoverThumbnailsTask = tasksContainer.getPdfCoverThumbnailsDisplayTask();
        if (showPdfCoverThumbnailsTask != null && showPdfCoverThumbnailsTask.isRunning()) {
            leftPanel.resetLeftPanelFileDropBorder();
            ModalWarningPanel.displayLoadingPdfThumbnailsWarning();
            return;
        }

        var imagesExtractionTask = tasksContainer.getImagesExtractionTask();
        if (imagesExtractionTask != null && imagesExtractionTask.isRunning()) {
            leftPanel.resetLeftPanelFileDropBorder();
            ModalWarningPanel.displayExtractingImagesWarning();
            return;
        }

        PageThumbnailsDisplayTask pageThumbnailsDisplayTask = tasksContainer.getPageThumbnailsDisplayTask();
        if (pageThumbnailsDisplayTask != null && pageThumbnailsDisplayTask.isRunning()) {
            leftPanel.resetLeftPanelFileDropBorder();
            ModalWarningPanel.displayLoadingPageThumbnailImagesWarning();
            return;
        }

        File[] files = fileOpen.getSelectedFiles();

        FileChooserTask newFileChooserTask = new FileChooserTask(files);
        tasksContainer.setFileChooserTask(newFileChooserTask);

        Thread openFileChooserThread = new Thread(newFileChooserTask);
        tasksContainer.setOpenFileChooserThread(openFileChooserThread);
        openFileChooserThread.start();
    }

}
