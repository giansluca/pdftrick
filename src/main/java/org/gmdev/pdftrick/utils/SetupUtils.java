package org.gmdev.pdftrick.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.log4j.Logger;

public class SetupUtils {
	
	private static final Logger logger = Logger.getLogger(SetupUtils.class);
	public static final String WIN_OS = "win";
	public static final String MAC_OS = "mac";

	public static String getOs() {
		if (System.getProperty("os.name").toLowerCase().contains(WIN_OS))
			return WIN_OS;
		if (System.getProperty("os.name").toLowerCase().contains(MAC_OS))
			return MAC_OS;
		else
			throw new IllegalStateException("Unknown Operating system");
	}

	public static boolean isWindows() {
		return getOs().equals(WIN_OS);
	}

	public static boolean isMac() {
		return getOs().equals(MAC_OS);
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
			nameLib = Constants.NATIVE_LIB_WIN_64;
			nativeLib = new File(getHomeFolder() + File.separator + Constants.NATIVE_LIB_WIN_64);
		} else if (isMac()) {
			nameLib = Constants.NATIVE_LIB_MAC_64;
			nativeLib = new File(getHomeFolder() + File.separator + Constants.NATIVE_LIB_MAC_64);
		}

		if (nativeLib.exists()) {
			return;
		}
		
		try {
			InputStream in = FileLoader.loadAsStream(
					Constants.NATIVE_LIB_PATH + File.separator + nameLib);

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
			logger.error(e);
		}
	}

	public static String getHomeFolder() {
		String userHomePath = System.getProperty("user.home");
		File pdfTrickHomeFolder = new File(userHomePath + File.separator + Constants.PDFTRICK_FOLDER);

		if (pdfTrickHomeFolder.exists())
			return pdfTrickHomeFolder.getPath();

		return createHomeFolder(pdfTrickHomeFolder);
	}

	private static String createHomeFolder(File pdfTrickHomeFolder) {
		if (!pdfTrickHomeFolder.mkdir())
			throw new IllegalStateException("Error creating PdfTrick home folder");

		if (isWindows()) {
			String[] command = {"attrib", "+h", pdfTrickHomeFolder.getPath()};
			try {
				Runtime.getRuntime().exec(command);
			} catch (IOException e) {
				logger.error(e);
			}
		}

		return pdfTrickHomeFolder.getPath();
	}

}
