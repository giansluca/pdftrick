package org.gmdev.pdftrick.ui.actions;

import org.gmdev.pdftrick.manager.*;
import org.gmdev.pdftrick.nativeutil.NativeObjectManager;
import org.gmdev.pdftrick.swingmanager.*;
import org.gmdev.pdftrick.utils.*;

import static org.gmdev.pdftrick.serviceprocessor.TaskTerminator.*;

/**
 * Action called when 'Exit' or 'About' menu item is selected (only for Mac os)
 */
public class MacActions {

	private static final PdfTrickBag BAG = PdfTrickBag.INSTANCE;

	public void handleQuitRequestWith() {
		terminateFirstPdfPageRenderTask();
		terminateExecutorRunnerTask();

		NativeObjectManager nativeManager = BAG.getNativeObjectManager();
		nativeManager.unloadNativeLib();
		
		FileUtils.deletePdfFile(BAG.getPdfFilePath());
		FileUtils.deleteThumbnailFiles(BAG.getThumbnailsFolderPath());
		System.exit(0);
	}

	public void handleAbout() { 
		ModalInfoPanel.displayAboutPanel();
	}

}
