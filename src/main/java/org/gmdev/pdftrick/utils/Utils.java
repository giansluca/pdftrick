package org.gmdev.pdftrick.utils;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.text.MessageFormat;
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
		throw new AssertionError("Utils should never be instantiated");
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
		return "/PdfTrick_" + time;
	}
	
	public static Properties loadMessageProperties() {
		Properties prop = new Properties();
		try {
			prop.load(FileLoader.loadAsStream(MESSAGES_PROPERTY_FILE));
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
		return prop;
	}

	public static void cleanUp() {
		createIfNotExistsThumbnailsFolder();
		deleteThumbnailsFiles();
		deletePdfFile();
	}

	public static void createIfNotExistsThumbnailsFolder() {
		File thumbnailsFolder = BAG.getThumbnailsFolderPath().toFile();
		if (!thumbnailsFolder.exists())
			if (!thumbnailsFolder.mkdir())
				throw new IllegalStateException("Error creating thumbnails folder");
	}

	public static void deleteThumbnailsFiles() {
		File thumbnailsFolder = BAG.getThumbnailsFolderPath().toFile();
		if (!thumbnailsFolder.exists()) return;

		File[] thumbnailFiles = thumbnailsFolder.listFiles();
		if (thumbnailFiles == null || thumbnailFiles.length == 0) return;

		deleteFileArray(thumbnailFiles);
	}

	private static void deleteFileArray(File[] files) {
		for (File file : files)
			if(!file.delete())
				throw new IllegalStateException(
						String.format("Error deleting image file %s", file.getName()));
	}

	public static void deletePdfFile() {
		File pdfFile = new File(BAG.getPdfFilePath());
		if (!pdfFile.exists()) return;

		if (!pdfFile.delete())
			throw new IllegalStateException("Error deleting pdf file");
	}

	public static void deleteSelectedFolderToSave(String extractionFolderPath) {
		File extractionFolder = new File(extractionFolderPath);
		if (!extractionFolder.exists()) return;

		File[] imagesArray = extractionFolder.listFiles();
		if (imagesArray == null || imagesArray.length == 0) return;

		for (File file : imagesArray)
			if (!file.delete())
				throw new IllegalStateException(
						String.format("Error deleting extracted image file %s", file.getName()));

		if (!extractionFolder.delete())
			throw new IllegalStateException(
					String.format("Error deleting extraction folder %s", extractionFolder.getName()));
	}
	
	public static BufferedImage getScaledImage(BufferedImage srcImg, int w, int h){
	    BufferedImage resizedImg = new BufferedImage(w, h, srcImg.getType() == 0
				? BufferedImage.TYPE_INT_ARGB :
				srcImg.getType() );

	    Graphics2D g2 = resizedImg.createGraphics();
	    
	    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
	    g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	    g2.drawImage(srcImg, 0, 0, w, h, null);
	    g2.dispose();
	    
	    return resizedImg;
	}
	
	public static BufferedImage getScaledImagWithScalr(BufferedImage srcImg, int w, int h) {
	    return Scalr.resize(srcImg, Scalr.Method.QUALITY, Scalr.Mode.FIT_EXACT, w, h, Scalr.OP_ANTIALIAS); 
	}

	public static BufferedImage adjustImage(BufferedImage srcImg, String flip, String angle) {
		BufferedImage buffImg = srcImg;
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
		
		srcImg.flush();
		return buffImg;
	}

	public static void startWaitIconLoadPdf() {
		JPanel centerPanel = BAG.getUserInterface().getCenter().getCenterPanel();
		
		ImageIcon imageIcon = new ImageIcon(FileLoader.loadAsUrl(Constants.WAIT));
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
	 * Remove the drop blue border, in some circumstances that happens only in windows OS
	 */
	public static void resetDropBorder() {
		JPanel leftPanel = BAG.getUserInterface().getLeft().getLeftPanel();
		leftPanel.setBorder(new EmptyBorder(0, 0, 0, 0)); 
	}
	
	/**
	 * Transform gray in transparency of an image
	 */
	public static Image TransformGrayToTransparency(BufferedImage image) {
	    ImageFilter filter = new RGBImageFilter() {
	        public final int filterRGB(int x, int y, int rgb) {
	            return (rgb << 8) & 0xFF000000;
	        }
	    };
	    ImageProducer ip = new FilteredImageSource(image.getSource(), filter);
	    return Toolkit.getDefaultToolkit().createImage(ip);
	}
	
	/**
	 * Apply a mask (alpha channel to an image)
	 */
	public static BufferedImage ApplyTransparency(BufferedImage image, Image mask) {
	    BufferedImage dest = new BufferedImage(image.getWidth(), image.getHeight(),BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g2 = dest.createGraphics();
	    
	    g2.drawImage(image, 0, 0, null);
	    AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.DST_IN, 1.0F);
	    g2.setComposite(ac);
	    g2.drawImage(mask, 0, 0, null);
	    g2.dispose();
	    
	    return dest;
	}

	public static void printWelcomeMessage() {
		Messages.append("INFO", MessageFormat.format(BAG.getMessages().getProperty("dmsg_09"),
	    		System.getProperty("os.name"),
	    		System.getProperty("sun.arch.data.model"),
	    		System.getProperty("java.version")));
	}

	public static void cleanPdfFilesArray(){
		BAG.getPdfFilesArray().clear();
	}

	public static void cleanImageSelectedHashMap() {
		BAG.getImageSelected().clear();
	}

	public static void cleanRotationFromPagesHashMap() {
		BAG.getRotationFromPages().clear();
	}

	public static void cleanInlineImgSelectedHashMap() {
		BAG.getInlineImgSelected().clear();
	}
	
	
}
