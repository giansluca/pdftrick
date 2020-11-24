package org.gmdev.pdftrick.tasks;

import java.io.File;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.gmdev.pdftrick.engine.*;
import org.gmdev.pdftrick.manager.PdfTrickBag;
import org.gmdev.pdftrick.render.PdfRenderLeft;
import org.gmdev.pdftrick.ui.panels.*;
import org.gmdev.pdftrick.utils.*;

public class FileChooserTask implements Runnable {
	
	private static final PdfTrickBag BAG = PdfTrickBag.INSTANCE;
	
	private final File[] openedFiles;
	private final AtomicBoolean running = new AtomicBoolean(false);
	
	public FileChooserTask(File[] openedFiles) {
		this.openedFiles = openedFiles;
	}
	
	public void stop() {
		running.set(false);
	}

	public boolean isRunning() {
		return running.get();
	}

	@Override
	public void run() {
		running.set(true);

		Properties messages = BAG.getMessagesProps();
		JTextField currentPageField = BAG.getUserInterface().getRight().getCurrentPageField();
		JTextField numImgSelectedField = BAG.getUserInterface().getRight().getNumImgSelectedField();
		CenterPanel centerPanel = BAG.getUserInterface().getCenter();
		LeftPanel leftPanel = BAG.getUserInterface().getLeft();
		ArrayList<File> filesArray = BAG.getPdfFilesArray();
		
        SwingUtilities.invokeLater(() -> {
			leftPanel.clean();
			centerPanel.clean();
			Messages.cleanTextArea();
			currentPageField.setText("");
			numImgSelectedField.setText("");
			BAG.getUserInterface().getCenter().startWaitIconLoadPdf();
		});
        		
        // clean up
        BAG.setSelectedPage(0);
        BAG.setExtractionFolderPath(null);
        BAG.cleanPdfFilesArray();
        BAG.cleanSelectedImagesHashMap();
        BAG.cleanInlineSelectedImagesHashMap();
        BAG.cleanPagesRotationHashMap();
        		
        FileUtils.deleteThumbnailFiles(BAG.getThumbnailsFolderPath());
        FileUtils.deletePdfFile(BAG.getPdfFilePath());

		for (File item : openedFiles)
			if (!item.isDirectory())
				filesArray.add(item);
        
        // call check class control files after selection
        boolean fileCheck;
        CheckFiles checkFiles = new CheckFiles();
        if (filesArray.size() > 0) {
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
        		File resultFile = engine.mergePdf(filesArray, BAG.getPdfFilePath());
        		
        		if (resultFile != null && resultFile.exists() && resultFile.length() > 0) {
        			Messages.append("INFO", messages.getProperty("tmsg_12"));
        			PdfRenderLeft render = new PdfRenderLeft();
        			render.pdfRender();
        		} else {
        			Messages.append("WARNING", messages.getProperty("tmsg_13"));
        		}
        	}
        } 

        running.set(false);
	}

}
