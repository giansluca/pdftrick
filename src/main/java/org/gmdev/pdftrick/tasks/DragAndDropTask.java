package org.gmdev.pdftrick.tasks;

import java.io.File;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.*;

import org.gmdev.pdftrick.engine.*;
import org.gmdev.pdftrick.manager.PdfTrickBag;
import org.gmdev.pdftrick.render.PdfRenderLeft;
import org.gmdev.pdftrick.serviceprocessor.Stoppable;
import org.gmdev.pdftrick.ui.panels.*;
import org.gmdev.pdftrick.utils.*;

public class DragAndDropTask implements Runnable, Stoppable {

    private static final PdfTrickBag BAG = PdfTrickBag.INSTANCE;

    private final File[] droppedFiles;
    private final AtomicBoolean running = new AtomicBoolean(false);

    public DragAndDropTask(File[] droppedFiles) {
        this.droppedFiles = droppedFiles;
    }

    public void stop() {
        running.set(false);
    }

    public boolean isRunning() {
        return running.get();
    }

    @Override
    public void run() {
        running.set(true);

        JTextField currentPageField = BAG.getUserInterface().getRight().getCurrentPageField();
        JTextField numImgSelectedField = BAG.getUserInterface().getRight().getSelectedImagesField();
        CenterPanel centerPanel = BAG.getUserInterface().getCenter();
        LeftPanel leftPanel = BAG.getUserInterface().getLeft();
        BottomPanel bottomPanel = BAG.getUserInterface().getBottom();
        ArrayList<File> filesArray = BAG.getPdfFilesArray();
        Properties messages = BAG.getMessagesProps();

        SwingUtilities.invokeLater(() -> {
            leftPanel.clean();
            centerPanel.clean();
            bottomPanel.clean();
            currentPageField.setText("");
            numImgSelectedField.setText("");
            centerPanel.startWaitIconLoadPdf();
        });

        // clean up
        BAG.setSelectedPage(0);
        BAG.setExtractionFolderPath(null);
        BAG.cleanPdfFilesArray();
        BAG.cleanSelectedImagesHashMap();
        BAG.cleanInlineSelectedImagesHashMap();
        BAG.cleanPagesRotationHashMap();

        FileUtils.deleteThumbnailFiles(BAG.getThumbnailsFolderPath());
        FileUtils.deletePdfFile(BAG.getPdfFilePath());

        for (File item : droppedFiles)
            if (!item.isDirectory())
                filesArray.add(item);

        boolean fileCheck;
        CheckFiles checkFiles = new CheckFiles();

        if (filesArray.size() > 0) {
            fileCheck = checkFiles.check();

            if (!fileCheck) {
                // in case of check failed i clean panel left and center, other stuff
                // (vector, hasmap, resultpdf was cleaned on approve open filechooser)
                Messages.append("WARNING", messages.getProperty("tmsg_11"));
                SwingUtilities.invokeLater(() -> {
                    leftPanel.clean();
                    centerPanel.clean();
                });
            } else {
                // merge pdf selection after check
                MergeFiles mergeFiles = new MergeFiles();
                File resultFile = mergeFiles.mergePdf(filesArray, BAG.getPdfFilePath());

                if (resultFile != null && resultFile.exists() && resultFile.length() > 0) {
                    Messages.append("INFO", messages.getProperty("tmsg_12"));
                    PdfRenderLeft render = new PdfRenderLeft();
                    render.pdfRender();
                } else {
                    Messages.append("WARNING", messages.getProperty("tmsg_13"));
                }
            }
        } else {
            SwingUtilities.invokeLater(() -> {
                BAG.getUserInterface().getCenter().stopWaitIcon();
                centerPanel.clean();
            });
            Messages.append("WARNING", messages.getProperty("tmsg_14"));
        }

        running.set(false);
    }


}
