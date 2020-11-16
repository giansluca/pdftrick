package org.gmdev.pdftrick.utils;

import java.awt.*;
import java.io.*;
import java.nio.file.Path;
import java.util.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import org.gmdev.pdftrick.manager.PdfTrickBag;
import org.gmdev.pdftrick.ui.actions.DragAndDropAction;
import org.gmdev.pdftrick.ui.custom.WrapLayout;
import org.gmdev.pdftrick.utils.external.FileDrop;

import static org.gmdev.pdftrick.utils.Constants.*;

public class FileUtils {
	
	private static final PdfTrickBag BAG = PdfTrickBag.INSTANCE;

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
		if (!file.delete())
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

	public static void startWaitIconLoadPdf() {
		JPanel centerPanel = BAG.getUserInterface().getCenter().getCenterPanel();
		
		ImageIcon imageIcon = new ImageIcon(FileLoader.loadFileAsUrl(WAIT));
		JLabel waitLabel = new JLabel(imageIcon);
		
		waitLabel.setHorizontalAlignment(JLabel.CENTER);
		waitLabel.setVerticalAlignment(JLabel.CENTER);
		centerPanel.setLayout(new GridBagLayout());
		centerPanel.add(waitLabel);
		centerPanel.revalidate();
		centerPanel.repaint();
	}

	public static void stopWaitIcon() {
		JPanel centerPanel = BAG.getUserInterface().getCenter().getCenterPanel();
		centerPanel.setLayout(new WrapLayout());
		centerPanel.removeAll();
		centerPanel.revalidate();
		centerPanel.repaint();
	}

	public static void cleanLeftPanel() {
		JPanel leftPanel = BAG.getUserInterface().getLeft().getLeftPanel();
		FileDrop.remove(leftPanel);
		leftPanel.removeAll();
		leftPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
		
		new FileDrop(leftPanel, new DragAndDropAction());
		leftPanel.revalidate();
		leftPanel.repaint();
	}

	public static void cleanCenterPanel() {
		JPanel centerPanel = BAG.getUserInterface().getCenter().getCenterPanel();
		centerPanel.removeAll();
		centerPanel.revalidate();
		centerPanel.repaint();
	}
	
	/**
	 * Remove the file drop blue border, in some circumstances it is needed under windows OS
	 */
	public static void resetLeftPanelFileDropBorder() {
		JPanel leftPanel = BAG.getUserInterface().getLeft().getLeftPanel();
		leftPanel.setBorder(new EmptyBorder(0, 0, 0, 0)); 
	}

	public static void cleanPdfFilesArray(){
		BAG.getPdfFilesArray().clear();
	}

	public static void cleanImageSelectedHashMap() {
		BAG.getImageSelected().clear();
	}

	public static void cleanInlineImgSelectedHashMap() {
		BAG.getInlineImgSelected().clear();
	}

	public static void cleanRotationFromPagesHashMap() {
		BAG.getRotationFromPages().clear();
	}

	
}
