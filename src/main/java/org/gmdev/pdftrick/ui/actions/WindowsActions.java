package org.gmdev.pdftrick.ui.actions;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Properties;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import org.gmdev.pdftrick.factory.PdfTrickBag;
import org.gmdev.pdftrick.utils.Constants;
import org.gmdev.pdftrick.utils.NativeObjectManager;
import org.gmdev.pdftrick.utils.PdfTrickMessages;
import org.gmdev.pdftrick.utils.PdfTrickUtils;

public class WindowsActions implements WindowListener {
	
	private static final PdfTrickBag factory = PdfTrickBag.getPdfTrickBag();
	
	public WindowsActions() {
	}
	
	@Override
	public void windowOpened(WindowEvent e) {
	}
	
	/**
	 * Called on closing window frame, exit application
	 */
	@Override
	public void windowClosing(WindowEvent e) {
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
			if (factory.gettContainer().getImgExtractionThread() !=null && factory.gettContainer().getImgExtractionThread() .isAlive()) {
				ImageIcon warningIcon = new ImageIcon(getClass().getResource(Constants.WARNING_ICO));
				PdfTrickMessages.displayMessage(null, messages.getProperty("jmsg_05"), messages.getProperty("jmsg_06"),
						JOptionPane.WARNING_MESSAGE, warningIcon);
			}
		}
		
		NativeObjectManager nativeManager = factory.getNativemanager();
		nativeManager.unloadNativeLib();
		PdfTrickUtils.deleteResultFile();
		PdfTrickUtils.deleteImgFolderAnDFile();
		
		System.exit(0);	
	}

	@Override
	public void windowClosed(WindowEvent e) {
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}
	

}
