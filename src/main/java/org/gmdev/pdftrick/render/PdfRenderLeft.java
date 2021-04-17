package org.gmdev.pdftrick.render;

import org.gmdev.pdftrick.manager.PdfTrickBag;
import org.gmdev.pdftrick.manager.TasksContainer;
import org.gmdev.pdftrick.tasks.*;

import com.itextpdf.text.pdf.PdfReader;

import java.io.File;

public class PdfRenderLeft {
	
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

		PdfCoverThumbnailsDisplayTask pdfCoverThumbnailsDisplayTask = new PdfCoverThumbnailsDisplayTask();
		tasksContainer.setPdfCoverThumbnailsDisplayTask(pdfCoverThumbnailsDisplayTask);
				
		Thread pdfCoverThumbnailsDisplayThread = new Thread(pdfCoverThumbnailsDisplayTask);
		tasksContainer.setPdfCoverThumbnailsDisplayThread(pdfCoverThumbnailsDisplayThread);
		pdfCoverThumbnailsDisplayThread.start();
	}

	
}
