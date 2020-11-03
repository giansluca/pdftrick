package org.gmdev.pdftrick.ui.actions;

import java.text.MessageFormat;
import java.util.Properties;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import org.gmdev.pdftrick.factory.PdfTrickBag;
import org.gmdev.pdftrick.nativeutil.NativeObjectManager;
import org.gmdev.pdftrick.utils.*;

public class MacActions {

	private static final PdfTrickBag factory = PdfTrickBag.getPdfTrickBag();
	
	public MacActions() {
	}

	/**
	 * Called on mac OS when the application exit
	 */
	public void handleQuitRequestWith() {
		final Properties messages = factory.getMessages();
		
		if (factory.getThreadContainer().getDivisionThumbs() != null && !factory.getThreadContainer().getDivisionThumbs().isFinished()) {
			factory.getThreadContainer().getDivisionThumbs().stop();
			while (!factory.getThreadContainer().getDivisionThumbs().isFinished()) {
				// wait thread stop
			}
			if (factory.getThreadContainer().getDivisionThumbsThread() != null) {
				while (factory.getThreadContainer().getDivisionThumbsThread().isAlive()) {
					// wait thread stop
				}
			}
		}
		
		if (factory.getThreadContainer().getExecPool() != null && !factory.getThreadContainer().getExecPool().isFinished()) {
			factory.getThreadContainer().getExecPool().stop();
			if (factory.getThreadContainer().getExecPoolThread() != null) {
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
		
		if (factory.getThreadContainer().getImgExtraction() !=null && !factory.getThreadContainer().getImgExtraction().isFinished()) {
			factory.getThreadContainer().getImgExtraction().stop();
			if (factory.getThreadContainer().getImgExtractionThread() !=null && factory.getThreadContainer().getImgExtractionThread().isAlive()) {
				ImageIcon warningIcon = new ImageIcon(getClass().getResource(Constants.WARNING_ICO));
				Messages.displayMessage(null, messages.getProperty("jmsg_05"), messages.getProperty("jmsg_06"),
						JOptionPane.WARNING_MESSAGE, warningIcon);
			}
		}
		
		NativeObjectManager nativeManager = factory.getNativeManager();
		nativeManager.unloadNativeLib();
		
		Utils.deleteResultFile();
		Utils.deleteImgFolderAnDFile();
		
		System.exit(0);
	}
	
	/**
	 * About menu on OSX
	 */
	public void handleAbout() { 
		Properties messages = factory.getMessages();
		String os = factory.getOs();
		ImageIcon imageIcon = new ImageIcon(FileLoader.loadAsUrl(Constants.MAIN_ICO));
		
		Messages.displayMessage(factory.getUserInterface(), MessageFormat.format(messages.getProperty("dmsg_01_m"), os),
				messages.getProperty("jmsg_07"), 1, imageIcon);
	}


}
