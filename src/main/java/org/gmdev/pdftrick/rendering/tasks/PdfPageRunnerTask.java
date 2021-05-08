package org.gmdev.pdftrick.rendering.tasks;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

import org.gmdev.pdftrick.manager.PdfTrickBag;
import org.gmdev.pdftrick.serviceprocessor.*;

public class PdfPageRunnerTask implements Runnable, Stoppable {
	
	private static final PdfTrickBag bag = PdfTrickBag.INSTANCE;
	private static final int THREAD_NUMBER = 3;
	
	private final String imagesFolderPath;
	private final int pages;
	private final AtomicBoolean running = new AtomicBoolean(false);
	
	public PdfPageRunnerTask(int pages, String imagesFolderPath) {
		this.pages = pages;
		this.imagesFolderPath = imagesFolderPath;
	}
	
	public void stop() {
		running.set(false);
	}

	public boolean isRunning() {
		return running.get();
	}
	
	@Override
	public void run() {
		running.set(true);
		ExecutorService executorservice = Executors.newFixedThreadPool(THREAD_NUMBER);
		bag.getTasksContainer().setExecutorService(executorservice);

		int i = 1;
		while (i <= pages && running.get()) {
			PdfPageRenderTask worker = new PdfPageRenderTask(imagesFolderPath, i);
			executorservice.execute(worker);
			i++;
		}

		executorservice.shutdown();
		running.set(false);
	}


}
