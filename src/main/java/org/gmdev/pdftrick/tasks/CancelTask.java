package org.gmdev.pdftrick.tasks;

import java.util.concurrent.atomic.AtomicBoolean;

import org.gmdev.pdftrick.manager.*;
import org.gmdev.pdftrick.serviceprocessor.Stoppable;
import org.gmdev.pdftrick.swingmanager.SwingCleaner;
import org.gmdev.pdftrick.utils.*;

import static org.gmdev.pdftrick.serviceprocessor.TaskTerminator.*;

public class CancelTask implements Runnable, Stoppable {
	
	private static final PdfTrickBag BAG = PdfTrickBag.INSTANCE;
	private final AtomicBoolean running = new AtomicBoolean(false);

	public void stop() {
	    running.set(false);
	 }

	public boolean isRunning() {
		return running.get();
	}

	@Override
	public void run() {
		running.set(true);

		terminateTasks();
		SwingCleaner.cleanUserInterface();
		cleanUp();

		FileUtils.deletePdfFile(BAG.getPdfFilePath());
		FileUtils.deleteThumbnailFiles(BAG.getThumbnailsFolderPath());

		running.set(false);
	}

	private void cleanUp() {
		BAG.cleanSelectedImagesHashMap();
		BAG.cleanInlineSelectedImagesHashMap();
		BAG.cleanPagesRotationHashMap();
		BAG.setSelectedPage(0);
		BAG.setExtractionFolderPath(null);
		BAG.cleanPdfFilesArray();
	}

	private void terminateTasks() {
		terminateFileChooserTask();
		terminateDragAndDropTask();
		terminateFirstPdfPageRenderTask();
		terminateExecutorRunnerTask();
		terminatePdfCoverThumbnailsDisplayTask();
	}

}
