package org.gmdev.pdftrick.ui.actions;

import java.awt.event.*;

import javax.swing.*;

import org.gmdev.pdftrick.manager.PdfTrickBag;
import org.gmdev.pdftrick.manager.TasksContainer;
import org.gmdev.pdftrick.nativeutil.NativeObjectManager;
import org.gmdev.pdftrick.swingmanager.ModalWarningPanel;
import org.gmdev.pdftrick.tasks.ExecPool;
import org.gmdev.pdftrick.tasks.ImagesExtractionTask;
import org.gmdev.pdftrick.utils.*;

import static org.gmdev.pdftrick.utils.Constants.TEN;
import static org.gmdev.pdftrick.utils.ThreadUtils.pause;

public class ExitAction extends AbstractAction {
	
	private static final PdfTrickBag BAG = PdfTrickBag.INSTANCE;
	private static final String ACTION_NAME = "Exit";

	public ExitAction() {
		ImageIcon exitIcon = new ImageIcon(FileLoader.loadFileAsUrl(Constants.EXIT_ICO));
		super.putValue(NAME, ACTION_NAME);
		super.putValue(SMALL_ICON, exitIcon);
		super.putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK));
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		TasksContainer tasksContainer = BAG.getTasksContainer();

		var firstPdfPageRenderTask = tasksContainer.getFirstPdfPageRenderTask();
		if (firstPdfPageRenderTask != null && firstPdfPageRenderTask.isRunning()) {
			firstPdfPageRenderTask.stop();
			while (firstPdfPageRenderTask.isRunning())
				pause(TEN);
		}

		ExecPool execPool = tasksContainer.getExecPool();
		if (execPool != null && execPool.isRunning()) {
			execPool.stop();
		}

		ImagesExtractionTask imagesExtractionTask = tasksContainer.getImagesExtractionTask();
		if (imagesExtractionTask !=null && imagesExtractionTask.isRunning()) {
			ModalWarningPanel.displayClosingDuringExtractionWarning();
			imagesExtractionTask.stop();
		}
		
		NativeObjectManager nativeManager = BAG.getNativeObjectManager();
		nativeManager.unloadNativeLib();
		
		FileUtils.deletePdfFile(BAG.getPdfFilePath());
		FileUtils.deleteThumbnailFiles(BAG.getThumbnailsFolderPath());
		
		System.exit(0);
	}
	
	
}
