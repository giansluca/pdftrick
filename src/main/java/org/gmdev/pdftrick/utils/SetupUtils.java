package org.gmdev.pdftrick.utils;

import java.io.*;

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

	public static void extractNativeLibrary(String homeFolder, String operatingSystem) {
		String libName;
		File libFile;
		if (operatingSystem.equals(WIN_OS)) {
			libName = Constants.NATIVE_LIB_WIN_64;
			libFile = new File(homeFolder + File.separator + libName);
		} else if (operatingSystem.equals(MAC_OS)) {
			libName = Constants.NATIVE_LIB_MAC_64;
			libFile = new File(homeFolder + File.separator + libName);
		} else {
			throw new IllegalStateException("Error selecting native library, should never get here");
		}

		if (libFile.exists())
			return;
		
		try {
			InputStream in = FileLoader.loadAsStream(Constants.NATIVE_LIB_PATH + "/" + libName);
			File fileOut = new File(homeFolder + File.separator + libName);
			OutputStream out = new FileOutputStream(fileOut);
			
			byte[] buffer = new byte[8192];
            int len;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
			
            in.close();
			out.close();
		} catch (IOException e) {
			logger.error(e);
		}
	}

	public static String getOrCreateHomeFolder() {
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
