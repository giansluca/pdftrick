package org.gmdev.pdftrick.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.log4j.Logger;

public class PdfTrickPreInitUtils {
	
	private static final Logger logger = Logger.getLogger(PdfTrickPreInitUtils.class);
	
	/**
	 * Check if operating system is windows
	 */
	public static boolean isWindows() {
		String os = System.getProperty("os.name").toLowerCase();
		return (os.contains("win"));
	}
	
	/**
	 * Check if operating system is osx
	 */
	public static boolean isMac() {
		String os = System.getProperty("os.name").toLowerCase();
		return (os.contains("mac"));
	}
	
	/**
	 * Check if jvm arch is 64 bit
	 */
	public static boolean isJvm64() {
		boolean jvm64 = false;
		String jvmArch = System.getProperty("sun.arch.data.model");
		
		if (jvmArch.contains("64")) {
			jvm64 = true;
		}
		
		return jvm64;
	}
	
	/**
	 * Check if jvm arch is 32 bit
	 */
	public static boolean isJvm32() {
		boolean jvm32 = false;
		String jvmArch = System.getProperty("sun.arch.data.model");
		
		if (jvmArch.contains("32")) {
			jvm32 = true;
		}
		
		return jvm32;
	}
	
	/**
	 * Set some osx graphic properties, used only in osx environment
	 */
	public static void setMacPreferencies() {
		System.setProperty("com.apple.mrj.application.apple.menu.about.name", "PdfTrick");
		System.setProperty("apple.laf.useScreenMenuBar", "true");
		System.setProperty("apple.awt.fileDialogForDirectories", "true");
	}
	
	/**
	 * Extract native c lib from jar in hidden home folder
	 */
	public static void extractNativeLibrary() {
		String nameLib = "";
		File nativelib = null;
		
		if (isWindows()) {
			nameLib = Consts.NATIVELIB_WIN_64;
			nativelib = new File(getHiddenHomeFolder()+File.separator+Consts.NATIVELIB_WIN_64);
		} else if (isMac()) {
			nameLib = Consts.NATIVELIB_MAC_64;
			nativelib = new File(getHiddenHomeFolder()+File.separator+Consts.NATIVELIB_MAC_64);
		}
		
		if (nativelib.exists()) {
			return;
		}
		
		try {
			InputStream in = FileLoader.loadAsStream(Consts.NATIVELIB_PATH+File.separator+nameLib);
			File fileOut = new File(getHiddenHomeFolder()+File.separator+nameLib);
			OutputStream out = new FileOutputStream(fileOut);
			
			byte[] buf = new byte[8192];
            int len;
            
            while ((len = in.read(buf)) != -1) {
                out.write(buf, 0, len);
            }
			
            in.close();
			out.close();
		} catch (Exception e) {
			logger.error("Exception", e);
		}
	}
	
	/**
	 * Create hidden home folder .pdftrick on startup
	 */
	public static String createHiddenHomeFolder() {
		String userPath = System.getProperty("user.home");
		File userPathFolder = new File(userPath+File.separator+Consts.HIDDENFOLDER);
		
		if (!userPathFolder.exists()) {
			userPathFolder.mkdir();
			if (isWindows()) {
				String[] cmd = {"attrib","+h",userPathFolder.getPath()};
				try {
					Runtime.getRuntime().exec(cmd);
				} catch (IOException e) {
					logger.error("Exception", e);
				}
			}
		}
		return userPathFolder.getPath();
	}
	
	/**
	 * Get the hidden home folder path
	 */
	public static String getHiddenHomeFolder() {
		String userPath = System.getProperty("user.home");
		String hiddenHomeFolder = userPath+File.separator+Consts.HIDDENFOLDER;
		return hiddenHomeFolder;
	}
	
	

}
