package org.gmdev.pdftrick.tasks;

import org.apache.log4j.Logger;
import org.gmdev.pdftrick.manager.PdfTrickBag;
import org.gmdev.pdftrick.nativeutil.NativeObjectManager;
import org.gmdev.pdftrick.serviceprocessor.Stoppable;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.gmdev.pdftrick.utils.Constants.ZOOM_THUMBNAIL;

public class FirstPdfPageRenderTask implements Runnable, Stoppable {
	
	private static final Logger logger = Logger.getLogger(FirstPdfPageRenderTask.class);
	private static final PdfTrickBag BAG = PdfTrickBag.INSTANCE;
	
	private final int division;
	private final String imagesFolderPath;
	private final AtomicBoolean running = new AtomicBoolean(false);
	
	public FirstPdfPageRenderTask(int division, String imagesFolderPath) {
		this.division = division;
		this.imagesFolderPath = imagesFolderPath;
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
						BAG.getPdfFilePath().toString(), imagesFolderPath, i, ZOOM_THUMBNAIL);
				i++;
			} catch (Exception e) {
				Thread.currentThread().interrupt();
				logger.error("Exception", e);
			}
		}
		
		running.set(false);
	}

}
