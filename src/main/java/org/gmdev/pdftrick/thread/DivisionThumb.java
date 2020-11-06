package org.gmdev.pdftrick.thread;

import org.apache.log4j.Logger;
import org.gmdev.pdftrick.manager.PdfTrickBag;
import org.gmdev.pdftrick.utils.Constants;
import org.gmdev.pdftrick.nativeutil.NativeObjectManager;

public class DivisionThumb implements Runnable  {
	
	private static final Logger logger = Logger.getLogger(DivisionThumb.class);
	private static final PdfTrickBag BAG = PdfTrickBag.INSTANCE;
	
	private final int division;
	private final String imgPath;
	volatile boolean finished = false;
	
	public DivisionThumb(int division, String imgPath) {
		this.division = division;
		this.imgPath = imgPath;
	}
	
	public void stop() {
	    finished = true;
	 }
	
	@Override
	public void run() {
		execute();
	}
	
	/** 
	 * Call native function for thumb rendering for first 1/3 pages, other pager are rendered by threadpool
	 * for improve performance. Whitout this trick (using only threadpool) there is a little initial delay
	 */
	public void execute() {
		NativeObjectManager nativeManager = BAG.getNativeObjectManager();
		int i = 1;
		
		while (i <= division && !finished) {
			try {
				nativeManager.renderPdfPageThumbnail(BAG.getPdfFilePath(), imgPath, i, Constants.ZOOM_THUMBNAIL);
				i++;
			} catch (Exception e) {
				Thread.currentThread().interrupt();
				logger.error("Exception", e);
			}
		}
		
		finished = true;
	}
	
	public boolean isFinished() {
		return finished;
	}
	
	public void setFinished(boolean finished) {
		this.finished = finished;
	}
	
}
