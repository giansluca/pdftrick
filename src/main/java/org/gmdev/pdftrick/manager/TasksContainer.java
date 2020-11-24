package org.gmdev.pdftrick.manager;

import java.util.concurrent.ExecutorService;

import org.gmdev.pdftrick.thread.*;

public class TasksContainer {
	
	private volatile DragAndDropTask dragAndDropTask = null;
	private volatile FileChooserTask fileChooserTask = null;

	private volatile ShowPdfCoverThumbnailsTask showPdfCoverThumbnailsTask = null;
	private volatile DivisionThumb divisionThumbs = null;
	private volatile ExecPool execPool = null;
	private volatile ExecutorService executor = null;
	private volatile ImgThumb imgThumb = null;
	private volatile ImgExtraction imgExtraction = null;
	private volatile Cancel cancel = null;
	
	private volatile Thread showThumbsThread = null;
	private volatile Thread divisionThumbsThread = null;
	private volatile Thread execPoolThread = null;
	private volatile Thread OpenFileChooserThread = null;
	private volatile Thread DragAnDropFileChooserThread = null;
	private volatile Thread imgThumbThread = null;
	private volatile Thread imgExtractionThread = null;
	private volatile Thread cancelThread = null;

	public synchronized ShowPdfCoverThumbnailsTask getShowPdfCoverThumbnailsTask() {
		return showPdfCoverThumbnailsTask;
	}
	
	public synchronized void setShowPdfCoverThumbnailsTask(
			ShowPdfCoverThumbnailsTask showPdfCoverThumbnailsTask) {
		this.showPdfCoverThumbnailsTask = showPdfCoverThumbnailsTask;
	}

	public FileChooserTask getFileChooserTask() {
		return fileChooserTask;
	}

	public void setFileChooserTask(FileChooserTask fileChooserTask) {
		this.fileChooserTask = fileChooserTask;
	}

	public DragAndDropTask getDragAndDropTask() {
		return dragAndDropTask;
	}

	public void setDragAndDropTask(DragAndDropTask dragAndDropTask) {
		this.dragAndDropTask = dragAndDropTask;
	}
	
	public DivisionThumb getDivisionThumbs() {
		return divisionThumbs;
	}
	
	public void setDivisionThumbs(DivisionThumb divisionThumbs) {
		this.divisionThumbs = divisionThumbs;
	}
	
	public ExecPool getExecPool() {
		return execPool;
	}
	
	public void setExecPool(ExecPool execPool) {
		this.execPool = execPool;
	}
	
	public ExecutorService getExecutor() {
		return executor;
	}
	
	public void setExecutor(ExecutorService executor) {
		this.executor = executor;
	}

	public ImgThumb getImgThumb() {
		return imgThumb;
	}
	
	public void setImgThumb(ImgThumb imgThumb) {
		this.imgThumb = imgThumb;
	}
	
	public ImgExtraction getImgExtraction() {
		return imgExtraction;
	}
	
	public void setImgExtraction(ImgExtraction imgExtraction) {
		this.imgExtraction = imgExtraction;
	}
	
	public Cancel getCancel() {
		return cancel;
	}
	
	public void setCancel(Cancel cancel) {
		this.cancel = cancel;
	}

	public Thread getShowThumbsThread() {
		return showThumbsThread;
	}
	
	public void setShowThumbsThread(Thread showThumbsThread) {
		this.showThumbsThread = showThumbsThread;
	}
	
	public Thread getDivisionThumbsThread() {
		return divisionThumbsThread;
	}

	public void setDivisionThumbsThread(Thread divisionThumbsThread) {
		this.divisionThumbsThread = divisionThumbsThread;
	}
	
	public Thread getExecPoolThread() {
		return execPoolThread;
	}
	
	public void setExecPoolThread(Thread execPoolThread) {
		this.execPoolThread = execPoolThread;
	}
	
	public Thread getOpenFileChooserThread() {
		return OpenFileChooserThread;
	}
	
	public void setOpenFileChooserThread(Thread openFileChooserThread) {
		OpenFileChooserThread = openFileChooserThread;
	}
	
	public Thread getDragAnDropFileChooserThread() {
		return DragAnDropFileChooserThread;
	}
	
	public void setDragAnDropFileChooserThread(Thread dragAnDropFileChooserThread) {
		DragAnDropFileChooserThread = dragAnDropFileChooserThread;
	}
	
	public Thread getImgThumbThread() {
		return imgThumbThread;
	}
	
	public void setImgThumbThread(Thread imgThumbThread) {
		this.imgThumbThread = imgThumbThread;
	}
	
	public Thread getImgExtractionThread() {
		return imgExtractionThread;
	}
	
	public void setImgExtractionThread(Thread imgExtractionThread) {
		this.imgExtractionThread = imgExtractionThread;
	}
	
	public Thread getCancelThread() {
		return cancelThread;
	}
	
	public void setCancelThread(Thread cancelThread) {
		this.cancelThread = cancelThread;
	}

}
