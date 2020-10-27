package org.gmdev.pdftrick.utils.exception;

import org.apache.log4j.Logger;
import org.gmdev.pdftrick.utils.Constants;
import org.gmdev.pdftrick.utils.PdfTrickMessages;

public class DefaultHandler implements Thread.UncaughtExceptionHandler {
	 
	private static final Logger logger = Logger.getLogger(DefaultHandler.class);
		 
	@Override  
	public void uncaughtException(Thread aThread, Throwable e) {
		logger.error(e);
		PdfTrickMessages.append("ERROR", Constants.SEND_LOG_MSG);
	}
}
