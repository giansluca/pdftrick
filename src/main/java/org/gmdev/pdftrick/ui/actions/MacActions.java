package org.gmdev.pdftrick.ui.actions;

import org.gmdev.pdftrick.manager.*;
import org.gmdev.pdftrick.nativeutil.NativeObjectManager;
import org.gmdev.pdftrick.swingmanager.*;
import org.gmdev.pdftrick.utils.*;

import static org.gmdev.pdftrick.serviceprocessor.TaskTerminator.*;

/**
 * 'Exit' or 'About' menu item actions (only for Mac os)
 */
public class MacActions {

	private static final PdfTrickBag bag = PdfTrickBag.INSTANCE;

	public void handleQuitRequestWith() {
		terminateFirstPdfPageRenderTask();
		terminateExecutorRunnerTask();

		NativeObjectManager nativeManager = bag.getNativeObjectManager();
		nativeManager.unloadNativeLib();
		
		FileUtils.deletePdfFile(bag.getSavedFilePath());
		FileUtils.deleteThumbnailFiles(bag.getThumbnailsFolderPath());
		System.exit(0);
	}

	public void handleAbout() { 
		ModalInfoPanel.displayAboutPanel();
	}

}
