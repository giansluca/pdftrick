package org.gmdev.pdftrick.thread;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Path;
import java.util.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;

import org.apache.log4j.Logger;
import org.gmdev.pdftrick.manager.PdfTrickBag;
import org.gmdev.pdftrick.render.ThumbAction;
import org.gmdev.pdftrick.utils.*;

public class ShowThumbs implements Runnable {
	
	private static final Logger logger = Logger.getLogger(ShowThumbs.class);
	private static final PdfTrickBag BAG = PdfTrickBag.INSTANCE;
	
	private volatile boolean finished = false;

	public void stop() {
	    finished = true;
	 }
	
	@Override
	public void run() {
		execute();
	}
	
	/** 
	 * Check img folder, and show images in a left panel, last image written by render thread
	 */
	public void execute() {
		Properties messages = BAG.getMessagesProps();
		JPanel leftPanel = BAG.getUserInterface().getLeft().getLeftPanel();
		Path thumbnailsFolderPath = BAG.getThumbnailsFolderPath();

		long time = System.currentTimeMillis();
		long delta = 1000;
		
		try {
			Messages.appendNoNewLine("INFO", messages.getProperty("tmsg_08"));
			int numberOfPages = BAG.getNumberOfPages();
			File[] renderedImages;
			
			int i = 0;
			while (i < numberOfPages && !finished) {
				long timeLoop = System.currentTimeMillis();
				if (timeLoop > time + delta) {
					Messages.appendInline(messages.getProperty("tmsg_09"));
					delta = delta + 1000;
				}
				renderedImages = getPdfRenderedPages(thumbnailsFolderPath);
				if (renderedImages !=null && renderedImages.length > i) {
					
					// check is next file has arrived
					if (renderedImages[i] != null && renderedImages[i].getName().endsWith("_" + (i + 1) + ".png")) {
								
						// check if file look has gone (native function finished to write)
						File look = new File(thumbnailsFolderPath +
								File.separator + "image_" + (i + 1) + ".png.look");
							
						if (!look.exists()) {
							BufferedImage bufImg = null;
							try {
								if (renderedImages[i].exists() && renderedImages[i].canRead() && renderedImages[i].length() >0) {
									FileInputStream fin = new FileInputStream(renderedImages[i]);
									bufImg = ImageIO.read(fin);
									fin.close();
								}
							}
							catch (Exception e) {
								logger.error("Exception", e);
							}

							if (bufImg != null) {
								int w = bufImg.getWidth();
								int h = bufImg.getHeight();
								if (w > h) {
									bufImg = ImageUtils.getScaledImage(bufImg, 170, 126);
								} else {
									bufImg = ImageUtils.getScaledImage(bufImg, 170, 228);
								}
								
								// update left panel, need invokeLater because i'm out EDT here
								BufferedImage buffImage = bufImg;
								bufImg.flush();
							
								if (!finished) {	
									SwingUtilities.invokeAndWait(() -> {
										Border border = BorderFactory.createLineBorder(Color.gray);
										JLabel picLabel = new JLabel(new ImageIcon(buffImage));
										picLabel.setPreferredSize(new Dimension(176, 236));
										picLabel.setBorder(border);
										leftPanel.add(picLabel);
										leftPanel.revalidate();
										leftPanel.repaint();
									});
								}
								
								buffImage.flush();
								i++;
							}
						} 
					}
				}
			}
			
			if (!finished) {
				Messages.appendNewLine();
				Messages.append("INFO", messages.getProperty("tmsg_10"));
				
				// after rendering add the mouse listener for each thumb (jlabel) and stop wait Icon, i need invokeLater because i'm out EDT here 
				SwingUtilities.invokeLater(() -> {
					Component[] comps =  leftPanel.getComponents();
					for (int z = 0; z < comps.length; z++) {
						JLabel picLabel = (JLabel) comps[z];
						picLabel.addMouseListener(new ThumbAction(z + 1));
						picLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
					}
					FileUtils.stopWaitIcon();
				});
			}
		} catch (Exception e) {
			logger.error("Exception", e);
			Messages.appendNewLine();
		}
		finished = true;
	}
	
	private File[] getPdfRenderedPages(Path thumbnailsFolderPath) {
		File[] renderedPdfThumbnails = null;
		File imgFolder = thumbnailsFolderPath.toFile();
		
		if (imgFolder.exists()) {
			renderedPdfThumbnails = imgFolder.listFiles(
					(dir, name) -> name.toLowerCase().endsWith(".png"));
		}
		
		// sort files by filename
		Comparator<File> comparator = (f1, f2) -> {
			Integer num1 = Integer.parseInt(
					f1.getName().substring(6).replace(".png", "").trim());
			Integer num2 = Integer.parseInt(
					f2.getName().substring(6).replace(".png", "").trim());

			return (num1.compareTo(num2));
};
        
        if (renderedPdfThumbnails != null)
        	Arrays.sort(renderedPdfThumbnails, comparator);
        
		return renderedPdfThumbnails;
	}
	
	public boolean isFinished() {
		return finished;
	}
	

}
