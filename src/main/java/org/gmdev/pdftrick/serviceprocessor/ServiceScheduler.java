package org.gmdev.pdftrick.serviceprocessor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServiceScheduler {
	
	private static final int NUMBER_OF_THREADS = 5;
	private static final ServiceScheduler INSTANCE = new ServiceScheduler(NUMBER_OF_THREADS);

	private final ExecutorService executorService;

	private ServiceScheduler(int numberOfThreads) {
		this.executorService = Executors.newFixedThreadPool(numberOfThreads);
	}

	public static ServiceScheduler getServiceScheduler() {
		return INSTANCE;
	}

	public void schedule(ServiceRequest serviceRequest) {
		Runnable task = () -> {
			try {
				serviceRequest.process();
			} catch (Exception e) {
				throw new IllegalStateException(e);
			}
		};
		executorService.execute(task);
	}
	
	public void shutdown() {
		executorService.shutdown();
	}


}
