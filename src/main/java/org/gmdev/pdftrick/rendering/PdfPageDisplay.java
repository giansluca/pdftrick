package org.gmdev.pdftrick.rendering;

import org.gmdev.pdftrick.manager.PdfTrickBag;
import org.gmdev.pdftrick.manager.TasksContainer;
import org.gmdev.pdftrick.rendering.tasks.PdfPageRunnerTask;
import org.gmdev.pdftrick.rendering.tasks.PdfPageDisplayTask;

import java.io.File;

public class PdfPageDisplay {

    private static final PdfTrickBag bag = PdfTrickBag.INSTANCE;

    public void render() {
        TasksContainer tasksContainer = bag.getTasksContainer();
        String imagesFolderPath = bag.getThumbnailsFolderPath() + File.separator;

        PdfPageRunnerTask pdfPageRunnerTask =
                new PdfPageRunnerTask(bag.getNumberOfPages(), imagesFolderPath);
        tasksContainer.setPdfPageRunnerTask(pdfPageRunnerTask);

        Thread pdfPageRunnerThread = new Thread(pdfPageRunnerTask);
        tasksContainer.setPdfPageRunnerThread(pdfPageRunnerThread);
		pdfPageRunnerThread.start();

        PdfPageDisplayTask pdfPageDisplayTask = new PdfPageDisplayTask();
        tasksContainer.setPdfPageDisplayTask(pdfPageDisplayTask);

        Thread pdfPageDisplayThread = new Thread(pdfPageDisplayTask);
        tasksContainer.setPdfPageDisplayThread(pdfPageDisplayThread);
		pdfPageDisplayThread.start();
    }


}
