package org.gmdev.pdftrick.nativeutil;

import java.lang.reflect.*;

public class NativeObjectManager {

	private CustomClassLoader classLoader;
	private Class<?> nativeLibCall;
	private Object nativeLibCallInstance;
	private Method coverThumbnailMethod;
	private Method coverFullMethod;

	private static final String NATIVE_LIB_CALL = "org.gmdev.pdftrick.nativeutil.NativeLibCall";
	private static final String RENDER_THUMBNAIL_FUNCTION = "start";
	private static final String RENDER_FULL_FUNCTION = "cover";
	
	public NativeObjectManager() {
		loadNativeLibrary();
	}

	/**
	 * Reflection and a Custom class loader are used to avoid some problems that may happen under windows OS
	 */
	private void loadNativeLibrary() {
		try {
			classLoader = new CustomClassLoader();
			nativeLibCall = classLoader.findClass(NATIVE_LIB_CALL);
			nativeLibCallInstance = nativeLibCall.getDeclaredConstructor().newInstance();
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}

	public void renderPdfPageThumbnail(String pdfFilePath, String imgPath, int nPage, int zoom) {
		try {
			if (coverThumbnailMethod == null)
				coverThumbnailMethod = nativeLibCall.getMethod(
						RENDER_THUMBNAIL_FUNCTION, String.class, String.class, int.class, int.class);

			coverThumbnailMethod.invoke(nativeLibCallInstance, pdfFilePath, imgPath, nPage, zoom);
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}

	public void renderPdfPageFull(String pdfFilePath, String imgPath, int nPage, int zoom) {
		try {
			if (coverFullMethod == null)
				coverFullMethod = nativeLibCall.getMethod(
						RENDER_FULL_FUNCTION, String.class, String.class, int.class, int.class);

			coverFullMethod.invoke(nativeLibCallInstance, pdfFilePath, imgPath, nPage, zoom);
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}	
	}

	public void unloadNativeLib() {
		classLoader = null;
		nativeLibCall = null;
		nativeLibCallInstance = null;
	}
	
}
