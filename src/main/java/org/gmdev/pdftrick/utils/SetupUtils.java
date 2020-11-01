package org.gmdev.pdftrick.utils;

import java.io.*;
import java.nio.file.*;

import static org.gmdev.pdftrick.utils.Constants.*;
import static org.gmdev.pdftrick.utils.SystemProperty.*;

public class SetupUtils {
	
	public static final String WIN_OS = "win";
	public static final String MAC_OS = "mac";

	public static String getOs() {
		String os = getSystemProperty("os.name");
		if (os.toLowerCase().contains(WIN_OS))
			return WIN_OS;
		else if (os.toLowerCase().contains(MAC_OS))
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
		return getSystemProperty("sun.arch.data.model").contains("64");
	}

	public static String setAndGetHomeFolder(String os) {
		String userHomePath = getSystemProperty("user.home");
		File homeFolder = new File(userHomePath + File.separator + PDFTRICK_FOLDER);

		if (homeFolder.exists())
			return homeFolder.getPath();

		return createHomeFolder(homeFolder, os);
	}

	private static String createHomeFolder(File homeFolder, String os) {
		if (!homeFolder.mkdir())
			throw new IllegalStateException("Error creating PdfTrick home folder");

		if (os.equals(WIN_OS)) {
			String[] command = {"attrib", "+h", homeFolder.getPath()};
			try {
				Runtime.getRuntime().exec(command);
			} catch (IOException e) {
				throw new IllegalStateException(e);
			}
		}

		return homeFolder.getPath();
	}

	public static Path setAndGetNativeLibrary(String homeFolder, String operatingSystem) {
		String libName;
		if (operatingSystem.equals(WIN_OS))
			libName = NATIVE_LIB_WIN_64;
		else if (operatingSystem.equals(MAC_OS))
			libName = NATIVE_LIB_MAC_64;
		else
			throw new IllegalStateException("Error selecting native library, should never get here");

		Path libPath = Path.of(homeFolder + File.separator + libName);
		if (libPath.toFile().exists())
			return libPath;

		String libToCopy = NATIVE_LIB_PATH + "/" + libName;
		return extractNativeLibrary(libPath, libToCopy);
	}

	private static Path extractNativeLibrary(Path to, String libToCopy) {
		InputStream from = FileLoader.loadAsStream(libToCopy);
		try {
			Files.copy(from, to);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}

		return to;
	}

}
