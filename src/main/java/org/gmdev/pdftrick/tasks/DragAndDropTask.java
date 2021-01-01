package org.gmdev.pdftrick.tasks;

import java.io.File;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import org.gmdev.pdftrick.engine.*;
import org.gmdev.pdftrick.manager.PdfTrickBag;
import org.gmdev.pdftrick.serviceprocessor.Stoppable;

import static org.gmdev.pdftrick.swingmanager.ModalWarningPanel.displayTooManyFilesLoadedAndThrow;

public class DragAndDropTask implements Runnable, Stoppable, FileIn {

    private static final PdfTrickBag bag = PdfTrickBag.INSTANCE;

    private final File[] droppedFiles;
    private final AtomicBoolean running = new AtomicBoolean(false);

    public DragAndDropTask(File[] droppedFiles) {
        if (droppedFiles.length > 1)
            displayTooManyFilesLoadedAndThrow();

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
        prepareForLoading();

        ArrayList<File> filesArray = bag.getPdfFilesArray();
        filesArray.add(droppedFiles[0]);

        checkAndPdfFile();
        loadPdfFile(filesArray);

        running.set(false);
    }


}
