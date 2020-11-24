package org.gmdev.pdftrick.ui.actions;

import java.awt.event.*;

import javax.swing.*;

import org.gmdev.pdftrick.manager.PdfTrickBag;
import org.gmdev.pdftrick.nativeutil.NativeObjectManager;
import org.gmdev.pdftrick.swingmanager.ModalWarningPanel;
import org.gmdev.pdftrick.utils.*;

public class ExitAction extends AbstractAction {
	
	private static final PdfTrickBag BAG = PdfTrickBag.INSTANCE;
	private static final String ACTION_NAME = "Exit";

	public ExitAction() {
		ImageIcon exitIcon = new ImageIcon(FileLoader.loadFileAsUrl(Constants.EXIT_ICO));
		super.putValue(NAME, ACTION_NAME);
		super.putValue(SMALL_ICON, exitIcon);
		super.putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK));
	}
	
	/**
	 * Called from the EXIT menu, exit the application
	 */
	@Override
	public void actionPerformed(ActionEvent event) {
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
	
	
}
