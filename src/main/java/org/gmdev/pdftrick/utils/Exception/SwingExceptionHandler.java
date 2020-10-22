package org.gmdev.pdftrick.utils.Exception;

import org.apache.log4j.Logger;
import org.gmdev.pdftrick.utils.Consts;
import org.gmdev.pdftrick.utils.PdfTrickMessages;

public class SwingExceptionHandler implements Thread.UncaughtExceptionHandler {
	 
	private static final Logger logger = Logger.getLogger(SwingExceptionHandler.class);
		 
	@Override  
	public void uncaughtException(Thread aThread, Throwable e) {
		logger.error(e);
		PdfTrickMessages.append("ERROR", Consts.SEND_LOG_MSG);
	}
}
