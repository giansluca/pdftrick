package org.gmdev.pdftrick.tasks;

import org.apache.log4j.Logger;
import org.gmdev.pdftrick.manager.PdfTrickBag;
import org.gmdev.pdftrick.nativeutil.NativeObjectManager;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.gmdev.pdftrick.utils.Constants.ZOOM_THUMBNAIL;

public class FirstPdfPageRenderTask implements Runnable  {
	
	private static final Logger logger = Logger.getLogger(FirstPdfPageRenderTask.class);
	private static final PdfTrickBag BAG = PdfTrickBag.INSTANCE;
	
	private final int division;
	private final String imgPath;
	private final AtomicBoolean running = new AtomicBoolean(false);
	
	public FirstPdfPageRenderTask(int division, String imgPath) {
		this.division = division;
		this.imgPath = imgPath;
	}
	
	public void stop() {
	    running.set(false);
	 }

	public boolean isRunning() {
		return running.get();
	}

	@Override
	public void run() {
		running.set(true);
		NativeObjectManager nativeManager = BAG.getNativeObjectManager();
		int i = 1;
		
		while (i <= division && running.get()) {
			try {
				nativeManager.renderPdfPageThumbnail(
						BAG.getPdfFilePath().toString(), imgPath, i, ZOOM_THUMBNAIL);
				i++;
			} catch (Exception e) {
				Thread.currentThread().interrupt();
				logger.error("Exception", e);
			}
		}
		
		running.set(false);
	}

}
