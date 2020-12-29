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

public class DragAndDropTask implements Runnable, Stoppable, FileIn {

    private static final PdfTrickBag BAG = PdfTrickBag.INSTANCE;

    private final File[] droppedFiles;
    private final AtomicBoolean running = new AtomicBoolean(false);

    public DragAndDropTask(File[] droppedFiles) {
        if (droppedFiles.length > 1)
            throw new IllegalStateException("only one file is allowed for loading");
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

        LeftPanel leftPanel = BAG.getUserInterface().getLeft();
        CenterPanel centerPanel = BAG.getUserInterface().getCenter();
        Properties messages = BAG.getMessagesProps();

        prepareForLoading(BAG);

        ArrayList<File> filesArray = BAG.getPdfFilesArray();
        filesArray.addAll(List.of(droppedFiles));

        checkAndLoadPdfFile(filesArray.get(0));

        boolean fileCheck;
        FileChecker fileChecker = new FileChecker();

        if (filesArray.size() > 0) {
            fileCheck = fileChecker.isValid();

            if (!fileCheck) {
                // in case of isValid failed i clean panel left and center, other stuff
                // (vector, hasmap, resultpdf was cleaned on approve open filechooser)
                Messages.append("WARNING", messages.getProperty("tmsg_11"));
                SwingUtilities.invokeLater(() -> {
                    leftPanel.clean();
                    centerPanel.clean();
                });
            } else {
                // merge pdf selection after isValid
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
