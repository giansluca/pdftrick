package org.gmdev.pdftrick.ui.actions;

import java.text.MessageFormat;
import java.util.Properties;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import org.gmdev.pdftrick.manager.PdfTrickBag;
import org.gmdev.pdftrick.nativeutil.NativeObjectManager;
import org.gmdev.pdftrick.utils.*;

public class MacActions {

	private static final PdfTrickBag BAG = PdfTrickBag.INSTANCE;
	
	public MacActions() {
	}

	/**
	 * Called on mac OS when the application exit
	 */
	public void handleQuitRequestWith() {
		final Properties messages = BAG.getMessages();
		
		if (BAG.getThreadContainer().getDivisionThumbs() != null && !BAG.getThreadContainer().getDivisionThumbs().isFinished()) {
			BAG.getThreadContainer().getDivisionThumbs().stop();
			while (!BAG.getThreadContainer().getDivisionThumbs().isFinished()) {
				// wait thread stop
			}
			if (BAG.getThreadContainer().getDivisionThumbsThread() != null) {
				while (BAG.getThreadContainer().getDivisionThumbsThread().isAlive()) {
					// wait thread stop
				}
			}
		}
		
		if (BAG.getThreadContainer().getExecPool() != null && !BAG.getThreadContainer().getExecPool().isFinished()) {
			BAG.getThreadContainer().getExecPool().stop();
			if (BAG.getThreadContainer().getExecPoolThread() != null) {
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
		
		if (BAG.getThreadContainer().getImgExtraction() !=null && !BAG.getThreadContainer().getImgExtraction().isFinished()) {
			BAG.getThreadContainer().getImgExtraction().stop();
			if (BAG.getThreadContainer().getImgExtractionThread() !=null && BAG.getThreadContainer().getImgExtractionThread().isAlive()) {
				ImageIcon warningIcon = new ImageIcon(getClass().getResource(Constants.WARNING_ICO));
				Messages.displayMessage(null, messages.getProperty("jmsg_05"), messages.getProperty("jmsg_06"),
						JOptionPane.WARNING_MESSAGE, warningIcon);
			}
		}
		
		NativeObjectManager nativeManager = BAG.getNativeObjectManager();
		nativeManager.unloadNativeLib();
		
		Utils.deletePdfFile();
		Utils.deleteThumbnailsFiles();
		
		System.exit(0);
	}
	
	/**
	 * About menu on OSX
	 */
	public void handleAbout() { 
		Properties messages = BAG.getMessages();
		String os = BAG.getOs();
		ImageIcon imageIcon = new ImageIcon(FileLoader.loadAsUrl(Constants.MAIN_ICO));
		
		Messages.displayMessage(BAG.getUserInterface(), MessageFormat.format(messages.getProperty("dmsg_01_m"), os),
				messages.getProperty("jmsg_07"), 1, imageIcon);
	}


}
