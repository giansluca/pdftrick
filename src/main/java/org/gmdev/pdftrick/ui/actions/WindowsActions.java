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
		if (BAG.getTasksContainer().getFirstPdfPageRenderTask() != null &&
				BAG.getTasksContainer().getFirstPdfPageRenderTask().isRunning()) {

			BAG.getTasksContainer().getFirstPdfPageRenderTask().stop();
			while (!BAG.getTasksContainer().getFirstPdfPageRenderTask().isRunning()) {
				// wait thread stop
			}
		}
		
		if (BAG.getTasksContainer().getExecPool() != null &&
				!BAG.getTasksContainer().getExecPool().isRunning()) {

			BAG.getTasksContainer().getExecPool().stop();
		}
		
		if (BAG.getTasksContainer().getImagesExtractionTask() !=null &&
				BAG.getTasksContainer().getImagesExtractionTask().isRunning()) {

			ModalWarningPanel.displayClosingDuringExtractionWarning();
			BAG.getTasksContainer().getImagesExtractionTask().stop();
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
