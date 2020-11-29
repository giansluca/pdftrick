package org.gmdev.pdftrick.ui.actions;

import java.awt.event.*;

import org.gmdev.pdftrick.manager.*;
import org.gmdev.pdftrick.swingmanager.ModalWarningPanel;
import org.gmdev.pdftrick.nativeutil.NativeObjectManager;
import org.gmdev.pdftrick.tasks.*;
import org.gmdev.pdftrick.utils.FileUtils;

import static org.gmdev.pdftrick.utils.Constants.TEN;
import static org.gmdev.pdftrick.utils.ThreadUtils.pause;

public class WindowsActions implements WindowListener {
	
	private static final PdfTrickBag BAG = PdfTrickBag.INSTANCE;
	
	@Override
	public void windowOpened(WindowEvent e) {
	}

	@Override
	public void windowClosing(WindowEvent event) {
		TasksContainer tasksContainer = BAG.getTasksContainer();

		var firstPdfPageRenderTask = tasksContainer.getFirstPdfPageRenderTask();
		if (firstPdfPageRenderTask != null && firstPdfPageRenderTask.isRunning()) {
			firstPdfPageRenderTask.stop();
			while (firstPdfPageRenderTask.isRunning())
				pause(TEN);
		}

		ExecutorRunnerTask executorRunnerTask = tasksContainer.getExecutorRunnerTask();
		if (executorRunnerTask != null && executorRunnerTask.isRunning())
			executorRunnerTask.stop();

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
