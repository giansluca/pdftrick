package org.gmdev.pdftrick.nativeutil;

import org.gmdev.pdftrick.manager.PdfTrickBag;

import java.nio.file.Path;

public class NativeLibCall {

	public native void start(String resultFile, String homeFolder, int nPage, int zoom);
	public native void cover(String resultFile, String homeFolder, int nPage, int zoom);

	private static final PdfTrickBag bag = PdfTrickBag.getBag();

	static{
		Path nativeLibraryPath = bag.getNativeLibraryPath();
		try{
			System.load(nativeLibraryPath.toString());
		} catch (SecurityException | UnsatisfiedLinkError e) {
			throw new IllegalStateException(e);
		}
	}

}
