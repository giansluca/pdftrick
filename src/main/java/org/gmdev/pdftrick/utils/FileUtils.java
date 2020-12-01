package org.gmdev.pdftrick.utils;

import java.io.*;
import java.nio.file.Path;
import java.util.*;

public class FileUtils {

	private FileUtils() {
		throw new AssertionError("FileUtils class should never be instantiated");
	}

	public static void cleanUpPdfTrickHome(Path thumbnailsFolderPath, Path pdfFilePath) {
		deletePdfFile(pdfFilePath);
		if (createIfNotExistsThumbnailsFolder(thumbnailsFolderPath)) return;
		deleteThumbnailFiles(thumbnailsFolderPath);
	}

	public static boolean createIfNotExistsThumbnailsFolder(Path thumbnailsFolderPath) {
		File thumbnailsFolder = thumbnailsFolderPath.toFile();
		if (thumbnailsFolder.exists()) return false;

		if (!thumbnailsFolder.mkdir())
				throw new IllegalStateException("Error creating thumbnails folder");
		return true;
	}

	public static void deleteThumbnailFiles(Path thumbnailsFolderPath) {
		File thumbnailsFolder = thumbnailsFolderPath.toFile();
		if (!thumbnailsFolder.exists()) return;

		File[] thumbnailFiles = thumbnailsFolder.listFiles();
		if (thumbnailFiles == null || thumbnailFiles.length == 0) return;

		deleteFileArray(thumbnailFiles);
	}

	public static void deletePdfFile(Path pdfFilePath) {
		File pdfFile = pdfFilePath.toFile();
		if (!pdfFile.exists()) return;
		deleteFile(pdfFile);
	}

	public static void deleteExtractionFolderAndImages(Path extractionFolderPath) {
		File extractionFolder = extractionFolderPath.toFile();
		if (!extractionFolder.exists()) return;

		File[] imageFiles = extractionFolder.listFiles();
		if (imageFiles != null && imageFiles.length > 0)
			deleteFileArray(imageFiles);

		deleteFolder(extractionFolder);
	}

	private static void deleteFileArray(File[] files) {
		for (File file : files)
			deleteFile(file);
	}

	private static void deleteFile(File file) {
		if (!file.delete() || !file.canWrite())
			throw new IllegalStateException(
					String.format("Error deleting file %s", file.getName()));
	}

	private static void deleteFolder(File folder) {
		if (!folder.delete())
			throw new IllegalStateException(
					String.format("Error deleting folder %s", folder.getName()));
	}

	public static String getTimeForExtractionFolder() {
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		int day = cal.get(Calendar.DAY_OF_MONTH);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minute = cal.get(Calendar.MINUTE);
		int second = cal.get(Calendar.SECOND);

		String time = String.format("%d-%d-%d_%d.%d.%d", day, month, year, hour, minute, second);
		return "PdfTrick_" + time;
	}

}
