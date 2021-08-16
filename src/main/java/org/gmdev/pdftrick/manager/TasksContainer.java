package org.gmdev.pdftrick.manager;

import java.util.concurrent.ExecutorService;

import org.gmdev.pdftrick.extraction.tasks.ImagesExtractionTask;
import org.gmdev.pdftrick.rendering.tasks.*;
import org.gmdev.pdftrick.tasks.*;

public class TasksContainer {

	// Tasks
	private PdfPageCoverRunnerTask pdfPageCoverRunnerTask = null;
	private PdfPageCoverDisplayTask pdfPageCoverDisplayTask = null;
	private PageThumbnailsDisplayTask pageThumbnailsDisplayTask = null;
	private ImagesExtractionTask imagesExtractionTask = null;
	private CancelTask cancelTask = null;

	// Treads
	private ExecutorService executorService = null;
	private Thread pdfPageRunnerThread = null;
	private Thread pdfPageDisplayThread = null;
	private Thread pageThumbnailsDisplayThread = null;
	private Thread imagesExtractionThread = null;
	private Thread cancelThread = null;

	// Tasks
	public PdfPageCoverRunnerTask getPdfPageRunnerTask() {
		return pdfPageCoverRunnerTask;
	}

	public void setPdfPageRunnerTask(PdfPageCoverRunnerTask pdfPageCoverRunnerTask) {
		this.pdfPageCoverRunnerTask = pdfPageCoverRunnerTask;
	}

	public PdfPageCoverDisplayTask getPdfPageDisplayTask() {
		return pdfPageCoverDisplayTask;
	}

	public void setPdfPageDisplayTask(PdfPageCoverDisplayTask pdfPageCoverDisplayTask) {
		this.pdfPageCoverDisplayTask = pdfPageCoverDisplayTask;
	}

	public PageThumbnailsDisplayTask getPageThumbnailsDisplayTask() {
		return pageThumbnailsDisplayTask;
	}
	
	public void setPageThumbnailsDisplayTask(PageThumbnailsDisplayTask pageThumbnailsDisplayTask) {
		this.pageThumbnailsDisplayTask = pageThumbnailsDisplayTask;
	}
	
	public ImagesExtractionTask getImagesExtractionTask() {
		return imagesExtractionTask;
	}
	
	public void setImagesExtractionTask(ImagesExtractionTask imagesExtractionTask) {
		this.imagesExtractionTask = imagesExtractionTask;
	}
	
	public CancelTask getCancelTask() {
		return cancelTask;
	}
	
	public void setCancelTask(CancelTask cancelTask) {
		this.cancelTask = cancelTask;
	}

	// Threads
	public ExecutorService getExecutorService() {
		return executorService;
	}

	public void setExecutorService(ExecutorService executorService) {
		this.executorService = executorService;
	}

	public Thread getPdfPageRunnerThread() {
		return pdfPageRunnerThread;
	}

	public void setPdfPageRunnerThread(Thread pdfPageRunnerThread) {
		this.pdfPageRunnerThread = pdfPageRunnerThread;
	}

	public Thread getPdfPageDisplayThread() {
		return pdfPageDisplayThread;
	}

	public void setPdfPageDisplayThread(Thread pdfPageDisplayThread) {
		this.pdfPageDisplayThread = pdfPageDisplayThread;
	}

	public Thread getPageThumbnailsDisplayThread() {
		return pageThumbnailsDisplayThread;
	}

	public void setPageThumbnailsDisplayThread(Thread pageThumbnailsDisplayThread) {
		this.pageThumbnailsDisplayThread = pageThumbnailsDisplayThread;
	}

	public Thread getImagesExtractionThread() {
		return imagesExtractionThread;
	}

	public void setImagesExtractionThread(Thread imagesExtractionThread) {
		this.imagesExtractionThread = imagesExtractionThread;
	}

	public Thread getCancelThread() {
		return cancelThread;
	}
	
	public void setCancelThread(Thread cancelThread) {
		this.cancelThread = cancelThread;
	}



}
