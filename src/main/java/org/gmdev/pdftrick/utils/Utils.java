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

public class Utils {
	
	private static final PdfTrickBag BAG = PdfTrickBag.INSTANCE;

	public static String getTimedDirResult() {
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
			prop.load(FileLoader.loadAsStream(Constants.MESSAGES_PROPERTY_FILE));
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
		
		return prop;
	}

	public static String createImgFolder() {
		File imgFolder = new File (BAG.getHomeFolderPath() + File.separator + "img");
		
		if (imgFolder.exists()) {
			File[] imageFiles = imgFolder.listFiles();
			if (imageFiles != null && imageFiles.length > 0)
				for (File item : imageFiles)
					if (item != null && item.exists())
						item.delete();

		} else
			imgFolder.mkdir();

		return imgFolder.getPath()+File.separator;
	}

	public static void deletePdfFile() {
		File pdfFile = new File(BAG.getPdfFilePath());
		if (pdfFile.exists())
			if(!pdfFile.delete())
				throw new IllegalStateException("Error deleting pdf file");
	}

	public static void deleteNativeLibraryFile() {
		File nativeLibraryFile = BAG.getNativeLibraryPath().toFile();
		if (nativeLibraryFile.exists())
			if(nativeLibraryFile.delete())
				throw new IllegalStateException("Error deleting native library file");
	}

	public static void deleteImgFolderAnDFiles() {
		File imgFolder = new File (BAG.getHomeFolderPath() + File.separator + "img");
		
		if (imgFolder.exists()) {
			File[] imageFiles = imgFolder.listFiles();
			if (imageFiles != null && imageFiles.length > 0)
				for (File item : imageFiles)
					if (item != null && item.exists())
						item.delete();

			imgFolder.delete();
		}
	}
	
	/**
	 * Delete the final folder to save image extracted
	 */
	public static void deleteSelectedFolderToSave(String folder) {
		File fileFinalFolderTosave = new File(folder);
		
		if (fileFinalFolderTosave.exists()) {
			File[] vetImg = fileFinalFolderTosave.listFiles();
			if (vetImg.length > 0) {
				for (File item : vetImg) {
					if (item != null && item.exists()) {
						item.delete();
					}
				}
			}
			
			fileFinalFolderTosave.delete();
		}
	}
	
	public static BufferedImage getScaledImage(BufferedImage srcImg, int w, int h){
	    BufferedImage resizedImg = new BufferedImage(w, h, srcImg.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : srcImg.getType() );
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
	
	/**
	 * Adjust the image, called on visualization and on extraction (flip and rotation)
	 */
	public static BufferedImage adjustImage(BufferedImage srcImg, String flip, String angle) {
		BufferedImage buffImg = srcImg;
		if (flip.equalsIgnoreCase("fh")) {
			buffImg = Scalr.rotate(buffImg, Scalr.Rotation.FLIP_HORZ);
		}
		else if (flip.equalsIgnoreCase("fv")) {
			buffImg = Scalr.rotate(buffImg, Scalr.Rotation.FLIP_VERT);
		}
		if (angle.equalsIgnoreCase("270")) {
			buffImg = Scalr.rotate(buffImg, Scalr.Rotation.CW_270);
		}
		else if (angle.equalsIgnoreCase("180")) {
			buffImg = Scalr.rotate(buffImg, Scalr.Rotation.CW_180);
		}
		else if (angle.equalsIgnoreCase("90")) {
			buffImg = Scalr.rotate(buffImg, Scalr.Rotation.CW_90);
		}
		
		srcImg.flush();
		
		return buffImg;
	}
	
	/**
	 * Start wait icon in center panel
	 */
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
	
	/**
	 * Stop wait Icon in center panel
	 */
	public static void stopWaitIcon() {
		JPanel centerPanel = BAG.getUserInterface().getCenter().getCenterPanel();
		centerPanel.setLayout(new WrapLayout());
		centerPanel.removeAll();
		centerPanel.revalidate();
		centerPanel.repaint();
	}
	
	/**
	 * Clean the left panel
	 */
	public static void cleanLeftPanel() {
		JPanel leftPanel = BAG.getUserInterface().getLeft().getLeftPanel();
		FileDrop.remove(leftPanel);
		leftPanel.removeAll();
		leftPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
		
		new FileDrop(leftPanel, new DragAndDropAction());
		leftPanel.revalidate();
		leftPanel.repaint();
	}
	
	/**
	 * Clean the center panel
	 */
	public static void cleanCenterPanel() {
		JPanel centerPanel = BAG.getUserInterface().getCenter().getCenterPanel();
		centerPanel.removeAll();
		centerPanel.revalidate();
		centerPanel.repaint();
	}
	
	/**
	 * Remove the drop blue border, in some circumstances that happens only in windows OS and only at runtime
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

	/**
	 * Print a welcome message on the text area
	 */
	public static void printWelcomeMessage() {
		Messages.append("INFO", MessageFormat.format(BAG.getMessages().getProperty("dmsg_09"),
	    		System.getProperty("os.name"),
	    		System.getProperty("sun.arch.data.model"),
	    		System.getProperty("java.version")));
	}
	
	/**
	 * Reinitializes new filesVett, called on cancel button and open new pdf file
	 */
	public static void cleanFilevett(){
		BAG.getPdfFilesArray().clear();
	}
	
	/**
	 * Reinitialize new ImageSelectedHashMap, called on cancel button, open new pdf file and after getImages
	 */
	public static void cleanImageSelectedHashMap() {
		BAG.getImageSelected().clear();
	}
	
	/**
	 * Reinitialize new RotationFromPagesHashMap, called on cancel button and open new pdf file
	 */
	public static void cleanRotationFromPagesHashMap() {
		BAG.getRotationFromPages().clear();
	}
	
	/**
	 * Clean InlineImgSelectedHashMap, called on cancel button, open new pdf file and after getImages
	 */
	public static void cleanInlineImgSelectedHashMap() {
		BAG.getInlineImgSelected().clear();
	}
	
	
}
