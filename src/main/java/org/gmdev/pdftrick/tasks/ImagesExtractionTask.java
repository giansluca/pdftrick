package org.gmdev.pdftrick.tasks;

import java.awt.*;
import java.awt.event.MouseListener;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.*;
import javax.swing.border.Border;

import org.gmdev.pdftrick.engine.ImagesExtractor;
import org.gmdev.pdftrick.manager.PdfTrickBag;
import org.gmdev.pdftrick.render.ImageAction;
import org.gmdev.pdftrick.swingmanager.WaitPanel;

public class ImagesExtractionTask implements Runnable {
	
	private static final PdfTrickBag BAG = PdfTrickBag.INSTANCE;

	private final AtomicBoolean running = new AtomicBoolean(false);
	
	public void stop() {
	    running.set(false);
	 }

	public boolean isRunning() {
		return running.get();
	}
	
	@Override
	public void run () {
		running.set(true);
		WaitPanel.setExtractingImagesWaitPanel();
		
		ImagesExtractor imagesExtractor = new ImagesExtractor();
		imagesExtractor.getImages();
		
		WaitPanel.removeWaitPanel();
		
		if (running.get()) {
			try {
				SwingUtilities.invokeAndWait(this::cleanAfterGetImages);
			} catch (InterruptedException | InvocationTargetException e) {
				throw new IllegalStateException(e);
			}
		}
		
		running.set(true);
	}
	
	/**
	 * Deselect images after extraction and clean images elected HashMap
	 */
	public void cleanAfterGetImages() {
		final JPanel centerPanel = BAG.getUserInterface().getCenter().getCenterPanel();
		final JTextField numImgSelectedField = BAG.getUserInterface().getRight().getNumImgSelectedField();
		
		Border borderGray = BorderFactory.createLineBorder(Color.gray);
		Component[] comps =  centerPanel.getComponents();
		Component component;

		for (Component comp : comps) {
			component = comp;

			if (component instanceof JLabel) {
				JLabel picLabel = (JLabel) comp;
				String name = "" + picLabel.getName();

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
		
		BAG.cleanSelectedImagesHashMap();
		BAG.cleanInlineSelectedImagesHashMap();
		numImgSelectedField.setText("");
	}

}
