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
		if (BAG.getThreadContainer().getDivisionThumbs() != null &&
				!BAG.getThreadContainer().getDivisionThumbs().isFinished()) {

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
		
		if (BAG.getThreadContainer().getExecPool() != null &&
				!BAG.getThreadContainer().getExecPool().isFinished()) {

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
		
		if (BAG.getThreadContainer().getImgExtraction() !=null &&
				!BAG.getThreadContainer().getImgExtraction().isFinished()) {

			BAG.getThreadContainer().getImgExtraction().stop();
			if (BAG.getThreadContainer().getImgExtractionThread() !=null &&
					BAG.getThreadContainer().getImgExtractionThread().isAlive()) {
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
