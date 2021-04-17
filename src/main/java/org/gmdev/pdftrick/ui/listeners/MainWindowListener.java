package org.gmdev.pdftrick.ui.listeners;

import java.awt.event.*;

import org.gmdev.pdftrick.manager.*;
import org.gmdev.pdftrick.nativeutil.NativeObjectManager;
import org.gmdev.pdftrick.utils.FileUtils;

import static org.gmdev.pdftrick.serviceprocessor.TaskTerminator.*;

/**
 * Action called when the main window is closed clicking on the red X
 */
public class MainWindowListener implements WindowListener {
	
	private static final PdfTrickBag bag = PdfTrickBag.INSTANCE;

	@Override
	public void windowClosing(WindowEvent event) {
		terminateFirstPdfPageRenderTask();
		terminateExecutorRunnerTask();

		NativeObjectManager nativeManager = bag.getNativeObjectManager();
		nativeManager.unloadNativeLib();

		FileUtils.deletePdfFile(bag.getSavedFilePath());
		FileUtils.deleteThumbnailFiles(bag.getThumbnailsFolderPath());
		System.exit(0);
	}

	@Override
	public void windowOpened(WindowEvent e) { }

	@Override
	public void windowClosed(WindowEvent e) { }

	@Override
	public void windowIconified(WindowEvent e) { }

	@Override
	public void windowDeiconified(WindowEvent e) { }

	@Override
	public void windowActivated(WindowEvent e) { }

	@Override
	public void windowDeactivated(WindowEvent e) { }
	

}
