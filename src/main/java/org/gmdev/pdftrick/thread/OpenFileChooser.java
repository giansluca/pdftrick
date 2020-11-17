package org.gmdev.pdftrick.thread;

import java.io.File;
import java.util.ArrayList;
import java.util.Properties;

import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.gmdev.pdftrick.engine.CheckFiles;
import org.gmdev.pdftrick.engine.MergeFiles;
import org.gmdev.pdftrick.manager.PdfTrickBag;
import org.gmdev.pdftrick.render.PdfRenderLeft;
import org.gmdev.pdftrick.ui.panels.CenterPanel;
import org.gmdev.pdftrick.ui.panels.LeftPanel;
import org.gmdev.pdftrick.utils.FileUtils;
import org.gmdev.pdftrick.utils.Messages;

public class OpenFileChooser implements Runnable {
	
	private static final PdfTrickBag BAG = PdfTrickBag.INSTANCE;
	
	private final File[] files;
	volatile boolean finished = false;
	
	public OpenFileChooser(File[] files) {
		this.files = files;
	}
	
	public void stop() {
	    finished = true;
	 }
	
	@Override
	public void run() {
		execute();
	}
	
	/**
	 * Called after choosing pdf file upload  
	 */
	public void execute() {
		Properties messages = BAG.getMessagesProps();
		JTextField currentPageField = BAG.getUserInterface().getRight().getCurrentPageField();
		JTextField numImgSelectedField = BAG.getUserInterface().getRight().getNumImgSelectedField();
		CenterPanel centerPanel = BAG.getUserInterface().getCenter();
		LeftPanel leftPanel = BAG.getUserInterface().getLeft();
		ArrayList<File> filesVett = BAG.getPdfFilesArray();
		
        SwingUtilities.invokeLater(() -> {
			leftPanel.clean();
			centerPanel.clean();
			Messages.cleanTextArea();
			currentPageField.setText("");
			numImgSelectedField.setText("");
			BAG.getUserInterface().getCenter().startWaitIconLoadPdf();
		});
        		
        // clean up
        BAG.setSelected("");
        BAG.setExtractionFolderPath(null);
        BAG.cleanPdfFilesArray();
        BAG.cleanImageSelectedHashMap();
        BAG.cleanInlineImgSelectedHashMap();
        BAG.cleanRotationFromPagesHashMap();
        		
        FileUtils.deleteThumbnailFiles(BAG.getThumbnailsFolderPath());
        FileUtils.deletePdfFile(BAG.getPdfFilePath());
     	
		for (int i = 0; i < files.length; i++) {
			File item = files[i];
			if (!item.isDirectory()) {
				filesVett.add(item);
			}
		}
        
        // call check class control files after selection
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
        		MergeFiles engine = new MergeFiles();
        		File resultFile = engine.mergePdf(filesVett, BAG.getPdfFilePath());
        		
        		if (resultFile != null && resultFile.exists() && resultFile.length() > 0) {
        			Messages.append("INFO", messages.getProperty("tmsg_12"));
        			PdfRenderLeft render = new PdfRenderLeft();
        			render.pdfRender();
        		} else {
        			Messages.append("WARNING", messages.getProperty("tmsg_13"));
        		}
        	}
        } 
        
        finished = true;
	}
	
	public boolean isFinished() {
		return finished;
	}
	

}
