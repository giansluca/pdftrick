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
		
		if (BAG.getTasksContainer().getFileChooserTask() != null) {
			while (BAG.getTasksContainer().getFileChooserTask().isRunning()) {
				// wait thread stop
			}
			
			if (BAG.getTasksContainer().getOpenFileChooserThread() !=null) {
				BAG.getTasksContainer().getOpenFileChooserThread().join();
				
				while (BAG.getTasksContainer().getOpenFileChooserThread().isAlive()) {
					// wait thread stop
				}
			}
		}
		
		if (BAG.getTasksContainer().getDragAndDropTask() != null) {
			while (BAG.getTasksContainer().getDragAndDropTask().isRunning()) {
				// wait thread stop
			}
			
			if (BAG.getTasksContainer().getDragAnDropFileChooserThread() != null) {
				BAG.getTasksContainer().getDragAnDropFileChooserThread().join();
				while (BAG.getTasksContainer().getDragAnDropFileChooserThread().isAlive()) {
				// wait thread stop
				}
			}
		}
		
		if (BAG.getTasksContainer().getDivisionThumbs() != null &&
				!BAG.getTasksContainer().getDivisionThumbs().isFinished()) {

			BAG.getTasksContainer().getDivisionThumbs().stop();
			while (!BAG.getTasksContainer().getDivisionThumbs().isFinished()) {
				// wait thread stop
			}
			if (BAG.getTasksContainer().getDivisionThumbsThread() != null) {
				BAG.getTasksContainer().getDivisionThumbsThread().join();
				while (BAG.getTasksContainer().getDivisionThumbsThread().isAlive()) {
					// wait thread stop
				}
			}
		}
		
		if (BAG.getTasksContainer().getShowPdfCoverThumbnailsTask() != null &&
				!BAG.getTasksContainer().getShowPdfCoverThumbnailsTask().isRunning()) {

			BAG.getTasksContainer().getShowPdfCoverThumbnailsTask().stop();
			while (BAG.getTasksContainer().getShowPdfCoverThumbnailsTask().isRunning()) {
				// wait thread stop
			}
			if (BAG.getTasksContainer().getShowThumbsThread() != null) {
				BAG.getTasksContainer().getShowThumbsThread().join();
				while (BAG.getTasksContainer().getShowThumbsThread().isAlive()) {
					// wait thread stop
				}
			}
		}
		
		if (BAG.getTasksContainer().getExecPool() != null && !BAG.getTasksContainer().getExecPool().isFinished()) {
			BAG.getTasksContainer().getExecPool().stop();
			if (BAG.getTasksContainer().getExecPoolThread() != null) {
				BAG.getTasksContainer().getExecPoolThread().join();
				while (BAG.getTasksContainer().getExecPoolThread().isAlive()) {
					// wait thread stop
				}
			}
		}
		if (BAG.getTasksContainer().getExecutor() != null) {
			BAG.getTasksContainer().getExecutor().shutdownNow();
			while (!BAG.getTasksContainer().getExecutor().isTerminated()) {
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
		BAG.cleanPagesRotationHashMap();
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
