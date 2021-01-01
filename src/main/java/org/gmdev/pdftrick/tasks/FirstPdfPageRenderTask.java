package org.gmdev.pdftrick.tasks;

import org.gmdev.pdftrick.manager.PdfTrickBag;
import org.gmdev.pdftrick.nativeutil.NativeObjectManager;
import org.gmdev.pdftrick.serviceprocessor.Stoppable;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.gmdev.pdftrick.utils.Constants.ZOOM_THUMBNAIL;

public class FirstPdfPageRenderTask implements Runnable, Stoppable {
	
	private static final PdfTrickBag bag = PdfTrickBag.INSTANCE;
	
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
		NativeObjectManager nativeManager = bag.getNativeObjectManager();

		int i = 1;
		while (i <= division && running.get()) {
			nativeManager.renderPdfPageThumbnail(
					bag.getPdfFilePath().toString(), imagesFolderPath, i, ZOOM_THUMBNAIL);
			i++;
		}
		
		running.set(false);
	}

}
