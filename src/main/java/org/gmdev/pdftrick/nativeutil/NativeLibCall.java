package org.gmdev.pdftrick.nativeutil;

import org.gmdev.pdftrick.manager.PdfTrickBag;

import java.nio.file.Path;

public class NativeLibCall {

	public native void renderThumbnail(String pdfFilePath, String imgPath, int pageNumber, int zoom);
	public native void renderFull(String pdfFilePath, String imgPath, int pageNumber, int zoom);

	private static final PdfTrickBag bag = PdfTrickBag.INSTANCE;

	static{
		Path nativeLibraryPath = bag.getNativeLibraryPath();
		try{
			System.load(nativeLibraryPath.toString());
		} catch (SecurityException | UnsatisfiedLinkError e) {
			throw new IllegalStateException(e);
		}
	}

}
