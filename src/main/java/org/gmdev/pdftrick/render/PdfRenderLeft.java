package org.gmdev.pdftrick.render;

import org.apache.log4j.Logger;
import org.gmdev.pdftrick.manager.PdfTrickBag;
import org.gmdev.pdftrick.thread.DivisionThumb;
import org.gmdev.pdftrick.thread.ExecPool;
import org.gmdev.pdftrick.thread.ShowThumbs;
import org.gmdev.pdftrick.utils.Utils;

import com.itextpdf.text.pdf.PdfReader;

public class PdfRenderLeft {
	
	private static final Logger logger = Logger.getLogger(PdfRenderLeft.class);
	private static final PdfTrickBag BAG = PdfTrickBag.INSTANCE;
	
	public PdfRenderLeft() {
	}

	public void pdfRender() {
		String imgPath = Utils.createImgFolder();
		int totPages = 0;
		
		// get the page number of the new generated pdf
		try {
			PdfReader reader = new PdfReader(BAG.getPdfFilePath());
			totPages = reader.getNumberOfPages();
			BAG.setNumberOfPages(totPages);
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
		BAG.getThreadContainer().setDivisionThumbs(divisionThumbs);
		
		Thread divisionThumbsThread = new Thread(divisionThumbs, "divisionThumbsThread");
		BAG.getThreadContainer().setDivisionThumbsThread(divisionThumbsThread);
		divisionThumbsThread.start();
		
		if (runPool) {
			ExecPool execPool = new ExecPool(totPages, division, imgPath);
			BAG.getThreadContainer().setExecPool(execPool);
			
			Thread execPoolThread = new Thread(execPool, "execPoolThread");
			BAG.getThreadContainer().setExecPoolThread(execPoolThread);
			execPoolThread.start();
		}
		
		// thread that search and showing thumbnails 
		ShowThumbs showThumbs = new ShowThumbs();
		BAG.getThreadContainer().setShowThumbs(showThumbs);
				
		Thread showThumbsThread = new Thread(showThumbs, "showThumbsThread");
		BAG.getThreadContainer().setShowThumbsThread(showThumbsThread);
		showThumbsThread.start();
	}

	
}
