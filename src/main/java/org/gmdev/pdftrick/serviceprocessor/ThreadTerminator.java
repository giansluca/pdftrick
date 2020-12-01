package org.gmdev.pdftrick.serviceprocessor;

import org.gmdev.pdftrick.manager.PdfTrickBag;
import org.gmdev.pdftrick.manager.TasksContainer;

public class ThreadTerminator {

    private static final TasksContainer tasksContainer = PdfTrickBag.INSTANCE.getTasksContainer();

    public  static void joinDragAndDropThread() {
        waitForThread(tasksContainer.getDragAndDropThread());
    }

    public static void joinFileChooserThread() {
        waitForThread(tasksContainer.getFileChooserThread());
    }

    public static void joinFirstPdfPageRenderThread() {
        waitForThread(tasksContainer.getFirstPdfPageRenderThread());
    }

    public static void joinPdfCoverThumbnailsDisplayTask() {
        waitForThread(tasksContainer.getPdfCoverThumbnailsDisplayThread());
    }


    public static void joinCancelThread() {
        waitForThread(tasksContainer.getCancelThread());
    }

    private static void waitForThread(Thread thread) {
        try {
            thread.join();
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }
}
