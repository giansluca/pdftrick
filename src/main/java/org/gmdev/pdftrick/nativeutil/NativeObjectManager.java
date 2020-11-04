package org.gmdev.pdftrick.nativeutil;

import java.lang.reflect.Method;
import org.apache.log4j.Logger;

public class NativeObjectManager {
	
	private static final Logger logger = Logger.getLogger(NativeObjectManager.class);
	
	private CustomClassLoader customClassLoader = null;
	private Class<?> classNative = null;
	private Object instanceNative = null;
	private Method methodNative_thumbs = null;
	private Method methodNative_cover = null;
	
	public NativeObjectManager() {
		loadNativeLib();
	}

	private void loadNativeLib() {
		try {
			customClassLoader = new CustomClassLoader();
			classNative = customClassLoader.findClass("org.gmdev.pdftrick.nativeutil.NativeLibCall");
			instanceNative = classNative.getDeclaredConstructor().newInstance();
		} catch (Exception e) {
			logger.error("Exception", e);
		}
	}
	
	/**
	 * Call native function in native library for render thumbs.
	 * Reflection to avoid some problems that may happen under windows OS
	 */
	public void runNativeLib_thumbs(String resultFilePath, String imgPath, int nPage, int zoom) {
		try {
			if (methodNative_thumbs == null)
				methodNative_thumbs = classNative.getMethod(
						"start", String.class, String.class, int.class, int.class);

			methodNative_thumbs.invoke(instanceNative, resultFilePath, imgPath, nPage, zoom);
		} catch (Exception e) {
			logger.error("Exception", e);
		}	
	}
	
	/**
	 * Call native function in native library for extract cover
	 */
	public void runNativeLib_cover(String resultFilePath, String imgPath, int nPage, int zoom) {
		try {
			if (methodNative_cover == null)
				methodNative_cover = classNative.getMethod(
						"cover", String.class, String.class, int.class, int.class);

			methodNative_cover.invoke(instanceNative, resultFilePath, imgPath, nPage, zoom);
		} catch (Exception e) {
			logger.error("Exception", e);
		}	
	}

	public void unloadNativeLib() {
		customClassLoader = null;
		classNative = null;
		instanceNative = null;
		
		System.gc();
	}
	
}
