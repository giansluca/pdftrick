package org.gmdev.pdftrick.thread;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseListener;
import java.lang.reflect.InvocationTargetException;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import org.apache.log4j.Logger;
import org.gmdev.pdftrick.engine.ImagesExtractor;
import org.gmdev.pdftrick.manager.PdfTrickBag;
import org.gmdev.pdftrick.render.ImageAction;
import org.gmdev.pdftrick.swingmanager.WaitPanel;
import org.gmdev.pdftrick.utils.FileUtils;

public class ImgExtraction implements Runnable {
	
	private static final Logger logger = Logger.getLogger(ImgExtraction.class);
	private static final PdfTrickBag BAG = PdfTrickBag.INSTANCE;
	
	volatile boolean finished = false;
	
	public ImgExtraction() {
	}
	
	public void stop() {
	    finished = true;
	 }
	
	@Override
	public void run() {
		execute();
		System.gc();
	}
	
	/**
	 * Extract images
	 */
	public void execute () {
		WaitPanel.setExtractingImagesWaitPanel();
		
		ImagesExtractor engine = new ImagesExtractor();
		engine.getImages();
		
		WaitPanel.removeWaitPanel();
		
		if (!finished) {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					@Override
					public void run() {
						cleanAfterGetImages();
					}
				});
			} catch (InterruptedException | InvocationTargetException e) {
				logger.error("Exception", e);
			}
		}
		
		finished = true;
	}
	
	/**
	 * Deselect images after extraction and clean imageselected HashMap
	 */
	public void cleanAfterGetImages() {
		final JPanel centerPanel = BAG.getUserInterface().getCenter().getCenterPanel();
		final JTextField numImgSelectedField = BAG.getUserInterface().getRight().getNumImgSelectedField();
		
		Border borderGray = BorderFactory.createLineBorder(Color.gray);
		Component[] comps =  centerPanel.getComponents();
		Component component = null;
		
		for (int z = 0; z < comps.length; z++) {
			component = comps[z];
			
			if (component instanceof JLabel) {
				JLabel picLabel = (JLabel) comps[z];
				String name = ""+picLabel.getName();
				
				if (!name.equalsIgnoreCase("NoPicsImg")) {
					picLabel.setBorder(borderGray);
					picLabel.setOpaque(true);
					picLabel.setBackground(Color.WHITE);
					MouseListener[] mls = (picLabel.getListeners(MouseListener.class));
					
					if (mls.length > 0) {
						ImageAction act = (ImageAction) mls[0];
						act.setSelected(false);
					}
				}	
			}
		}
		
		FileUtils.cleanImageSelectedHashMap();
		FileUtils.cleanInlineImgSelectedHashMap();
		numImgSelectedField.setText("");
		
		System.gc();
	}

	public boolean isFinished() {
		return finished;
	}
	
	
}
