package org.gmdev.pdftrick.rendering;

import org.gmdev.pdftrick.manager.PdfTrickBag;
import org.gmdev.pdftrick.manager.TasksContainer;
import org.gmdev.pdftrick.rendering.tasks.PdfPageCoverRunnerTask;
import org.gmdev.pdftrick.rendering.tasks.PdfPageCoverDisplayTask;

import java.io.File;

public class PdfPageRendering {

    private static final PdfTrickBag bag = PdfTrickBag.INSTANCE;

    public void render() {
        TasksContainer tasksContainer = bag.getTasksContainer();
        String imagesFolderPath = bag.getThumbnailsFolderPath() + File.separator;

        PdfPageCoverRunnerTask pdfPageCoverRunnerTask =
                new PdfPageCoverRunnerTask(bag.getNumberOfPages(), imagesFolderPath);
        tasksContainer.setPdfPageRunnerTask(pdfPageCoverRunnerTask);

        Thread pdfPageRunnerThread = new Thread(pdfPageCoverRunnerTask);
        tasksContainer.setPdfPageRunnerThread(pdfPageRunnerThread);
		pdfPageRunnerThread.start();

        PdfPageCoverDisplayTask pdfPageCoverDisplayTask = new PdfPageCoverDisplayTask();
        tasksContainer.setPdfPageDisplayTask(pdfPageCoverDisplayTask);

        Thread pdfPageDisplayThread = new Thread(pdfPageCoverDisplayTask);
        tasksContainer.setPdfPageDisplayThread(pdfPageDisplayThread);
		pdfPageDisplayThread.start();
    }


}
