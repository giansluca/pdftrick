package org.gmdev.pdftrick.ui.actions;

import org.gmdev.pdftrick.manager.PdfTrickBag;
import org.gmdev.pdftrick.nativeutil.NativeObjectManager;
import org.gmdev.pdftrick.swingmanager.*;
import org.gmdev.pdftrick.utils.*;

public class MacActions {

	private static final PdfTrickBag BAG = PdfTrickBag.INSTANCE;

	/**
	 * Called on mac OS when the application exit
	 */
	public void handleQuitRequestWith() {
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
		
		if (BAG.getTasksContainer().getImagesExtractionTask() !=null &&
				BAG.getTasksContainer().getImagesExtractionTask().isRunning()) {

			BAG.getTasksContainer().getImagesExtractionTask().stop();
			if (BAG.getTasksContainer().getImgExtractionThread() !=null &&
					BAG.getTasksContainer().getImgExtractionThread().isAlive()) {
				ModalWarningPanel.displayClosingDuringExtractionWarning();
			}
		}
		
		NativeObjectManager nativeManager = BAG.getNativeObjectManager();
		nativeManager.unloadNativeLib();
		
		FileUtils.deletePdfFile(BAG.getPdfFilePath());
		FileUtils.deleteThumbnailFiles(BAG.getThumbnailsFolderPath());
		System.exit(0);
	}
	
	/**
	 * About menu Mac
	 */
	public void handleAbout() { 
		ModalInfoPanel.displayAboutPanel();
	}


}
