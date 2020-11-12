package org.gmdev.pdftrick.utils;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.nio.file.Path;
import java.util.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import org.gmdev.pdftrick.manager.PdfTrickBag;
import org.gmdev.pdftrick.ui.actions.DragAndDropAction;
import org.gmdev.pdftrick.ui.custom.WrapLayout;
import org.gmdev.pdftrick.utils.external.FileDrop;
import org.imgscalr.Scalr;

import static org.gmdev.pdftrick.utils.Constants.*;

public class Utils {
	
	private static final PdfTrickBag BAG = PdfTrickBag.INSTANCE;

	private Utils() {
		throw new AssertionError("Utils class should never be instantiated");
	}

	public static void cleanUp(Path thumbnailsFolderPath, Path pdfFilePath) {
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
	
	public static BufferedImage getScaledImage(BufferedImage sourceImage, int w, int h) {
	    BufferedImage resizedImg =
				new BufferedImage(w, h,
						sourceImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : sourceImage.getType()
				);

	    Graphics2D graphics = resizedImg.createGraphics();
		graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphics.drawImage(sourceImage, 0, 0, w, h, null);
		graphics.dispose();
	    
	    return resizedImg;
	}
	
	public static BufferedImage getScaledImageWithScalr(BufferedImage sourceImage, int w, int h) {
	    return Scalr.resize(
	    		sourceImage,
				Scalr.Method.QUALITY,
				Scalr.Mode.FIT_EXACT,
				w,
				h,
				Scalr.OP_ANTIALIAS);
	}

	public static BufferedImage adjustImage(BufferedImage sourceImage, String flip, String angle) {
		BufferedImage buffImg = sourceImage;
		if (flip.equalsIgnoreCase("fh"))
			buffImg = Scalr.rotate(buffImg, Scalr.Rotation.FLIP_HORZ);
		else if (flip.equalsIgnoreCase("fv"))
			buffImg = Scalr.rotate(buffImg, Scalr.Rotation.FLIP_VERT);
		if (angle.equalsIgnoreCase("270"))
			buffImg = Scalr.rotate(buffImg, Scalr.Rotation.CW_270);
		else if (angle.equalsIgnoreCase("180"))
			buffImg = Scalr.rotate(buffImg, Scalr.Rotation.CW_180);
		else if (angle.equalsIgnoreCase("90"))
			buffImg = Scalr.rotate(buffImg, Scalr.Rotation.CW_90);

		sourceImage.flush();
		return buffImg;
	}

	public static Image TransformGrayToTransparency(BufferedImage image) {
		ImageFilter filter = new RGBImageFilter() {
			public final int filterRGB(int x, int y, int rgb) {
				return (rgb << 8) & 0xFF000000;
			}
		};

		ImageProducer imageProducer = new FilteredImageSource(image.getSource(), filter);
		return Toolkit.getDefaultToolkit().createImage(imageProducer);
	}

	/**
	 * Apply mask (alpha channel to an image)
	 */
	public static BufferedImage ApplyTransparency(BufferedImage sourceImage, Image mask) {
		BufferedImage destImage = new BufferedImage(
				sourceImage.getWidth(), sourceImage.getHeight(),BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics = destImage.createGraphics();

		graphics.drawImage(sourceImage, 0, 0, null);
		AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.DST_IN, 1.0F);
		graphics.setComposite(ac);
		graphics.drawImage(mask, 0, 0, null);
		graphics.dispose();

		return destImage;
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
