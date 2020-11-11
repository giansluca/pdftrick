package org.gmdev.pdftrick.ui.actions;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Properties;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import org.gmdev.pdftrick.manager.PdfTrickBag;
import org.gmdev.pdftrick.utils.Constants;
import org.gmdev.pdftrick.nativeutil.NativeObjectManager;
import org.gmdev.pdftrick.utils.Messages;
import org.gmdev.pdftrick.utils.Utils;

public class WindowsActions implements WindowListener {
	
	private static final PdfTrickBag BAG = PdfTrickBag.INSTANCE;
	
	public WindowsActions() {
	}
	
	@Override
	public void windowOpened(WindowEvent e) {
	}
	
	/**
	 * Called on closing window frame, exit application
	 */
	@Override
	public void windowClosing(WindowEvent event) {
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
			if (BAG.getThreadContainer().getImgExtractionThread() !=null && BAG.getThreadContainer().getImgExtractionThread() .isAlive()) {
				ImageIcon warningIcon = new ImageIcon(getClass().getResource(Constants.WARNING_ICO));
				Messages.displayMessage(null, messages.getProperty("jmsg_05"), messages.getProperty("jmsg_06"),
						JOptionPane.WARNING_MESSAGE, warningIcon);
			}
		}
		
		NativeObjectManager nativeManager = BAG.getNativeObjectManager();
		nativeManager.unloadNativeLib();

		Utils.deletePdfFile(BAG.getPdfFilePath());
		Utils.deleteThumbnailFiles(BAG.getThumbnailsFolderPath());
		
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
