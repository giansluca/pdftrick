package org.gmdev.pdftrick.nativeutil;

import java.lang.reflect.*;

public class NativeObjectManager {

	private CustomClassLoader classLoader;
	private Class<?> nativeLibCallClass;
	private Object nativeLibCallInstance;
	private Method pageThumbnailMethod;
	private Method pageFullMethod;

	private static final String NATIVE_LIB_CALL = "org.gmdev.pdftrick.nativeutil.NativeLibCall";
	private static final String RENDER_THUMBNAIL_FUNCTION = "start";
	private static final String RENDER_FULL_FUNCTION = "cover";
	
	public NativeObjectManager() {
		loadNativeLibrary();
	}

	/**
	 * Reflection and Custom class loader are used to avoid some problems
	 * that may happen under windows OS
	 */
	private void loadNativeLibrary() {
		try {
			classLoader = new CustomClassLoader();
			nativeLibCallClass = classLoader.findClass(NATIVE_LIB_CALL);
			nativeLibCallInstance = nativeLibCallClass.getDeclaredConstructor().newInstance();
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}

	public void renderPdfPageThumbnail(String pdfFilePath, String imgPath, int nPage, int zoom) {
		try {
			if (pageThumbnailMethod == null)
				pageThumbnailMethod = nativeLibCallClass.getMethod(
						RENDER_THUMBNAIL_FUNCTION, String.class, String.class, int.class, int.class);

			pageThumbnailMethod.invoke(nativeLibCallInstance, pdfFilePath, imgPath, nPage, zoom);
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}

	public void renderPdfPageFull(String pdfFilePath, String imgPath, int nPage, int zoom) {
		try {
			if (pageFullMethod == null)
				pageFullMethod = nativeLibCallClass.getMethod(
						RENDER_FULL_FUNCTION, String.class, String.class, int.class, int.class);

			pageFullMethod.invoke(nativeLibCallInstance, pdfFilePath, imgPath, nPage, zoom);
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}	
	}

	public void unloadNativeLib() {
		classLoader = null;
		nativeLibCallClass = null;
		nativeLibCallInstance = null;
		System.runFinalization();
		System.gc();
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			throw new IllegalStateException(e);
		}
	}

}
