package org.gmdev.pdftrick.tasks;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

import org.gmdev.pdftrick.manager.PdfTrickBag;

public class ExecPool implements Runnable {
	
	private static final PdfTrickBag BAG = PdfTrickBag.INSTANCE;
	private static final int THREADS_NUMBER = 2;
	
	private final String imgPath;
	private final int numPages;
	private final int division;
	private final AtomicBoolean running = new AtomicBoolean(false);
	
	public ExecPool(int totPages, int division, String imgPath) {
		this.imgPath = imgPath;
		this.numPages = totPages;
		this.division = division;
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

		ExecutorService executor = Executors.newFixedThreadPool(THREADS_NUMBER);
		BAG.getTasksContainer().setExecutor(executor);
		
		int i = division + 1;
		
		while (i <= numPages && running.get()) {
			Runnable worker = new PageThumb(imgPath, i);
			executor.execute(worker);
			i++;
		}
		
		executor.shutdown();
		running.set(false);
	}

}
