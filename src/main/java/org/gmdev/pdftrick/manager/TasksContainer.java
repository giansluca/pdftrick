package org.gmdev.pdftrick.manager;

import java.util.concurrent.ExecutorService;

import org.gmdev.pdftrick.tasks.*;

public class TasksContainer {

	// Tasks
	private DragAndDropTask dragAndDropTask = null;
	private FileChooserTask fileChooserTask = null;
	private PdfCoverThumbnailsDisplayTask pdfCoverThumbnailsDisplayTask = null;
	private FirstPdfPageRenderTask firstPdfPageRenderTask = null;
	private PageThumbnailsDisplayTask pageThumbnailsDisplayTask = null;
	private ImagesExtractionTask imagesExtractionTask = null;
	private CancelTask cancelTask = null;
	private ExecutorRunnerTask executorRunnerTask = null;

	// Treads
	private Thread dragAndDropThread = null;
	private Thread fileChooserThread = null;
	private Thread pdfCoverThumbnailsDisplayThread = null;
	private Thread firstPdfPageRenderThread = null;
	private Thread pageThumbnailsDisplayThread = null;
	private Thread imagesExtractionThread = null;
	private Thread cancelThread = null;
	private Thread executorRunnerThread = null;
	private ExecutorService executorService = null;

	public synchronized DragAndDropTask getDragAndDropTask() {
		return dragAndDropTask;
	}

	public synchronized void setDragAndDropTask(DragAndDropTask dragAndDropTask) {
		this.dragAndDropTask = dragAndDropTask;
	}

	public synchronized FileChooserTask getFileChooserTask() {
		return fileChooserTask;
	}

	public synchronized void setFileChooserTask(FileChooserTask fileChooserTask) {
		this.fileChooserTask = fileChooserTask;
	}

	public synchronized PdfCoverThumbnailsDisplayTask getPdfCoverThumbnailsDisplayTask() {
		return pdfCoverThumbnailsDisplayTask;
	}

	public synchronized void setPdfCoverThumbnailsDisplayTask(
			PdfCoverThumbnailsDisplayTask pdfCoverThumbnailsDisplayTask) {
		this.pdfCoverThumbnailsDisplayTask = pdfCoverThumbnailsDisplayTask;
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

	public Thread getDragAndDropThread() {
		return dragAndDropThread;
	}

	public void setDragAndDropThread(Thread dragAndDropThread) {
		this.dragAndDropThread = dragAndDropThread;
	}

	public Thread getFileChooserThread() {
		return fileChooserThread;
	}

	public void setFileChooserThread(Thread fileChooserThread) {
		this.fileChooserThread = fileChooserThread;
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
