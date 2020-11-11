package org.gmdev.pdftrick.utils;

import java.io.*;
import java.nio.file.*;

import static org.gmdev.pdftrick.utils.Constants.*;
import static org.gmdev.pdftrick.utils.SystemProperty.*;

public class SetupUtils {
	
	public static final String WIN_OS = "win";
	public static final String MAC_OS = "mac";

	private SetupUtils() {
		throw new AssertionError("SetupUtils class should never be instantiated");
	}

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

	public static Path setAndGetHomeFolder(String os) {
		String userHomePath = getSystemProperty("user.home");
		File homeFolder = new File(userHomePath + File.separator + PDFTRICK_HOME_FOLDER);

		if (homeFolder.exists())
			return homeFolder.toPath();

		return createHomeFolder(homeFolder, os);
	}

	private static Path createHomeFolder(File homeFolder, String os) {
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

		return homeFolder.toPath();
	}

	public static Path setAndGetNativeLibrary(Path homeFolder, String operatingSystem) {
		String libraryName;
		if (operatingSystem.equals(WIN_OS))
			libraryName = NATIVE_LIB_WIN_64;
		else if (operatingSystem.equals(MAC_OS))
			libraryName = NATIVE_LIB_MAC_64;
		else
			throw new IllegalStateException("Error selecting native library, should never get here");

		Path libraryDestinationPath = Path.of(homeFolder + File.separator + libraryName);
		String librarySource = NATIVE_LIB_PATH + "/" + libraryName;
		if (!libraryDestinationPath.toFile().exists())
			extractNativeLibrary(libraryDestinationPath, librarySource);

		return libraryDestinationPath;
	}

	private static void extractNativeLibrary(Path to, String librarySource) {
		try (InputStream in = FileLoader.loadAsStream(librarySource)) {
			Files.copy(in, to);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

}
