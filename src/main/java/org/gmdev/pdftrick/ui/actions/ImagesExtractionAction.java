package org.gmdev.pdftrick.ui.actions;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.io.File;
import java.nio.file.Path;
import java.util.*;

import javax.swing.*;

import org.gmdev.pdftrick.manager.*;
import org.gmdev.pdftrick.swingmanager.ModalWarningPanel;
import org.gmdev.pdftrick.tasks.ImagesExtractionTask;
import org.gmdev.pdftrick.ui.custom.CustomFileChooser;
import org.gmdev.pdftrick.utils.*;

public class ImagesExtractionAction extends AbstractAction  {
	
	private static final PdfTrickBag bag = PdfTrickBag.INSTANCE;

	@Override
	public void actionPerformed(ActionEvent event) {
		Properties messages = bag.getMessagesProps();
		Container contentPanel = bag.getUserInterface().getContentPane();
		TasksContainer tasksContainer = bag.getTasksContainer();
		
		if (tasksContainer.getImagesExtractionThread() != null &&
				tasksContainer.getImagesExtractionThread().isAlive()) {

			Messages.append("WARNING", messages.getProperty("tmsg_02"));
			return;
		}
		if (tasksContainer.getFileChooserThread() != null &&
				tasksContainer.getFileChooserThread().isAlive()) {

			Messages.append("WARNING", messages.getProperty("t_msg_01"));
			return;
		}
		if (tasksContainer.getDragAndDropThread() != null &&
				tasksContainer.getDragAndDropThread().isAlive()) {

			Messages.append("WARNING", messages.getProperty("t_msg_01"));
			return;
		}
		if (tasksContainer.getPdfCoverThumbnailsDisplayThread() != null &&
				tasksContainer.getPdfCoverThumbnailsDisplayThread().isAlive()) {

			ModalWarningPanel.displayLoadingPdfThumbnailsWarning();
			return;
		}
		
		boolean extract = true;
		File resultFile = bag.getPdfFilePath().toFile();
		if (resultFile.exists() && resultFile.length() > 0) {
			CustomFileChooser chooseFolderToSave = new CustomFileChooser();
			chooseFolderToSave.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			chooseFolderToSave.setDialogTitle(Constants.JFC_EXTRACT_TITLE);
			
			String extractionFolder = null;
			Set<String> keys = bag.getSelectedImages().keySet();
			Set<String> kk = bag.getInlineSelectedImages().keySet();
			
			if (keys.size() > 0 || kk.size() > 0) {
				if (chooseFolderToSave.showSaveDialog(contentPanel) == JFileChooser.APPROVE_OPTION) {
					if (SetupUtils.isWindows()) {
						extractionFolder = chooseFolderToSave.getSelectedFile().getAbsolutePath();
					} else if (SetupUtils.isMac()) {
						extractionFolder = chooseFolderToSave.getCurrentDirectory().getAbsolutePath();
					}
					bag.setExtractionFolderPath(Path.of(extractionFolder));
				} else {
					extract = false;
				}
			} else {
				Messages.append("INFO", messages.getProperty("tmsg_03"));
				extract = false;
			}
		} else {
			Messages.append("INFO", messages.getProperty("tmsg_04"));
			extract = false;
		}
		if (extract) {
			ImagesExtractionTask imagesExtractionTask = new ImagesExtractionTask();
			tasksContainer.setImagesExtractionTask(imagesExtractionTask);
			
			Thread imagesExtractionThread = new Thread(imagesExtractionTask);
			tasksContainer.setImagesExtractionThread(imagesExtractionThread);

			imagesExtractionThread.start();
		}
	}


}
