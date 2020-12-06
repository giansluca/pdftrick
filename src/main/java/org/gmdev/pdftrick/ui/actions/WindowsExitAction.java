package org.gmdev.pdftrick.ui.actions;

import java.awt.event.*;

import javax.swing.*;

import org.gmdev.pdftrick.manager.*;
import org.gmdev.pdftrick.nativeutil.NativeObjectManager;
import org.gmdev.pdftrick.utils.*;

import static org.gmdev.pdftrick.serviceprocessor.TaskTerminator.*;

/**
 * Action called when 'Exit' menu item is selected or CTRL+Q is pressed (only for Windows os)
 */
public class WindowsExitAction extends AbstractAction {
	
	private static final PdfTrickBag BAG = PdfTrickBag.INSTANCE;
	private static final String ACTION_NAME = "Exit";

	public WindowsExitAction() {
		ImageIcon exitIcon = new ImageIcon(FileLoader.loadFileAsUrl(Constants.EXIT_ICO));
		super.putValue(NAME, ACTION_NAME);
		super.putValue(SMALL_ICON, exitIcon);
		super.putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK));
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		terminateFirstPdfPageRenderTask();
		terminateExecutorRunnerTask();
		
		NativeObjectManager nativeManager = BAG.getNativeObjectManager();
		nativeManager.unloadNativeLib();
		
		FileUtils.deletePdfFile(BAG.getPdfFilePath());
		FileUtils.deleteThumbnailFiles(BAG.getThumbnailsFolderPath());
		System.exit(0);
	}
	
	
}
