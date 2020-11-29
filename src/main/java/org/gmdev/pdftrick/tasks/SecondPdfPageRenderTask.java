package org.gmdev.pdftrick.tasks;

import org.gmdev.pdftrick.manager.PdfTrickBag;
import org.gmdev.pdftrick.nativeutil.NativeObjectManager;

import java.nio.file.Path;

import static org.gmdev.pdftrick.utils.Constants.ZOOM_THUMBNAIL;

public class SecondPdfPageRenderTask implements Runnable {
	
	private static final PdfTrickBag BAG = PdfTrickBag.INSTANCE;
	
	private final String imagePath;
	private final int pageNumber;
	
	public SecondPdfPageRenderTask(String imagePath, int pageNumber) {
		this.imagePath = imagePath;
		this.pageNumber = pageNumber;
	}

	@Override
	public void run() {
		NativeObjectManager nativeManager = BAG.getNativeObjectManager();
		Path pdfFilePath = BAG.getPdfFilePath();
		
		try {
			nativeManager.renderPdfPageThumbnail(pdfFilePath.toString(), imagePath, pageNumber, ZOOM_THUMBNAIL);
		} catch (Exception e) {
			Thread.currentThread().interrupt();
			throw new IllegalStateException(e);
		}
	}
	
	
}
