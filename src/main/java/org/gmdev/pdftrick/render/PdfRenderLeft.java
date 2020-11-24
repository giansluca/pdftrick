package org.gmdev.pdftrick.render;

import org.apache.log4j.Logger;
import org.gmdev.pdftrick.manager.PdfTrickBag;
import org.gmdev.pdftrick.thread.DivisionThumb;
import org.gmdev.pdftrick.thread.ExecPool;
import org.gmdev.pdftrick.thread.ShowPdfCoverThumbnailsTask;

import com.itextpdf.text.pdf.PdfReader;

import java.io.File;

public class PdfRenderLeft {
	
	private static final Logger logger = Logger.getLogger(PdfRenderLeft.class);
	private static final PdfTrickBag BAG = PdfTrickBag.INSTANCE;
	
	public PdfRenderLeft() {
	}

	public void pdfRender() {
		String imgPath = BAG.getThumbnailsFolderPath() + File.separator;
		int totPages = 0;
		
		// get the page number of the new generated pdf
		try {
			PdfReader reader = new PdfReader(BAG.getPdfFilePath().toString());
			totPages = reader.getNumberOfPages();
			BAG.setNumberOfPages(totPages);
			reader.close();
		} catch (Exception e) {
			logger.error("Exception", e);
		}
		
		// call native function and rendering pdf cover in png images
		boolean runPool = true;
		int division = totPages / 3;
		
		if (totPages < 3) {
			runPool = false;
			division = totPages;
		}
		
		DivisionThumb divisionThumbs = new DivisionThumb(division, imgPath);
		BAG.getTasksContainer().setDivisionThumbs(divisionThumbs);
		
		Thread divisionThumbsThread = new Thread(divisionThumbs, "divisionThumbsThread");
		BAG.getTasksContainer().setDivisionThumbsThread(divisionThumbsThread);
		divisionThumbsThread.start();
		
		if (runPool) {
			ExecPool execPool = new ExecPool(totPages, division, imgPath);
			BAG.getTasksContainer().setExecPool(execPool);
			
			Thread execPoolThread = new Thread(execPool, "execPoolThread");
			BAG.getTasksContainer().setExecPoolThread(execPoolThread);
			execPoolThread.start();
		}
		
		// thread that search and showing thumbnails 
		ShowPdfCoverThumbnailsTask showPdfCoverThumbnailsTask = new ShowPdfCoverThumbnailsTask();
		BAG.getTasksContainer().setShowPdfCoverThumbnailsTask(showPdfCoverThumbnailsTask);
				
		Thread showThumbsThread = new Thread(showPdfCoverThumbnailsTask, "showThumbsThread");
		BAG.getTasksContainer().setShowThumbsThread(showThumbsThread);
		showThumbsThread.start();
	}

	
}
