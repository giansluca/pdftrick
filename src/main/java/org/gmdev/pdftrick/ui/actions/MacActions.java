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
		if (BAG.getTasksContainer().getFirstPdfPageRenderTask() != null &&
				BAG.getTasksContainer().getFirstPdfPageRenderTask().isRunning()) {

			BAG.getTasksContainer().getFirstPdfPageRenderTask().stop();
			while (BAG.getTasksContainer().getFirstPdfPageRenderTask().isRunning()) {
				// wait thread stop
			}
		}
		
		if (BAG.getTasksContainer().getExecPool() != null &&
				BAG.getTasksContainer().getExecPool().isRunning()) {

			BAG.getTasksContainer().getExecPool().stop();
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
