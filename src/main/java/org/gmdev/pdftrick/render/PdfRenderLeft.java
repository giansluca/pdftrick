package org.gmdev.pdftrick.render;

import org.gmdev.pdftrick.manager.PdfTrickBag;
import org.gmdev.pdftrick.manager.TasksContainer;
import org.gmdev.pdftrick.tasks.*;

import com.itextpdf.text.pdf.PdfReader;

import java.io.File;

public class PdfRenderLeft {
	
	private static final PdfTrickBag bag = PdfTrickBag.INSTANCE;
	
	public PdfRenderLeft() {
	}

	public void pdfRender() {
		TasksContainer tasksContainer = bag.getTasksContainer();
		String imagesFolderPath = bag.getThumbnailsFolderPath() + File.separator;

		int pages;
		try {
			PdfReader reader = new PdfReader(bag.getPdfFilePath().toString());
			pages = reader.getNumberOfPages();
			bag.setNumberOfPages(pages);
			reader.close();
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}

		boolean runPool = true;
		int divisionResult = pages / 3;
		
		if (pages < 3) {
			runPool = false;
			divisionResult = pages;
		}
		
		FirstPdfPageRenderTask firstPdfPageRenderTask =
				new FirstPdfPageRenderTask(divisionResult, imagesFolderPath);
		tasksContainer.setFirstPdfPageRenderTask(firstPdfPageRenderTask);
		
		Thread firstPdfPageRenderThread = new Thread(firstPdfPageRenderTask);
		tasksContainer.setFirstPdfPageRenderThread(firstPdfPageRenderThread);
		firstPdfPageRenderThread.start();
		
		if (runPool) {
			ExecutorRunnerTask executorRunnerTask =
					new ExecutorRunnerTask(pages, divisionResult, imagesFolderPath);
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
