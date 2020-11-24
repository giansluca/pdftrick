package org.gmdev.pdftrick.ui.actions;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.io.File;
import java.nio.file.Path;
import java.util.*;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;

import org.gmdev.pdftrick.manager.PdfTrickBag;
import org.gmdev.pdftrick.swingmanager.ModalWarningPanel;
import org.gmdev.pdftrick.tasks.ImagesExtractionTask;
import org.gmdev.pdftrick.ui.custom.CustomFileChooser;
import org.gmdev.pdftrick.utils.*;

public class ImagesExtractionAction extends AbstractAction  {
	
	private static final PdfTrickBag BAG = PdfTrickBag.INSTANCE;

	@Override
	public void actionPerformed(ActionEvent event) {
		final Properties messages = BAG.getMessagesProps();
		final Container contentPanel = BAG.getUserInterface().getContentPane();
		
		if (BAG.getTasksContainer().getImgExtractionThread() != null &&
				BAG.getTasksContainer().getImgExtractionThread().isAlive()) {

			Messages.append("WARNING", messages.getProperty("tmsg_02"));
			return;
		}
		if (BAG.getTasksContainer().getOpenFileChooserThread() != null &&
				BAG.getTasksContainer().getOpenFileChooserThread().isAlive()) {

			Messages.append("WARNING", messages.getProperty("tmsg_01"));
			return;
		}
		if (BAG.getTasksContainer().getDragAnDropFileChooserThread() != null &&
				BAG.getTasksContainer().getDragAnDropFileChooserThread().isAlive()) {

			Messages.append("WARNING", messages.getProperty("tmsg_01"));
			return;
		}
		if (BAG.getTasksContainer().getShowThumbsThread() != null &&
				BAG.getTasksContainer().getShowThumbsThread().isAlive()) {

			ModalWarningPanel.displayLoadingPdfThumbnailsWarning();
			return;
		}
		
		boolean extract = true;
		File resultFile = BAG.getPdfFilePath().toFile();
		if (resultFile.exists() && resultFile.length() > 0) {
			CustomFileChooser chooseFolderToSave = new CustomFileChooser();
			chooseFolderToSave.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			chooseFolderToSave.setDialogTitle(Constants.JFC_EXTRACT_TITLE);
			
			String extractionFolder = null;
			Set<String> keys = BAG.getSelectedImages().keySet();
			Set<String> kk = BAG.getInlineSelectedImages().keySet();
			
			if (keys.size() > 0 || kk.size() > 0) {
				if (chooseFolderToSave.showSaveDialog(contentPanel) == JFileChooser.APPROVE_OPTION) {
					if (SetupUtils.isWindows()) {
						extractionFolder = chooseFolderToSave.getSelectedFile().getAbsolutePath();
					} else if (SetupUtils.isMac()) {
						extractionFolder = chooseFolderToSave.getCurrentDirectory().getAbsolutePath();
					}
					BAG.setExtractionFolderPath(Path.of(extractionFolder));
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
			BAG.getTasksContainer().setImagesExtractionTask(imagesExtractionTask);
			
			Thread imgExtractionThread = new Thread(imagesExtractionTask, "imgExtractionThread");
			BAG.getTasksContainer().setImgExtractionThread(imgExtractionThread);
			
			imgExtractionThread.start();
		}
	}


}
