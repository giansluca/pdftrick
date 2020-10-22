package org.gmdev.pdftrick.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.log4j.Logger;

public class SetupUtils {
	
	private static final Logger LOGGER = Logger.getLogger(SetupUtils.class);
	public static final String WIN_OS = "win";
	public static final String MAC_OS = "mac";

	public static String getOs() {
		if (System.getProperty("os.name").toLowerCase().contains(WIN_OS))
			return WIN_OS;
		if (System.getProperty("os.name").toLowerCase().contains(MAC_OS))
			return MAC_OS;
		else
			throw new IllegalStateException("FATAL! Unknown Operating system");
	}

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

	public static void extractNativeLibrary() {
		String nameLib = "";
		File nativeLib = null;
		
		if (isWindows()) {
			nameLib = Consts.NATIVELIB_WIN_64;
			nativeLib = new File(getHomeFolder() + File.separator + Consts.NATIVELIB_WIN_64);
		} else if (isMac()) {
			nameLib = Consts.NATIVELIB_MAC_64;
			nativeLib = new File(getHomeFolder() + File.separator + Consts.NATIVELIB_MAC_64);
		}

		if (nativeLib.exists()) {
			return;
		}
		
		try {
			InputStream in = FileLoader.loadAsStream(
					Consts.NATIVELIB_PATH + File.separator + nameLib);

			File fileOut = new File(getHomeFolder() + File.separator + nameLib);
			OutputStream out = new FileOutputStream(fileOut);
			
			byte[] buf = new byte[8192];
            int len;
            
            while ((len = in.read(buf)) != -1) {
                out.write(buf, 0, len);
            }
			
            in.close();
			out.close();
		} catch (Exception e) {
			LOGGER.error(e);
		}
	}

	public static String createHomeFolder() {
		String userPath = System.getProperty("user.home");
		File userPathFolder = new File(userPath + File.separator + Consts.HOME_FOLDER);
		
		if (!userPathFolder.exists()) {
			userPathFolder.mkdir();
			if (isWindows()) {
				String[] cmd = {"attrib","+h",userPathFolder.getPath()};
				try {
					Runtime.getRuntime().exec(cmd);
				} catch (IOException e) {
					LOGGER.error(e);
				}
			}
		}
		return userPathFolder.getPath();
	}

	public static String getHomeFolder() {
		String userPath = System.getProperty("user.home");
		return userPath + File.separator + Consts.HOME_FOLDER;
	}

}
