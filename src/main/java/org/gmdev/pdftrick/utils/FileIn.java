package org.gmdev.pdftrick.utils;

import org.gmdev.pdftrick.engine.FileChecker;
import org.gmdev.pdftrick.manager.*;
import org.gmdev.pdftrick.swingmanager.*;
import org.gmdev.pdftrick.tasks.PageThumbnailsDisplayTask;
import org.gmdev.pdftrick.ui.panels.*;

import java.io.File;
import java.util.Properties;

import static org.gmdev.pdftrick.utils.Messages.MessageLevel.WARNING;

public interface FileIn {

    default void beforeLoadingCheck(PdfTrickBag bag) {
        LeftPanel leftPanel = bag.getUserInterface().getLeft();
        Properties messagesProps = bag.getMessagesProps();
        TasksContainer tasksContainer = bag.getTasksContainer();

        var dragAndDropTask = tasksContainer.getDragAndDropTask();
        var fileChooserTask = tasksContainer.getFileChooserTask();
        if ((dragAndDropTask != null && dragAndDropTask.isRunning()) ||
                (fileChooserTask != null && fileChooserTask.isRunning())) {

            leftPanel.resetLeftPanelFileDropBorder();
            Messages.append(WARNING.name(), messagesProps.getProperty("t_msg_01"));
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
        }
    }

    default void prepareForLoading(PdfTrickBag bag) {
        CenterPanel centerPanel = bag.getUserInterface().getCenter();

        SwingCleaner.cleanUserInterface();
        SwingInvoker.invokeLater(centerPanel::startWaitIconLoadPdf);

        bag.cleanUp();
        FileUtils.deleteThumbnailFiles(bag.getThumbnailsFolderPath());
        FileUtils.deletePdfFile(bag.getPdfFilePath());
    }

    default void checkAndLoadPdfFile(File pdfFile) {
        FileChecker fileChecker = new FileChecker();
    }


}
