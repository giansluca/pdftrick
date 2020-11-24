package org.gmdev.pdftrick.ui.actions;

import java.awt.event.*;

import org.gmdev.pdftrick.manager.PdfTrickBag;
import org.gmdev.pdftrick.swingmanager.ModalWarningPanel;
import org.gmdev.pdftrick.nativeutil.NativeObjectManager;
import org.gmdev.pdftrick.utils.FileUtils;

public class WindowsActions implements WindowListener {
	
	private static final PdfTrickBag BAG = PdfTrickBag.INSTANCE;
	
	@Override
	public void windowOpened(WindowEvent e) {
	}
	
	/**
	 * Called on closing window frame, exit application
	 */
	@Override
	public void windowClosing(WindowEvent event) {
		if (BAG.getTasksContainer().getDivisionThumbs() != null &&
				!BAG.getTasksContainer().getDivisionThumbs().isFinished()) {

			BAG.getTasksContainer().getDivisionThumbs().stop();
			while (!BAG.getTasksContainer().getDivisionThumbs().isFinished()) {
				// wait thread stop
			}
			
			if (BAG.getTasksContainer().getDivisionThumbsThread() != null) {
				while (BAG.getTasksContainer().getDivisionThumbsThread().isAlive()) {
					// wait thread stop
				}
			}
		}
		
		if (BAG.getTasksContainer().getExecPool() != null &&
				!BAG.getTasksContainer().getExecPool().isFinished()) {

			BAG.getTasksContainer().getExecPool().stop();
			if (BAG.getTasksContainer().getExecPoolThread() != null) {
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
		
		if (BAG.getTasksContainer().getImgExtraction() !=null &&
				!BAG.getTasksContainer().getImgExtraction().isFinished()) {

			BAG.getTasksContainer().getImgExtraction().stop();
			if (BAG.getTasksContainer().getImgExtractionThread() !=null &&
					BAG.getTasksContainer().getImgExtractionThread() .isAlive()) {
				ModalWarningPanel.displayClosingDuringExtractionWarning();
			}
		}
		
		NativeObjectManager nativeManager = BAG.getNativeObjectManager();
		nativeManager.unloadNativeLib();

		FileUtils.deletePdfFile(BAG.getPdfFilePath());
		FileUtils.deleteThumbnailFiles(BAG.getThumbnailsFolderPath());
		
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
