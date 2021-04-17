package org.gmdev.pdftrick.engine;

import com.itextpdf.text.pdf.PdfReader;
import org.gmdev.pdftrick.manager.*;
import org.gmdev.pdftrick.render.PdfRenderLeft;
import org.gmdev.pdftrick.swingmanager.*;
import org.gmdev.pdftrick.tasks.PageThumbnailsDisplayTask;
import org.gmdev.pdftrick.ui.panels.*;
import org.gmdev.pdftrick.utils.*;

import java.io.File;
import java.io.IOException;

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
        if(!isValidPdfFile(uploadedFile)) return;

        bag.setUploadedFile(uploadedFile);
        savePdfFile(uploadedFile);
        renderPdfPages();
    }

    private void prepareForLoading() {
        SwingCleaner.cleanUserInterface();

        bag.cleanUp();
        FileUtils.deleteThumbnailFiles(bag.getThumbnailsFolderPath());
        FileUtils.deletePdfFile(bag.getSavedFilePath());
    }

    private boolean isValidPdfFile(File uploadedFile) {
        FileChecker fileChecker = new FileChecker(uploadedFile);
        return fileChecker.isValid();
    }

    private void savePdfFile(File uploadedFile) {
        CenterPanel centerPanel = bag.getUserInterface().getCenter();
        SwingInvoker.invokeLater(centerPanel::startWaitIconLoadPdf);

        PdfFileTransformer transformer = new PdfFileTransformer();
        File savedFile = transformer.saveUploadedFile(uploadedFile);

        if (savedFile == null || !savedFile.exists()) {
            String message = "Error saving pdf file.";
            Messages.append("ERROR", message);
            throw new IllegalStateException(message);
        }

        try {
            PdfReader reader = new PdfReader(bag.getSavedFilePath().toString());
            bag.setNumberOfPages(reader.getNumberOfPages());
            reader.close();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private void renderPdfPages() {
        Messages.append("INFO", "Check Pdf OK, start loading pages");
        PdfRenderLeft render = new PdfRenderLeft();
        render.pdfRender();
    }

}
