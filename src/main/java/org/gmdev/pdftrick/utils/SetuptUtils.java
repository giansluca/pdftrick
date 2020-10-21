package org.gmdev.pdftrick.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.log4j.Logger;

public class SetuptUtils {
	
	private static final Logger logger = Logger.getLogger(SetuptUtils.class);
	public static final String WIN_OS = "win";
	public static final String MAC_OS = "mac";

	public static boolean isWindows() {
		String os = System.getProperty("os.name").toLowerCase();
		return os.contains(WIN_OS);
	}

	public static boolean isMac() {
		String os = System.getProperty("os.name").toLowerCase();
		return os.contains(MAC_OS);
	}

	public static boolean isJvm64() {
		return System.getProperty("sun.arch.data.model").contains("64");
	}

	public static void setMacPreferences() {
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
