package org.gmdev.pdftrick.serviceprocessor;

import org.gmdev.pdftrick.manager.PdfTrickBag;
import org.gmdev.pdftrick.manager.TasksContainer;

import java.util.concurrent.ExecutorService;

public class TaskTerminator {

    private static final TasksContainer tasksContainer = PdfTrickBag.INSTANCE.getTasksContainer();

    public static void terminateFileChooserTask() {
        stopTask(tasksContainer.getFileChooserTask());
        waitForThread(tasksContainer.getFileChooserThread());
    }

    public  static void terminateDragAndDropTask() {
        stopTask(tasksContainer.getDragAndDropTask());
        waitForThread(tasksContainer.getDragAndDropThread());
    }

    public static void terminateFirstPdfPageRenderTask() {
        stopTask(tasksContainer.getFirstPdfPageRenderTask());
        waitForThread(tasksContainer.getFirstPdfPageRenderThread());
    }

    public static void terminateExecutorRunnerTask() {
        stopTask(tasksContainer.getExecutorRunnerTask());
        waitForThread(tasksContainer.getExecutorRunnerThread());
        shutdownExecutorService(tasksContainer.getExecutorService());
    }

    public static void terminatePdfCoverThumbnailsDisplayTask() {
        stopTask(tasksContainer.getPdfCoverThumbnailsDisplayTask());
        waitForThread(tasksContainer.getPdfCoverThumbnailsDisplayThread());
    }

    public static void terminateCancelThread() {
        stopTask(tasksContainer.getCancelTask());
        waitForThread(tasksContainer.getCancelThread());
    }

    private static void stopTask(Stoppable task) {
        if (task == null) return;
        task.stop();
    }

    private static void waitForThread(Thread thread) {
        if (thread == null) return;
        try {
            thread.join();
            while (thread.isAlive())
                System.out.println("still alive");
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }

    private static void shutdownExecutorService(ExecutorService executorService) {
        if (executorService == null) return;
        executorService.shutdown();
    }


}
