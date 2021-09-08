package org.gmdev.pdftrick.tasks;

import java.util.concurrent.atomic.AtomicBoolean;

import org.gmdev.pdftrick.manager.*;
import org.gmdev.pdftrick.serviceprocessor.Stoppable;
import org.gmdev.pdftrick.swingmanager.SwingCleaner;
import org.gmdev.pdftrick.utils.*;

import static org.gmdev.pdftrick.serviceprocessor.TaskTerminator.*;

public class CancelTask implements Runnable, Stoppable {
	
	private static final PdfTrickBag bag = PdfTrickBag.INSTANCE;
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
		bag.cleanUp();

		FileUtils.deletePdfFile(bag.getSavedFilePath());
		FileUtils.deleteThumbnailFiles(bag.getThumbnailsFolderPath());

		running.set(false);
	}

	private void terminateTasks() {
		terminateExecutorRunnerTask();
		terminatePdfCoverThumbnailsDisplayTask();
	}

}
