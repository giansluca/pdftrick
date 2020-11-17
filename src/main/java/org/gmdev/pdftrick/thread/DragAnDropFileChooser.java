package org.gmdev.pdftrick.thread;

import java.io.File;
import java.util.*;

import javax.swing.*;

import org.gmdev.pdftrick.engine.*;
import org.gmdev.pdftrick.manager.PdfTrickBag;
import org.gmdev.pdftrick.render.PdfRenderLeft;
import org.gmdev.pdftrick.ui.panels.CenterPanel;
import org.gmdev.pdftrick.ui.panels.LeftPanel;
import org.gmdev.pdftrick.utils.*;

public class DragAnDropFileChooser implements Runnable {
	
	private static final PdfTrickBag BAG = PdfTrickBag.INSTANCE;
	
	private final File[] fileDrop;
	volatile boolean finished = false;
	
	public DragAnDropFileChooser(File[] fileDrop) {
		this.fileDrop = fileDrop;
	}
	
	public void stop() {
	    finished = true;
	 }
	
	@Override
	public void run() {
		execute();
	}
	
	/**
	 * Called after pdf file dropped in  
	 */
	public void execute() {
		JTextField currentPageField = BAG.getUserInterface().getRight().getCurrentPageField();
		JTextField numImgSelectedField = BAG.getUserInterface().getRight().getNumImgSelectedField();
		CenterPanel centerPanel = BAG.getUserInterface().getCenter();
		LeftPanel leftPanel = BAG.getUserInterface().getLeft();
		ArrayList<File> filesVett = BAG.getPdfFilesArray();
		Properties messages = BAG.getMessagesProps();

    	SwingUtilities.invokeLater(() -> {
			leftPanel.clean();
			centerPanel.clean();
			Messages.cleanTextArea();
			currentPageField.setText("");
			numImgSelectedField.setText("");
			centerPanel.startWaitIconLoadPdf();
		});
    	
    	// clean up
    	BAG.setSelectedPage(0);
    	BAG.setExtractionFolderPath(null);
    	BAG.cleanPdfFilesArray();
    	BAG.cleanSelectedImagesHashMap();
    	BAG.cleanInlineSelectedImagesHashMap();
    	BAG.cleanRotationFromPagesHashMap();
    		
    	FileUtils.deleteThumbnailFiles(BAG.getThumbnailsFolderPath());
    	FileUtils.deletePdfFile(BAG.getPdfFilePath());
    	
		for (int i = 0; i < fileDrop.length; i++) {
			File item = fileDrop[i];
			if (!item.isDirectory()) {
				filesVett.add(item);
			}
		}
    	
		boolean fileCheck = false;
        CheckFiles checkFiles = new CheckFiles();
        
        if (filesVett.size() > 0) {
        	fileCheck = checkFiles.check();
        	
        	if (!fileCheck) {
        		// in case of check failed i clean panel left and center, other stuff 
        		// (vector, hasmap, resultpdf was cleaned on approve open filechooser)
        		Messages.append("WARNING", messages.getProperty("tmsg_11"));
        		SwingUtilities.invokeLater(() -> {
					leftPanel.clean();
					centerPanel.clean();
				});
        	} else {
        		// merge pdf selection after check
        		MergeFiles mergeFiles = new MergeFiles();
            	File resultFile = mergeFiles.mergePdf(filesVett, BAG.getPdfFilePath());
            	
            	if (resultFile != null && resultFile.exists() && resultFile.length() > 0) {
            		Messages.append("INFO", messages.getProperty("tmsg_12"));
            		PdfRenderLeft render = new PdfRenderLeft();
            		render.pdfRender();
            	} else {
            		Messages.append("WARNING", messages.getProperty("tmsg_13"));
            	}
        	}
        } else {
        	SwingUtilities.invokeLater(() -> {
				BAG.getUserInterface().getCenter().stopWaitIcon();
				centerPanel.clean();
			});
        	Messages.append("WARNING", messages.getProperty("tmsg_14"));
        }
        
        finished = true;
	}

	public boolean isFinished() {
		return finished;
	}
	
	public void setFinished(boolean finished) {
		this.finished = finished;
	}


}
