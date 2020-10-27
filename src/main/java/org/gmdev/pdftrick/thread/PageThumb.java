package org.gmdev.pdftrick.thread;

import org.apache.log4j.Logger;
import org.gmdev.pdftrick.factory.PdfTrickBag;
import org.gmdev.pdftrick.utils.Constants;
import org.gmdev.pdftrick.utils.NativeObjectManager;
import org.gmdev.pdftrick.utils.PdfTrickMessages;

public class PageThumb implements Runnable {
	
	private static final Logger logger = Logger.getLogger(PageThumb.class);
	private static final PdfTrickBag factory = PdfTrickBag.getPdfTrickBag();
	
	private final String imgPath;
	private final int numPage;
	
	public PageThumb(String imgPath, int numPage) {
		this.imgPath = imgPath;
		this.numPage = numPage;
	}
	
	@Override
	public void run() {
		execute();
	}
	
	/**
	 * Render pdf cover calling native lib
	 */
	public void execute() {
		NativeObjectManager nativeManager = factory.getNativemanager();
		
		try {
			nativeManager.runNativeLib_thumbs(factory.getResultFile(), imgPath, numPage, Constants.ZOOM_THUMB);
			//System.out.println(Thread.currentThread().getName() + " add image " + numPage);
		} catch (Exception e) {
			Thread.currentThread().interrupt();
			logger.error("Exception", e);
			PdfTrickMessages.append("ERROR", Constants.SEND_LOG_MSG);
		}
	}
	
	
}
