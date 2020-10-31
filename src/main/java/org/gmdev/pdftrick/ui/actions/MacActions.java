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
		
		if (factory.gettContainer().getDivisionThumbs() != null && !factory.gettContainer().getDivisionThumbs().isFinished()) {
			factory.gettContainer().getDivisionThumbs().stop();
			while (!factory.gettContainer().getDivisionThumbs().isFinished()) {
				// wait thread stop
			}
			if (factory.gettContainer().getDivisionThumbsThread() != null) {
				while (factory.gettContainer().getDivisionThumbsThread().isAlive()) {
					// wait thread stop
				}
			}
		}
		
		if (factory.gettContainer().getExecPool() != null && !factory.gettContainer().getExecPool().isFinished()) {
			factory.gettContainer().getExecPool().stop();
			if (factory.gettContainer().getExecPoolThread() != null) {
				while (factory.gettContainer().getExecPoolThread().isAlive()) {
					// wait thread stop
				}
			}
		}
		if (factory.gettContainer().getExecutor() != null) {
			factory.gettContainer().getExecutor().shutdownNow();
			while (!factory.gettContainer().getExecutor().isTerminated()) {
				//wait stop all threadPool task
			}
		}
		
		if (factory.gettContainer().getImgExtraction() !=null && !factory.gettContainer().getImgExtraction().isFinished()) {
			factory.gettContainer().getImgExtraction().stop();
			if (factory.gettContainer().getImgExtractionThread() !=null && factory.gettContainer().getImgExtractionThread().isAlive()) {
				ImageIcon warningIcon = new ImageIcon(getClass().getResource(Constants.WARNING_ICO));
				Messages.displayMessage(null, messages.getProperty("jmsg_05"), messages.getProperty("jmsg_06"),
						JOptionPane.WARNING_MESSAGE, warningIcon);
			}
		}
		
		NativeObjectManager nativeManager = factory.getNativemanager();
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
