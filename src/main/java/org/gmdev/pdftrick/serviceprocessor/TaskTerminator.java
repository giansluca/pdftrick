package org.gmdev.pdftrick.serviceprocessor;

import org.gmdev.pdftrick.manager.*;

import java.util.concurrent.*;

public class TaskTerminator {

    private static final TasksContainer tasksContainer = PdfTrickBag.INSTANCE.getTasksContainer();

    public static void terminateExecutorRunnerTask() {
        stopTask(tasksContainer.getPdfPageRunnerTask());
        waitForThread(tasksContainer.getPdfPageRunnerThread());
        shutdownExecutorService(tasksContainer.getExecutorService());
    }

    public static void terminatePdfCoverThumbnailsDisplayTask() {
        stopTask(tasksContainer.getPdfPageDisplayTask());
        waitForThread(tasksContainer.getPdfPageDisplayThread());
    }

    public static void terminateImagesExtractionTask() {
        stopTask(tasksContainer.getImagesExtractionTask());
        waitForThread(tasksContainer.getImagesExtractionThread());
    }

    private static void stopTask(Stoppable task) {
        if (task == null) return;
        task.stop();
    }

    private static void waitForThread(Thread thread) {
        if (thread == null) return;
        try {
            thread.join();
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }

    private static void shutdownExecutorService(ExecutorService executorService) {
        if (executorService == null) return;
        executorService.shutdownNow();
        try {
            if(!executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS))
                throw(new IllegalStateException("Error awaiting Executor service termination"));
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }


}
