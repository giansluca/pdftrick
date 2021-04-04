package org.gmdev.pdftrick.engine;

import org.gmdev.pdftrick.manager.*;
import org.gmdev.pdftrick.render.PdfRenderLeft;
import org.gmdev.pdftrick.swingmanager.*;
import org.gmdev.pdftrick.tasks.PageThumbnailsDisplayTask;
import org.gmdev.pdftrick.ui.panels.*;
import org.gmdev.pdftrick.utils.*;

import java.io.File;

import static org.gmdev.pdftrick.swingmanager.ModalWarningPanel.displayTooManyFilesLoadedAndThrow;

public interface FileIn {

    PdfTrickBag bag = PdfTrickBag.INSTANCE;

    default boolean beforeLoadingCheck() {
        LeftPanel leftPanel = bag.getUserInterface().getLeft();
        TasksContainer tasksContainer = bag.getTasksContainer();

        var showPdfCoverThumbnailsTask = tasksContainer.getPdfCoverThumbnailsDisplayTask();
        if (showPdfCoverThumbnailsTask != null && showPdfCoverThumbnailsTask.isRunning()) {
            leftPanel.resetLeftPanelFileDropBorder();
            ModalWarningPanel.displayLoadingPdfThumbnailsWarning();
            return false;
        }

        var imagesExtractionTask = tasksContainer.getImagesExtractionTask();
        if (imagesExtractionTask != null && imagesExtractionTask.isRunning()) {
            leftPanel.resetLeftPanelFileDropBorder();
            ModalWarningPanel.displayExtractingImagesWarning();
            return false;
        }

        PageThumbnailsDisplayTask pageThumbnailsDisplayTask = tasksContainer.getPageThumbnailsDisplayTask();
        if (pageThumbnailsDisplayTask != null && pageThumbnailsDisplayTask.isRunning()) {
            leftPanel.resetLeftPanelFileDropBorder();
            ModalWarningPanel.displayLoadingPageThumbnailImagesWarning();
            return false;
        }

        return true;
    }

    default void start(File[] filesArray) {
        if (filesArray.length > 1) {
            displayTooManyFilesLoadedAndThrow();
            return;
        }

        if (!beforeLoadingCheck()) return;
        prepareForLoading();

        File uploadedFile = filesArray[0];
        bag.setUploadedFile(uploadedFile);

        checkPdfFile();
        loadPdfFile(uploadedFile);
    }

    default void prepareForLoading() {
        SwingCleaner.cleanUserInterface();

        bag.cleanUp();
        FileUtils.deleteThumbnailFiles(bag.getThumbnailsFolderPath());
        FileUtils.deletePdfFile(bag.getPdfFilePath());
    }

    default void checkPdfFile() {
        String message = "check file failed!";
        FileChecker fileChecker = new FileChecker();

        if (!fileChecker.isValid()) {
            Messages.append("WARNING", message);
            throw new IllegalStateException(message);
        }
    }

    default void loadPdfFile(File uploadedFile) {
        CenterPanel centerPanel = bag.getUserInterface().getCenter();
        SwingInvoker.invokeLater(centerPanel::startWaitIconLoadPdf);

        FileDataManager fileDataManager = new FileDataManager();
        File outFile = fileDataManager.mergePdf(uploadedFile, bag.getPdfFilePath());

        if (outFile == null || !outFile.exists()) {
            String message = "Error checking pdf files!";
            Messages.append("WARNING", message);
            throw new IllegalStateException(message);
        }

        Messages.append("INFO", "Check Pdf OK, start loading pages");
        PdfRenderLeft render = new PdfRenderLeft();
        render.pdfRender();
    }

}
