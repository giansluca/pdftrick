package org.gmdev.pdftrick.thread;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import org.apache.log4j.Logger;
import org.gmdev.pdftrick.manager.PdfTrickBag;
import org.gmdev.pdftrick.render.ThumbAction;
import org.gmdev.pdftrick.utils.Messages;
import org.gmdev.pdftrick.utils.Utils;

public class ShowThumbs implements Runnable {
	
	private static final Logger logger = Logger.getLogger(ShowThumbs.class);
	private static final PdfTrickBag BAG = PdfTrickBag.INSTANCE;
	
	private volatile boolean finished = false;
	
	public ShowThumbs() {
	}
	
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
		final Properties messages = BAG.getMessagesProps();
		final JPanel leftPanel = BAG.getUserInterface().getLeft().getLeftPanel();
		final Path hiddenHomeFolder = BAG.getHomeFolderPath();
		Path thumbnailsFolderPath = BAG.getThumbnailsFolderPath();

		long time = System.currentTimeMillis();
		long delta = 1000;
		
		try {
			Messages.appendNoNewLine("INFO", messages.getProperty("tmsg_08"));
			int nPages = BAG.getNumberOfPages();
			File[] imgVett = getCoverImagesRendered(hiddenHomeFolder);
			
			int i = 0;
			while (i < nPages && !finished) {
				long timeLoop = System.currentTimeMillis();
				if (timeLoop > time + delta) {
					Messages.appendInline(messages.getProperty("tmsg_09"));
					delta = delta + 1000;
				}
				imgVett = getCoverImagesRendered(thumbnailsFolderPath);
				if (imgVett !=null && imgVett.length > i) {
					
					// check is next file has arrived
					if (imgVett[i] != null && imgVett[i].getName().endsWith("_" + (i + 1) + ".png")) {
								
						// check if file look has gone (native function finished to write)
						File look = new File(thumbnailsFolderPath +
								File.separator + "image_" + (i + 1) + ".png.look");
							
						if (!look.exists()) {
							BufferedImage bufImg = null;
							try {
								if (imgVett[i].exists() && imgVett[i].canRead() && imgVett[i].length() >0) {
									FileInputStream fin = new FileInputStream(imgVett[i]);
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
									bufImg = Utils.getScaledImage(bufImg, 170, 126);
								} else {
									bufImg = Utils.getScaledImage(bufImg, 170, 228);
								}
								
								// update left panel, need invokeLater because i'm out EDT here
								final BufferedImage buffImage = bufImg;
								bufImg.flush();
							
								if (!finished) {	
									SwingUtilities.invokeAndWait(new Runnable() {
										@Override
										public void run() {
											Border border = BorderFactory.createLineBorder(Color.gray);
											JLabel picLabel = new JLabel(new ImageIcon(buffImage));
											picLabel.setPreferredSize(new Dimension(176, 236));
											picLabel.setBorder(border);
											leftPanel.add(picLabel);
											leftPanel.revalidate();
											leftPanel.repaint();
										}
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
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						Component[] comps =  leftPanel.getComponents();
						for (int z = 0; z < comps.length; z++) {
							JLabel picLabel = (JLabel) comps[z];
							picLabel.addMouseListener(new ThumbAction(z+1));
							picLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
						}
						Utils.stopWaitIcon();
					}
				});
			}
		} catch (Exception e) {
			logger.error("Exception", e);
			Messages.appendNewLine();
		}
		finished = true;
	}
	
	public File[] getCoverImagesRendered(Path thumbnailsFolderPath) {
		File[] imgVett = null;
		File imgFolder = thumbnailsFolderPath.toFile();
		
		if (imgFolder.exists()) {
			imgVett = imgFolder.listFiles(new FilenameFilter() {
			    public boolean accept(File dir, String name) {
			        return name.toLowerCase().endsWith(".png");
			    }
			});
		}
		
		// sort files by filename
		Comparator<File> comparator = new Comparator<File>() {
			@Override
            public int compare(File f1, File f2) {
				Integer num1 = Integer.parseInt( f1.getName().substring(6).replace(".png", "").trim() );
				Integer num2 = Integer.parseInt( f2.getName().substring(6).replace(".png", "").trim() );
				return ( num1.compareTo(num2 ) ) ;
            }
        };
        
        if (imgVett != null) {
        	Arrays.sort(imgVett, comparator);
        }
        
		return imgVett;
	}
	
	public boolean isFinished() {
		return finished;
	}
	

}
