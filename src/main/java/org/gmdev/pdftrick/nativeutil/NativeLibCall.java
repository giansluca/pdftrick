package org.gmdev.pdftrick.nativeutil;

import org.apache.log4j.Logger;
import org.gmdev.pdftrick.utils.Utils;

public class NativeLibCall {
	
	private static final Logger logger = Logger.getLogger(NativeLibCall.class);
	
	public native void start(String resultFile, String homeHiddenfolder, int nPage, int zoom);
	public native void cover(String resultFile, String homeHiddenfolder, int nPage, int zoom);
	
	/**
	 * Load native library from hidden home folder
	 */
	static{
		String nativeLibPath = Utils.getNativeLibrary();
		
		try{
			System.load(nativeLibPath);
		} catch (UnsatisfiedLinkError e){
			logger.error("Exception", e);
		}
	}
	
}
