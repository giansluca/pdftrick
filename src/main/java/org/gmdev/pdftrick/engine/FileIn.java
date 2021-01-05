package org.gmdev.pdftrick.engine;

import org.gmdev.pdftrick.manager.*;
import org.gmdev.pdftrick.render.PdfRenderLeft;
import org.gmdev.pdftrick.swingmanager.*;
import org.gmdev.pdftrick.tasks.PageThumbnailsDisplayTask;
import org.gmdev.pdftrick.ui.panels.*;
import org.gmdev.pdftrick.utils.*;

import java.io.File;
import java.util.*;

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
            displayTooManyFilesLoadedAndThrow(); return;
        }

        if (!beforeLoadingCheck()) return;
        prepareForLoading();

        ArrayList<File> files = bag.getPdfFilesArray();
        files.add(filesArray[0]);

        checkAndPdfFile();
        loadPdfFile(files);
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
