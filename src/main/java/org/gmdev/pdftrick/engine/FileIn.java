package org.gmdev.pdftrick.engine;

import org.gmdev.pdftrick.manager.*;
import org.gmdev.pdftrick.render.PdfRenderLeft;
import org.gmdev.pdftrick.swingmanager.*;
import org.gmdev.pdftrick.tasks.PageThumbnailsDisplayTask;
import org.gmdev.pdftrick.ui.panels.*;
import org.gmdev.pdftrick.utils.FileUtils;
import org.gmdev.pdftrick.utils.Messages;

import java.io.File;
import java.util.ArrayList;
import java.util.Properties;

import static org.gmdev.pdftrick.utils.Messages.MessageLevel.WARNING;

public interface FileIn {

    PdfTrickBag bag = PdfTrickBag.INSTANCE;

    default void beforeLoadingCheck() {
        LeftPanel leftPanel = bag.getUserInterface().getLeft();
        Properties messages = bag.getMessagesProps();
        TasksContainer tasksContainer = bag.getTasksContainer();

        var dragAndDropTask = tasksContainer.getDragAndDropTask();
        var fileChooserTask = tasksContainer.getFileChooserTask();
        if ((dragAndDropTask != null && dragAndDropTask.isRunning()) ||
                (fileChooserTask != null && fileChooserTask.isRunning())) {

            leftPanel.resetLeftPanelFileDropBorder();
            Messages.append(WARNING.name(), messages.getProperty("t_msg_01"));
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

    default void prepareForLoading() {
        CenterPanel centerPanel = bag.getUserInterface().getCenter();

        SwingCleaner.cleanUserInterface();
        SwingInvoker.invokeLater(centerPanel::startWaitIconLoadPdf);

        bag.cleanUp();
        FileUtils.deleteThumbnailFiles(bag.getThumbnailsFolderPath());
        FileUtils.deletePdfFile(bag.getPdfFilePath());
    }

    default void checkAndPdfFile() {
        String message = "check file failed!";
        FileChecker fileChecker = new FileChecker();

        if (!fileChecker.isValid()) {
            SwingCleaner.cleanPanels();
            Messages.append("WARNING", message);
            throw new IllegalStateException(message);
        }
    }

    default void loadPdfFile(ArrayList<File> filesArray) {
        FileDataManager fileDataManager = new FileDataManager();
        File pdfFile = fileDataManager.mergePdf(filesArray, bag.getPdfFilePath());

        if (pdfFile == null || !pdfFile.exists() || pdfFile.length() <= 0) {
            String message = "Error analyzing pdf files!";
            Messages.append("WARNING", message);
            throw new IllegalStateException(message);
        }

        Messages.append("INFO", "Check Pdf OK, start loading pages");
        PdfRenderLeft render = new PdfRenderLeft();
        render.pdfRender();
    }




}
