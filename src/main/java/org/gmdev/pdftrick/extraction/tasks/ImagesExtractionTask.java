package org.gmdev.pdftrick.extraction.tasks;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.*;

import org.gmdev.pdftrick.extraction.ImageExtractor;
import org.gmdev.pdftrick.manager.PdfTrickBag;
import org.gmdev.pdftrick.serviceprocessor.Stoppable;
import org.gmdev.pdftrick.swingmanager.WaitPanel;

public class ImagesExtractionTask implements Runnable, Stoppable {
	
	private static final PdfTrickBag bag = PdfTrickBag.INSTANCE;

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

		ImageExtractor imageExtractor = new ImageExtractor();
		imageExtractor.extract();
		
		WaitPanel.removeWaitPanel();
		
		if (running.get()) {
			try {
				SwingUtilities.invokeAndWait(this::cleanAfterGetImages);
			} catch (InterruptedException | InvocationTargetException e) {
				throw new IllegalStateException(e);
			}
		}
		
		running.set(false);
	}

	public void cleanAfterGetImages() {
		bag.getUserInterface().getCenter().cleanSelection();
		bag.cleanSelectedImagesHashMap();

		JTextField numImgSelectedField = bag.getUserInterface().getRight().getSelectedImagesField();
		numImgSelectedField.setText("");
	}

}
