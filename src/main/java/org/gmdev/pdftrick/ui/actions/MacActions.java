package org.gmdev.pdftrick.ui.actions;

import org.gmdev.pdftrick.manager.*;
import org.gmdev.pdftrick.nativeutil.NativeObjectManager;
import org.gmdev.pdftrick.swingmanager.*;
import org.gmdev.pdftrick.tasks.*;
import org.gmdev.pdftrick.utils.*;

import static org.gmdev.pdftrick.utils.Constants.TEN;
import static org.gmdev.pdftrick.utils.ThreadUtils.pause;

public class MacActions {

	private static final PdfTrickBag BAG = PdfTrickBag.INSTANCE;

	public void handleQuitRequestWith() {
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

	public void handleAbout() { 
		ModalInfoPanel.displayAboutPanel();
	}

}
