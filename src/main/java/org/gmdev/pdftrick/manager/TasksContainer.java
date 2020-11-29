package org.gmdev.pdftrick.manager;

import java.util.concurrent.ExecutorService;

import org.gmdev.pdftrick.tasks.*;

public class TasksContainer {
	
	private DragAndDropTask dragAndDropTask = null;
	private FileChooserTask fileChooserTask = null;
	private PdfCoverThumbnailsDisplayTask pdfCoverThumbnailsDisplayTask = null;
	private FirstPdfPageRenderTask firstPdfPageRenderTask = null;
	private PageThumbnailsDisplayTask pageThumbnailsDisplayTask = null;
	private ImagesExtractionTask imagesExtractionTask = null;
	private CancelTask cancelTask = null;
	private ExecPool execPool = null;

	private ExecutorService executor = null;
	private Thread showThumbsThread = null;
	private Thread divisionThumbsThread = null;
	private Thread execPoolThread = null;
	private Thread OpenFileChooserThread = null;
	private Thread DragAnDropFileChooserThread = null;
	private Thread imgThumbThread = null;
	private Thread imgExtractionThread = null;
	private Thread cancelThread = null;

	public synchronized PdfCoverThumbnailsDisplayTask getPdfCoverThumbnailsDisplayTask() {
		return pdfCoverThumbnailsDisplayTask;
	}
	
	public synchronized void setPdfCoverThumbnailsDisplayTask(
			PdfCoverThumbnailsDisplayTask pdfCoverThumbnailsDisplayTask) {
		this.pdfCoverThumbnailsDisplayTask = pdfCoverThumbnailsDisplayTask;
	}

	public synchronized FileChooserTask getFileChooserTask() {
		return fileChooserTask;
	}

	public synchronized void setFileChooserTask(FileChooserTask fileChooserTask) {
		this.fileChooserTask = fileChooserTask;
	}

	public synchronized DragAndDropTask getDragAndDropTask() {
		return dragAndDropTask;
	}

	public synchronized void setDragAndDropTask(DragAndDropTask dragAndDropTask) {
		this.dragAndDropTask = dragAndDropTask;
	}
	
	public synchronized FirstPdfPageRenderTask getFirstPdfPageRenderTask() {
		return firstPdfPageRenderTask;
	}
	
	public synchronized void setFirstPdfPageRenderTask(FirstPdfPageRenderTask firstPdfPageRenderTask) {
		this.firstPdfPageRenderTask = firstPdfPageRenderTask;
	}
	
	public synchronized ExecPool getExecPool() {
		return execPool;
	}
	
	public synchronized void setExecPool(ExecPool execPool) {
		this.execPool = execPool;
	}
	
	public synchronized ExecutorService getExecutor() {
		return executor;
	}
	
	public synchronized void setExecutor(ExecutorService executor) {
		this.executor = executor;
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

	public synchronized Thread getShowThumbsThread() {
		return showThumbsThread;
	}
	
	public synchronized void setShowThumbsThread(Thread showThumbsThread) {
		this.showThumbsThread = showThumbsThread;
	}
	
	public synchronized Thread getDivisionThumbsThread() {
		return divisionThumbsThread;
	}

	public void setDivisionThumbsThread(Thread divisionThumbsThread) {
		this.divisionThumbsThread = divisionThumbsThread;
	}
	
	public synchronized Thread getExecPoolThread() {
		return execPoolThread;
	}
	
	public synchronized void setExecPoolThread(Thread execPoolThread) {
		this.execPoolThread = execPoolThread;
	}
	
	public synchronized Thread getOpenFileChooserThread() {
		return OpenFileChooserThread;
	}
	
	public synchronized void setOpenFileChooserThread(Thread openFileChooserThread) {
		OpenFileChooserThread = openFileChooserThread;
	}
	
	public synchronized Thread getDragAnDropFileChooserThread() {
		return DragAnDropFileChooserThread;
	}
	
	public synchronized void setDragAnDropFileChooserThread(Thread dragAnDropFileChooserThread) {
		DragAnDropFileChooserThread = dragAnDropFileChooserThread;
	}
	
	public  synchronized Thread getImgThumbThread() {
		return imgThumbThread;
	}

	public synchronized void setImgThumbThread(Thread imgThumbThread) {
		this.imgThumbThread = imgThumbThread;
	}
	
	public synchronized Thread getImgExtractionThread() {
		return imgExtractionThread;
	}
	
	public synchronized void setImgExtractionThread(Thread imgExtractionThread) {
		this.imgExtractionThread = imgExtractionThread;
	}
	
	public synchronized Thread getCancelThread() {
		return cancelThread;
	}
	
	public synchronized void setCancelThread(Thread cancelThread) {
		this.cancelThread = cancelThread;
	}

}
