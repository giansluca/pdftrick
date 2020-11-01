package org.gmdev.pdftrick.utils;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.text.MessageFormat;
import java.util.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import org.apache.log4j.Logger;
import org.gmdev.pdftrick.factory.PdfTrickBag;
import org.gmdev.pdftrick.ui.actions.DragAndDropAction;
import org.gmdev.pdftrick.ui.custom.WrapLayout;
import org.gmdev.pdftrick.utils.external.FileDrop;
import org.imgscalr.Scalr;

public class Utils {
	
	private static final Logger logger = Logger.getLogger(Utils.class);
	private static final PdfTrickBag factory = PdfTrickBag.getPdfTrickBag();

	public static String getTimedDirResult() {
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH)+1;
		int day = cal.get(Calendar.DAY_OF_MONTH);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minute = cal.get(Calendar.MINUTE);
		int second = cal.get(Calendar.SECOND);
		
		String data = day + "-" + month + "-" + year + "_" + hour + "." + minute + "." + second;
		return "/PdfTrick_"+data;
	}
	
	public static Properties loadProperties() {
		Properties prop = new Properties();
		try {
			prop.load(FileLoader.loadAsStream(Constants.PROPERTY_FILE));
		} catch (IOException e) {
			logger.error("Exception", e);
		}
		
		return prop;
	}
	
	public static String getNativeLibrary() {
		if (SetupUtils.isWindows())
			return factory.getHomeFolder() + File.separator + Constants.NATIVE_LIB_WIN_64;
		else if (SetupUtils.isMac())
			return  factory.getHomeFolder() + File.separator + Constants.NATIVE_LIB_MAC_64;
		else
			throw new IllegalStateException("Error selecting native library, should never get here");
	}
	
	/**
	 * Create img folder inside home hidden folder for store rendered images from native lib
	 */
	public static String createImgFolder() {
		File imgFolder = new File (factory.getHomeFolder() + File.separator+"img");
		
		if (imgFolder.exists()) {
			File[] vetImg = imgFolder.listFiles();
			if (vetImg.length > 0) {
				for (File item : vetImg) {
					if (item != null && item.exists()) {
						item.delete();
					}
				}
			}
		} else {
			imgFolder.mkdir();
		}
		
		return imgFolder.getPath()+File.separator;
	}

	/**
	 * Delete merged file in hidden home folder
	 */
	public static void deleteResultFile() {
		File resulFile = new File(factory.getResultFile());
		
		if (resulFile.exists()) {
			resulFile.delete();
		}
	}
	
	/**
	 * Delete native lib in hidden home folder
	 */
	public static void deleteNativeLibFile() {
		File nativelibFile = new File(getNativeLibrary());
		
		if (nativelibFile.exists()) {
			nativelibFile.delete();
		}
	}
	
	/**
	 * Delete img folder and imaged contained inside
	 */
	public static void deleteImgFolderAnDFile() {
		File imgFolder = new File (factory.getHomeFolder()+File.separator+"img");
		
		if (imgFolder.exists()) {
			File[] vetImg = imgFolder.listFiles();
			if (vetImg.length > 0) {
				for (File item : vetImg) {
					if (item != null && item.exists()) {
						item.delete();
					}
				}
			}
			
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
		JPanel centerPanel = factory.getUserInterface().getCenter().getCenterPanel();
		
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
		JPanel centerPanel = factory.getUserInterface().getCenter().getCenterPanel();
		centerPanel.setLayout(new WrapLayout());
		centerPanel.removeAll();
		centerPanel.revalidate();
		centerPanel.repaint();
	}
	
	/**
	 * Clean the left panel
	 */
	public static void cleanLeftPanel() {
		JPanel leftPanel = factory.getUserInterface().getLeft().getLeftPanel();
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
		JPanel centerPanel = factory.getUserInterface().getCenter().getCenterPanel();
		centerPanel.removeAll();
		centerPanel.revalidate();
		centerPanel.repaint();
	}
	
	/**
	 * Remove the drop blue border, in some circumstances that happens only in windows OS and only at runtime
	 */
	public static void resetDropBorder() {
		JPanel leftPanel = factory.getUserInterface().getLeft().getLeftPanel();
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
	public static void welcomeMessage() {
		Messages.append("INFO", MessageFormat.format(factory.getMessages().getProperty("dmsg_09"),
	    		System.getProperty("os.name"),
	    		System.getProperty("sun.arch.data.model"),
	    		System.getProperty("java.version")));
	}
	
	/**
	 * Reinitializes new filesVett, called on cancel button and open new pdf file
	 */
	public static void cleanFilevett(){
		factory.getFilesVett().clear();
	}
	
	/**
	 * Reinitialize new ImageSelectedHashMap, called on cancel button, open new pdf file and after getImages
	 */
	public static void cleanImageSelectedHashMap() {
		factory.getImageSelected().clear();
	}
	
	/**
	 * Reinitialize new RotationFromPagesHashMap, called on cancel button and open new pdf file
	 */
	public static void cleanRotationFromPagesHashMap() {
		factory.getRotationFromPages().clear();
	}
	
	/**
	 * Clean InlineImgSelectedHashMap, called on cancel button, open new pdf file and after getImages
	 */
	public static void cleanInlineImgSelectedHashMap() {
		factory.getInlineImgSelected().clear();
	}
	
	
}
