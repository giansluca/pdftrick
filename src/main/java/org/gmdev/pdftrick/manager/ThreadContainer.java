package org.gmdev.pdftrick.manager;

import java.util.concurrent.ExecutorService;

import org.gmdev.pdftrick.thread.Cancel;
import org.gmdev.pdftrick.thread.DivisionThumb;
import org.gmdev.pdftrick.thread.DragAnDropFileChooser;
import org.gmdev.pdftrick.thread.ExecPool;
import org.gmdev.pdftrick.thread.ImgExtraction;
import org.gmdev.pdftrick.thread.ImgThumb;
import org.gmdev.pdftrick.thread.OpenFileChooser;
import org.gmdev.pdftrick.thread.ShowThumbs;

public class ThreadContainer {
	
	private volatile ShowThumbs showThumbs = null;
	private volatile DivisionThumb divisionThumbs = null;
	private volatile ExecPool execPool = null;
	private volatile ExecutorService executor = null;
	private volatile OpenFileChooser openFileChooser = null;
	private volatile DragAnDropFileChooser dragAnDropFileChooser = null;
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
	private volatile Thread sendLogThread = null;
	
	public synchronized ShowThumbs getShowThumbs() {
		return showThumbs;
	}
	
	public synchronized void setShowThumbs(ShowThumbs showThumbs) {
		this.showThumbs = showThumbs;
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
	
	public OpenFileChooser getOpenFileChooser() {
		return openFileChooser;
	}
	
	public void setOpenFileChooser(OpenFileChooser openFileChooser) {
		this.openFileChooser = openFileChooser;
	}
	
	public DragAnDropFileChooser getDragAnDropFileChooser() {
		return dragAnDropFileChooser;
	}
	
	public void setDragAnDropFileChooser(DragAnDropFileChooser dragAnDropFileChooser) {
		this.dragAnDropFileChooser = dragAnDropFileChooser;
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
	
	public Thread getSendLogThread() {
		return sendLogThread;
	}
	
	public void setSendLogThread(Thread sendLogThread) {
		this.sendLogThread = sendLogThread;
	}	
	
}
