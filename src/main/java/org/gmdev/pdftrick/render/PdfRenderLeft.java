package org.gmdev.pdftrick.render;

import org.apache.log4j.Logger;
import org.gmdev.pdftrick.factory.PdfTrickBag;
import org.gmdev.pdftrick.thread.DivisionThumb;
import org.gmdev.pdftrick.thread.ExecPool;
import org.gmdev.pdftrick.thread.ShowThumbs;
import org.gmdev.pdftrick.utils.Utils;

import com.itextpdf.text.pdf.PdfReader;

public class PdfRenderLeft {
	
	private static final Logger logger = Logger.getLogger(PdfRenderLeft.class);
	private static final PdfTrickBag factory = PdfTrickBag.getPdfTrickBag();
	
	public PdfRenderLeft() {
	}
	
	/**
	 * Render pdf resultfile thumbs in a left_panel, using threads pool
	 */
	public void pdfRender() {
		String imgPath = Utils.createImgFolder();
		int totPages = 0;
		
		// get the page number of the new generated pdf
		try {
			PdfReader reader = new PdfReader(factory.getResultFile());
			totPages = reader.getNumberOfPages();
			factory.setNumberOfPages(totPages);
			reader.close();
		} catch (Exception e) {
			logger.error("Exception", e);
		}
		
		// system of thread that call native function and renderizing pdf cover in png images 
		boolean runPool = true;
		int division = totPages / 3;
		
		if (totPages < 3) {
			runPool = false;
			division = totPages;
		}
		
		DivisionThumb divisionThumbs = new DivisionThumb(division, imgPath);
		factory.getThreadContainer().setDivisionThumbs(divisionThumbs);
		
		Thread divisionThumbsThread = new Thread(divisionThumbs, "divisionThumbsThread");
		factory.getThreadContainer().setDivisionThumbsThread(divisionThumbsThread);
		divisionThumbsThread.start();
		
		if (runPool) {
			ExecPool execPool = new ExecPool(totPages, division, imgPath);
			factory.getThreadContainer().setExecPool(execPool);
			
			Thread execPoolThread = new Thread(execPool, "execPoolThread");
			factory.getThreadContainer().setExecPoolThread(execPoolThread);
			execPoolThread.start();
		}
		
		// thread that search and showing thumbnails 
		ShowThumbs showThumbs = new ShowThumbs();
		factory.getThreadContainer().setShowThumbs(showThumbs);
				
		Thread showThumbsThread = new Thread(showThumbs, "showThumbsThread");
		factory.getThreadContainer().setShowThumbsThread(showThumbsThread);
		showThumbsThread.start();
	}

	
}
