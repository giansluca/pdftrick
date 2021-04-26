package org.gmdev.pdftrick.rendering;

import org.gmdev.pdftrick.manager.PdfTrickBag;
import org.gmdev.pdftrick.manager.TasksContainer;
import org.gmdev.pdftrick.rendering.tasks.ExecutorRunnerTask;
import org.gmdev.pdftrick.rendering.tasks.FirstPdfPageRenderTask;
import org.gmdev.pdftrick.rendering.tasks.PdfPageDisplayTask;

import java.io.File;

public class PdfPageDisplay {
	
	private static final PdfTrickBag bag = PdfTrickBag.INSTANCE;

	public void pdfRender() {
		TasksContainer tasksContainer = bag.getTasksContainer();
		String imagesFolderPath = bag.getThumbnailsFolderPath() + File.separator;
		int numberOfPages = bag.getNumberOfPages();

		boolean runPool = true;
		int divisionResult = numberOfPages / 3;
		
		if (numberOfPages < 3) {
			runPool = false;
			divisionResult = numberOfPages;
		}
		
		FirstPdfPageRenderTask firstPdfPageRenderTask =
				new FirstPdfPageRenderTask(divisionResult, imagesFolderPath);
		tasksContainer.setFirstPdfPageRenderTask(firstPdfPageRenderTask);
		
		Thread firstPdfPageRenderThread = new Thread(firstPdfPageRenderTask);
		tasksContainer.setFirstPdfPageRenderThread(firstPdfPageRenderThread);
		firstPdfPageRenderThread.start();
		
		if (runPool) {
			ExecutorRunnerTask executorRunnerTask =
					new ExecutorRunnerTask(numberOfPages, divisionResult, imagesFolderPath);
			tasksContainer.setExecutorRunnerTask(executorRunnerTask);
			
			Thread executorRunnerThread = new Thread(executorRunnerTask);
			tasksContainer.setExecutorRunnerThread(executorRunnerThread);
			executorRunnerThread.start();
		}

		PdfPageDisplayTask pdfPageDisplayTask = new PdfPageDisplayTask();
		tasksContainer.setPdfCoverThumbnailsDisplayTask(pdfPageDisplayTask);
				
		Thread pdfCoverThumbnailsDisplayThread = new Thread(pdfPageDisplayTask);
		tasksContainer.setPdfCoverThumbnailsDisplayThread(pdfCoverThumbnailsDisplayThread);
		pdfCoverThumbnailsDisplayThread.start();
	}

	
}
