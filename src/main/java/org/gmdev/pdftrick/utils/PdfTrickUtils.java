package org.gmdev.pdftrick.utils;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Properties;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;


import org.apache.log4j.Logger;
import org.gmdev.pdftrick.factory.PdfTrickFactory;
import org.gmdev.pdftrick.ui.actions.DragAndDropAction;
import org.gmdev.pdftrick.ui.custom.WrapLayout;
import org.imgscalr.Scalr;

public class PdfTrickUtils {
	
	private static final Logger logger = Logger.getLogger(PdfTrickUtils.class);
	private static final PdfTrickFactory factory = PdfTrickFactory.getFactory();
	
	/**
	 * Find jvm architecture
	 */
	public static String getArch() {
		String arch="";
		String jvmArch = System.getProperty("sun.arch.data.model");
		
		if (jvmArch.contains("64")) {
			arch="64";
		} else if (jvmArch.contains("32")) {
			arch="32";
		}
		
		return arch;
	}
	
	/**
	 * Create a path (File) in current folder like: /PdfTrick/trick-current_date(gg-mm-yyyy-hh-min-sec) 
	 */
	public static String getTimedDirResult() {
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int mounth = cal.get(Calendar.MONTH)+1;
		int day = cal.get(Calendar.DAY_OF_MONTH);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minute = cal.get(Calendar.MINUTE);
		int second = cal.get(Calendar.SECOND);
		
		String data = day+"-"+mounth+"-"+year+"_"+hour+"."+minute+"."+second;
		
		return "/PdfTrick_"+data;
	}
	
	public static Properties loadProperties() {
		Properties prop = new Properties();
		
		try {
			prop.load(FileLoader.loadAsStream(Consts.PROPERTYFILE));
		} catch (IOException e) {
			logger.error("Exception", e);
		}
		
		return prop;
	}
	
	public static String getNativeLibrary() {
		String nativeLibPath = "";
		
		if (PdfTrickPreInitUtils.isWindows()) {
			nativeLibPath = factory.getHiddenHomeFolder()+File.separator+Consts.NATIVELIB_WIN_64;
		} else if (PdfTrickPreInitUtils.isMac()) {
			nativeLibPath = factory.getHiddenHomeFolder()+File.separator+Consts.NATIVELIB_MAC_64;
		}
		
		return nativeLibPath;
	}
	
	/**
	 * Create img folder inside home hidden folder for store rendered images from native lib
	 */
	public static String createImgFolder() {
		File imgFolder = new File (factory.getHiddenHomeFolder()+File.separator+"img");
		
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
		File imgFolder = new File (factory.getHiddenHomeFolder()+File.separator+"img");
		
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
		final JPanel centerPanel = factory.getUserInterface().getCenter().getCenterPanel();
		
		ImageIcon imageIcon = new ImageIcon(FileLoader.loadAsUrl(Consts.WAIT));
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
		final JPanel centerPanel = factory.getUserInterface().getCenter().getCenterPanel();
		centerPanel.setLayout(new WrapLayout());
		centerPanel.removeAll();
		centerPanel.revalidate();
		centerPanel.repaint();
	}
	
	/**
	 * Clean the left panel
	 */
	public static void cleanLeftPanel() {
		final JPanel leftPanel = factory.getUserInterface().getLeft().getLeftPanel();
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
		final JPanel centerPanel = factory.getUserInterface().getCenter().getCenterPanel();
		centerPanel.removeAll();
		centerPanel.revalidate();
		centerPanel.repaint();
	}
	
	/**
	 * Remove the drop blue border, in some circumstances that happens only in windows OS and only at runtime
	 */
	public static void resetDropBorder() {
		final JPanel leftPanel = factory.getUserInterface().getLeft().getLeftPanel();
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
	 * Send log to server, yes password is not protected, i decided to not implement 
	 * a security system for ftp credential ... 
	 * the folder contains only log sent by user and if you want see inside .. you can :) 
	 */
	public static void sendLog() {
		logger.info("Os: "+System.getProperty("os.name"));
		logger.info("Arch: "+System.getProperty("sun.arch.data.model"));
		logger.info("Jvm: "+System.getProperty("java.version")); 
		String ftpUrl = "ftp://%s:%s@%s/%s;type=i";
		String filePath = factory.getHiddenHomeFolder()+File.separator+Consts.LOGFILE;
		String uploadPath = File.separator+Consts.FOLDER+File.separator+getTimedDirResult()+".log";
		ftpUrl = String.format(ftpUrl, Consts.USER, Consts.PASS, Consts.HOST, uploadPath);
		
		try {
		    URL url = new URL(ftpUrl);
		    URLConnection conn = url.openConnection();
		    OutputStream outputStream = conn.getOutputStream();
		    
		    FileInputStream inputStream = new FileInputStream(filePath);
		    byte[] buffer = new byte[1024];
		    int bytesRead = -1;
		    
		    while ((bytesRead = inputStream.read(buffer)) != -1) {
		        outputStream.write(buffer, 0, bytesRead);
		    }
		    
		    inputStream.close();
		    outputStream.close();
		} catch (IOException e) {
			logger.error("Exception", e);
		}
	}
	
	/**
	 * Print a welcome message on the text area
	 */
	public static void welcomeMessage() {
		PdfTrickMessages.append("INFO", MessageFormat.format(factory.getMessages().getProperty("dmsg_09"), 
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
