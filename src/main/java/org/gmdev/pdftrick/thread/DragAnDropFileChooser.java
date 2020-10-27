package org.gmdev.pdftrick.thread;

import java.io.File;
import java.util.ArrayList;
import java.util.Properties;

import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.gmdev.pdftrick.engine.CheckFiles;
import org.gmdev.pdftrick.engine.MergeFiles;
import org.gmdev.pdftrick.factory.PdfTrickBag;
import org.gmdev.pdftrick.render.PdfRenderLeft;
import org.gmdev.pdftrick.utils.PdfTrickMessages;
import org.gmdev.pdftrick.utils.PdfTrickUtils;

public class DragAnDropFileChooser implements Runnable {
	
	private static final PdfTrickBag factory = PdfTrickBag.getPdfTrickBag();
	
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
		final Properties messages = factory.getMessages();
		final JTextField currentPageField = factory.getUserInterface().getRight().getCurrentPageField();
		final JTextField numImgSelectedField = factory.getUserInterface().getRight().getNumImgSelectedField();
		final ArrayList<File> filesVett = factory.getFilesVett();
		
    	SwingUtilities.invokeLater(new Runnable() {
    		@Override
			public void run() {
				PdfTrickUtils.cleanLeftPanel();
				PdfTrickUtils.cleanCenterPanel();
				PdfTrickMessages.cleanTextArea();
				currentPageField.setText("");
				numImgSelectedField.setText("");
				PdfTrickUtils.startWaitIconLoadPdf();
			}
		});
    	
    	// clean areatext fileVett resultFile imageSelected imageselected in case of reopen jfilechooser 
    	factory.setSelected("");
    	factory.setFolderToSave("");
    	PdfTrickUtils.cleanFilevett();
    	PdfTrickUtils.cleanImageSelectedHashMap();
    	PdfTrickUtils.cleanInlineImgSelectedHashMap();
    	PdfTrickUtils.cleanRotationFromPagesHashMap();
    		
    	PdfTrickUtils.deleteImgFolderAnDFile();
    	PdfTrickUtils.deleteResultFile();
    	System.gc();
    	
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
        		PdfTrickMessages.append("WARNING", messages.getProperty("tmsg_11"));
        		SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						PdfTrickUtils.cleanLeftPanel();
						PdfTrickUtils.cleanCenterPanel();
					}
				});
        	} else {
        		// merge pdf selection after check
        		MergeFiles engine = new MergeFiles();
            	File resultFile = engine.mergePdf(filesVett, factory.getResultFile());
            	
            	if (resultFile != null && resultFile.exists() && resultFile.length() > 0) {
            		PdfTrickMessages.append("INFO", messages.getProperty("tmsg_12"));
            		PdfRenderLeft render = new PdfRenderLeft();
            		render.pdfRender();
            	} else {
            		PdfTrickMessages.append("WARNING", messages.getProperty("tmsg_13"));	
            	}
        	}
        } else {
        	SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					PdfTrickUtils.stopWaitIcon();
					PdfTrickUtils.cleanCenterPanel();
				}
			});
        	PdfTrickMessages.append("WARNING", messages.getProperty("tmsg_14"));
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
