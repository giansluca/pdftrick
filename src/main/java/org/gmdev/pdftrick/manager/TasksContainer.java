package org.gmdev.pdftrick.manager;

import java.util.concurrent.ExecutorService;

import org.gmdev.pdftrick.extraction.tasks.ImagesExtractionTask;
import org.gmdev.pdftrick.rendering.tasks.ExecutorRunnerTask;
import org.gmdev.pdftrick.rendering.tasks.FirstPdfPageRenderTask;
import org.gmdev.pdftrick.rendering.tasks.PageThumbnailsDisplayTask;
import org.gmdev.pdftrick.rendering.tasks.PdfPageDisplayTask;
import org.gmdev.pdftrick.tasks.*;

public class TasksContainer {

	// Tasks
	private PdfPageDisplayTask pdfPageDisplayTask = null;
	private FirstPdfPageRenderTask firstPdfPageRenderTask = null;
	private PageThumbnailsDisplayTask pageThumbnailsDisplayTask = null;
	private ImagesExtractionTask imagesExtractionTask = null;
	private CancelTask cancelTask = null;
	private ExecutorRunnerTask executorRunnerTask = null;

	// Treads
	private Thread pdfCoverThumbnailsDisplayThread = null;
	private Thread firstPdfPageRenderThread = null;
	private Thread pageThumbnailsDisplayThread = null;
	private Thread imagesExtractionThread = null;
	private Thread cancelThread = null;
	private Thread executorRunnerThread = null;
	private ExecutorService executorService = null;

	public synchronized PdfPageDisplayTask getPdfCoverThumbnailsDisplayTask() {
		return pdfPageDisplayTask;
	}

	public synchronized void setPdfCoverThumbnailsDisplayTask(
			PdfPageDisplayTask pdfPageDisplayTask) {
		this.pdfPageDisplayTask = pdfPageDisplayTask;
	}

	public synchronized FirstPdfPageRenderTask getFirstPdfPageRenderTask() {
		return firstPdfPageRenderTask;
	}
	
	public synchronized void setFirstPdfPageRenderTask(FirstPdfPageRenderTask firstPdfPageRenderTask) {
		this.firstPdfPageRenderTask = firstPdfPageRenderTask;
	}

	public ExecutorRunnerTask getExecutorRunnerTask() {
		return executorRunnerTask;
	}

	public void setExecutorRunnerTask(ExecutorRunnerTask executorRunnerTask) {
		this.executorRunnerTask = executorRunnerTask;
	}

	public synchronized PageThumbnailsDisplayTask getPageThumbnailsDisplayTask() {
		return pageThumbnailsDisplayTask;
	}
	
	public synchronized void setPageThumbnailsDisplayTask(PageThumbnailsDisplayTask pageThumbnailsDisplayTask) {
		this.pageThumbnailsDisplayTask = pageThumbnailsDisplayTask;
	}
	
	public synchronized ImagesExtractionTask getImagesExtractionTask() {
		return imagesExtractionTask;
	}
	
	public synchronized void setImagesExtractionTask(ImagesExtractionTask imagesExtractionTask) {
		this.imagesExtractionTask = imagesExtractionTask;
	}
	
	public synchronized CancelTask getCancelTask() {
		return cancelTask;
	}
	
	public synchronized void setCancelTask(CancelTask cancelTask) {
		this.cancelTask = cancelTask;
	}

	public Thread getPdfCoverThumbnailsDisplayThread() {
		return pdfCoverThumbnailsDisplayThread;
	}

	public void setPdfCoverThumbnailsDisplayThread(Thread pdfCoverThumbnailsDisplayThread) {
		this.pdfCoverThumbnailsDisplayThread = pdfCoverThumbnailsDisplayThread;
	}

	public Thread getFirstPdfPageRenderThread() {
		return firstPdfPageRenderThread;
	}

	public void setFirstPdfPageRenderThread(Thread firstPdfPageRenderThread) {
		this.firstPdfPageRenderThread = firstPdfPageRenderThread;
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

	public synchronized Thread getCancelThread() {
		return cancelThread;
	}
	
	public synchronized void setCancelThread(Thread cancelThread) {
		this.cancelThread = cancelThread;
	}

	public Thread getExecutorRunnerThread() {
		return executorRunnerThread;
	}

	public void setExecutorRunnerThread(Thread executorRunnerThread) {
		this.executorRunnerThread = executorRunnerThread;
	}

	public ExecutorService getExecutorService() {
		return executorService;
	}

	public void setExecutorService(ExecutorService executorService) {
		this.executorService = executorService;
	}


}
