package org.gmdev.pdftrick.thread;

import java.lang.reflect.InvocationTargetException;

import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;
import org.gmdev.pdftrick.factory.PdfTrickBag;
import org.gmdev.pdftrick.utils.Messages;
import org.gmdev.pdftrick.utils.Utils;

public class Cancel implements Runnable {
	
	private static final Logger logger = Logger.getLogger(Cancel.class);
	private static final PdfTrickBag factory = PdfTrickBag.getPdfTrickBag();
	
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
		JTextField currentPageField = factory.getUserInterface().getRight().getCurrentPageField();
		JTextField numImgSelectedField = factory.getUserInterface().getRight().getNumImgSelectedField();
		
		if (factory.getThreadContainer().getOpenFileChooser() != null) {
			while (!factory.getThreadContainer().getOpenFileChooser().isFinished()) {
				// wait thread stop
			}
			
			if (factory.getThreadContainer().getOpenFileChooserThread() !=null) {
				factory.getThreadContainer().getOpenFileChooserThread().join();
				
				while (factory.getThreadContainer().getOpenFileChooserThread().isAlive()) {
					// wait thread stop
				}
			}
		}
		
		if (factory.getThreadContainer().getDragAnDropFileChooser() != null) {
			while (!factory.getThreadContainer().getDragAnDropFileChooser().isFinished()) {
				// wait thread stop
			}
			
			if (factory.getThreadContainer().getDragAnDropFileChooserThread() != null) {
				factory.getThreadContainer().getDragAnDropFileChooserThread().join();
				while (factory.getThreadContainer().getDragAnDropFileChooserThread().isAlive()) {
				// wait thread stop
				}
			}
		}
		
		if (factory.getThreadContainer().getDivisionThumbs() != null && !factory.getThreadContainer().getDivisionThumbs().isFinished()) {
			factory.getThreadContainer().getDivisionThumbs().stop();
			while (!factory.getThreadContainer().getDivisionThumbs().isFinished()) {
				// wait thread stop
			}
			if (factory.getThreadContainer().getDivisionThumbsThread() != null) {
				factory.getThreadContainer().getDivisionThumbsThread().join();
				while (factory.getThreadContainer().getDivisionThumbsThread().isAlive()) {
					// wait thread stop
				}
			}
		}
		
		if (factory.getThreadContainer().getShowThumbs() != null && !factory.getThreadContainer().getShowThumbs().isFinished()) {
			factory.getThreadContainer().getShowThumbs().stop();
			while (!factory.getThreadContainer().getShowThumbs().isFinished()) {
				// wait thread stop
			}
			if (factory.getThreadContainer().getShowThumbsThread() != null) {
				factory.getThreadContainer().getShowThumbsThread().join();
				while (factory.getThreadContainer().getShowThumbsThread().isAlive()) {
					// wait thread stop
				}
			}
		}
		
		if (factory.getThreadContainer().getExecPool() != null && !factory.getThreadContainer().getExecPool().isFinished()) {
			factory.getThreadContainer().getExecPool().stop();
			if (factory.getThreadContainer().getExecPoolThread() != null) {
				factory.getThreadContainer().getExecPoolThread().join();
				while (factory.getThreadContainer().getExecPoolThread().isAlive()) {
					// wait thread stop
				}
			}
		}
		if (factory.getThreadContainer().getExecutor() != null) {
			factory.getThreadContainer().getExecutor().shutdownNow();
			while (!factory.getThreadContainer().getExecutor().isTerminated()) {
				//wait stop all threadPool task
			}
		}
		
		try {
			SwingUtilities.invokeAndWait(() -> {
				Utils.cleanLeftPanel();
				Utils.cleanCenterPanel();
				Messages.cleanTextArea();
				currentPageField.setText("");
				numImgSelectedField.setText("");
			});
		} catch (InterruptedException e) {
			logger.error("Exception", e);
		} catch (InvocationTargetException e) {
			logger.error("Exception", e);
		}

		Utils.cleanImageSelectedHashMap();
		Utils.cleanInlineImgSelectedHashMap();
		Utils.cleanRotationFromPagesHashMap();
		factory.setSelected("");
		factory.setFolderToSave("");
		Utils.cleanFilevett();
		
		Utils.deleteResultFile();
		Utils.deleteImgFolderAnDFile();
		finished = true;
	}
	
	public boolean isFinished() {
		return finished;
	}

}
