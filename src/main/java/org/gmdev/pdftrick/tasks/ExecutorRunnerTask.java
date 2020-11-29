package org.gmdev.pdftrick.tasks;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

import org.gmdev.pdftrick.manager.PdfTrickBag;

public class ExecutorRunnerTask implements Runnable {
	
	private static final PdfTrickBag BAG = PdfTrickBag.INSTANCE;
	private static final int THREAD_NUMBER = 2;
	
	private final String imagesFolderPath;
	private final int pages;
	private final int divisionResult;
	private final AtomicBoolean running = new AtomicBoolean(false);
	
	public ExecutorRunnerTask(int pages, int divisionResult, String imagesFolderPath) {
		this.pages = pages;
		this.divisionResult = divisionResult;
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
		BAG.getTasksContainer().setExecutorService(executorservice);
		
		int i = divisionResult + 1;
		while (i <= pages && running.get()) {
			Runnable worker = new SecondPdfPageRenderTask(imagesFolderPath, i);
			executorservice.execute(worker);
			i++;
		}

		executorservice.shutdown();
		running.set(false);
	}

}
