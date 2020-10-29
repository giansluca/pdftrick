package org.gmdev.pdftrick.utils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.gmdev.pdftrick.utils.Constants.*;

public class SetupUtils {
	
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

	public static void setNativeLibrary(String homeFolder, String operatingSystem) {
		String libName;
		if (operatingSystem.equals(WIN_OS))
			libName = NATIVE_LIB_WIN_64;
		else if (operatingSystem.equals(MAC_OS))
			libName = NATIVE_LIB_MAC_64;
		else
			throw new IllegalStateException("Error selecting native library, should never get here");

		Path libPath = Path.of(homeFolder + File.separator + libName);
		if (libPath.toFile().exists())
			return;

		String libToCopy = NATIVE_LIB_PATH + "/" + libName;
		extractNativeLibrary(libPath, libToCopy);
	}

	private static void extractNativeLibrary(Path to, String libToCopy) {
		InputStream from = FileLoader.loadAsStream(libToCopy);
		try {
			Files.copy(from, to);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	public static String getOrCreateHomeFolder() {
		String userHomePath = System.getProperty("user.home");
		File pdfTrickHomeFolder = new File(userHomePath + File.separator + PDFTRICK_FOLDER);

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
				throw new IllegalStateException(e);
			}
		}

		return pdfTrickHomeFolder.getPath();
	}

}
