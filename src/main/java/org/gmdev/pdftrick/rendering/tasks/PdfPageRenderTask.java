package org.gmdev.pdftrick.rendering.tasks;

import org.gmdev.pdftrick.manager.PdfTrickBag;
import org.gmdev.pdftrick.nativeutil.NativeObjectManager;

import java.nio.file.Path;

import static org.gmdev.pdftrick.utils.Constants.ZOOM_THUMBNAIL;

public class PdfPageRenderTask implements Runnable {
	
	private static final PdfTrickBag bag = PdfTrickBag.INSTANCE;
	
	private final String imagePath;
	private final int pageNumber;
	
	public PdfPageRenderTask(String imagePath, int pageNumber) {
		this.imagePath = imagePath;
		this.pageNumber = pageNumber;
	}

	@Override
	public void run() {
		NativeObjectManager nativeManager = bag.getNativeObjectManager();
		Path pdfFilePath = bag.getSavedFilePath();

		nativeManager.renderPdfPageThumbnail(pdfFilePath.toString(), imagePath, pageNumber, ZOOM_THUMBNAIL);
	}

}
