package org.gmdev.pdftrick.thread;

import java.lang.reflect.InvocationTargetException;

import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;
import org.gmdev.pdftrick.manager.PdfTrickBag;
import org.gmdev.pdftrick.utils.FileUtils;
import org.gmdev.pdftrick.utils.Messages;

public class Cancel implements Runnable {
	
	private static final Logger logger = Logger.getLogger(Cancel.class);
	private static final PdfTrickBag BAG = PdfTrickBag.INSTANCE;
	
	volatile boolean finished = false;
	
	public Cancel() {
	}
	
	public void stop() {
	    finished = true;
	 }
	
	@Override
	public void run() {
		try {
			execute();
		} catch (Exception e) {
			logger.error("Exception", e);
		}
	}
	
	/**
	 * Clean the interface and all objects
	 */
	public void execute () throws InterruptedException {
		JTextField currentPageField = BAG.getUserInterface().getRight().getCurrentPageField();
		JTextField numImgSelectedField = BAG.getUserInterface().getRight().getNumImgSelectedField();
		
		if (BAG.getThreadContainer().getOpenFileChooser() != null) {
			while (!BAG.getThreadContainer().getOpenFileChooser().isFinished()) {
				// wait thread stop
			}
			
			if (BAG.getThreadContainer().getOpenFileChooserThread() !=null) {
				BAG.getThreadContainer().getOpenFileChooserThread().join();
				
				while (BAG.getThreadContainer().getOpenFileChooserThread().isAlive()) {
					// wait thread stop
				}
			}
		}
		
		if (BAG.getThreadContainer().getDragAnDropFileChooser() != null) {
			while (!BAG.getThreadContainer().getDragAnDropFileChooser().isFinished()) {
				// wait thread stop
			}
			
			if (BAG.getThreadContainer().getDragAnDropFileChooserThread() != null) {
				BAG.getThreadContainer().getDragAnDropFileChooserThread().join();
				while (BAG.getThreadContainer().getDragAnDropFileChooserThread().isAlive()) {
				// wait thread stop
				}
			}
		}
		
		if (BAG.getThreadContainer().getDivisionThumbs() != null && !BAG.getThreadContainer().getDivisionThumbs().isFinished()) {
			BAG.getThreadContainer().getDivisionThumbs().stop();
			while (!BAG.getThreadContainer().getDivisionThumbs().isFinished()) {
				// wait thread stop
			}
			if (BAG.getThreadContainer().getDivisionThumbsThread() != null) {
				BAG.getThreadContainer().getDivisionThumbsThread().join();
				while (BAG.getThreadContainer().getDivisionThumbsThread().isAlive()) {
					// wait thread stop
				}
			}
		}
		
		if (BAG.getThreadContainer().getShowThumbs() != null && !BAG.getThreadContainer().getShowThumbs().isFinished()) {
			BAG.getThreadContainer().getShowThumbs().stop();
			while (!BAG.getThreadContainer().getShowThumbs().isFinished()) {
				// wait thread stop
			}
			if (BAG.getThreadContainer().getShowThumbsThread() != null) {
				BAG.getThreadContainer().getShowThumbsThread().join();
				while (BAG.getThreadContainer().getShowThumbsThread().isAlive()) {
					// wait thread stop
				}
			}
		}
		
		if (BAG.getThreadContainer().getExecPool() != null && !BAG.getThreadContainer().getExecPool().isFinished()) {
			BAG.getThreadContainer().getExecPool().stop();
			if (BAG.getThreadContainer().getExecPoolThread() != null) {
				BAG.getThreadContainer().getExecPoolThread().join();
				while (BAG.getThreadContainer().getExecPoolThread().isAlive()) {
					// wait thread stop
				}
			}
		}
		if (BAG.getThreadContainer().getExecutor() != null) {
			BAG.getThreadContainer().getExecutor().shutdownNow();
			while (!BAG.getThreadContainer().getExecutor().isTerminated()) {
				//wait stop all threadPool task
			}
		}
		
		try {
			SwingUtilities.invokeAndWait(() -> {
				BAG.getUserInterface().getLeft().clean();
				BAG.getUserInterface().getCenter().clean();
				Messages.cleanTextArea();
				currentPageField.setText("");
				numImgSelectedField.setText("");
			});
		} catch (InterruptedException | InvocationTargetException e) {
			logger.error("Exception", e);
		}

		BAG.cleanSelectedImagesHashMap();
		BAG.cleanInlineSelectedImagesHashMap();
		BAG.cleanRotationFromPagesHashMap();
		BAG.setSelectedPage(0);
		BAG.setExtractionFolderPath(null);
		BAG.cleanPdfFilesArray();
		
		FileUtils.deletePdfFile(BAG.getPdfFilePath());
		FileUtils.deleteThumbnailFiles(BAG.getThumbnailsFolderPath());

		finished = true;
	}
	
	public boolean isFinished() {
		return finished;
	}

}
