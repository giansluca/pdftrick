package org.gmdev.pdftrick.exception;

import org.apache.log4j.Logger;

public class DefaultHandler implements Thread.UncaughtExceptionHandler {
	 
	private static final Logger logger = Logger.getLogger(DefaultHandler.class);
		 
	@Override  
	public void uncaughtException(Thread aThread, Throwable e) {
		logger.error(e);
	}
}
