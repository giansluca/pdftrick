package org.gmdev.pdftrick.tasks;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.*;

import org.gmdev.pdftrick.manager.*;
import org.gmdev.pdftrick.utils.*;

import static org.gmdev.pdftrick.serviceprocessor.ThreadTerminator.*;

public class CancelTask implements Runnable {
	
	private static final PdfTrickBag BAG = PdfTrickBag.INSTANCE;
	private final AtomicBoolean running = new AtomicBoolean(false);
	public void stop() {
	    running.set(false);
	 }

	public boolean isRunning() {
		return running.get();
	}

	@Override
	public void run() {
		running.set(true);
		JTextField currentPageField = BAG.getUserInterface().getRight().getCurrentPageField();
		JTextField numImagesSelectedField = BAG.getUserInterface().getRight().getNumImgSelectedField();
		TasksContainer tasksContainer = BAG.getTasksContainer();

		var fileChooserTask = tasksContainer.getFileChooserTask();
		if (fileChooserTask != null && fileChooserTask.isRunning()) {
			fileChooserTask.stop();
			joinFileChooserThread();
		}
		var dragAndDropTask = tasksContainer.getDragAndDropTask();
		if (dragAndDropTask != null && dragAndDropTask.isRunning()) {
			dragAndDropTask.stop();
			joinDragAndDropThread();
		}

		var firstPdfPageRenderTask = tasksContainer.getFirstPdfPageRenderTask();
		if (firstPdfPageRenderTask != null && firstPdfPageRenderTask.isRunning()) {
			firstPdfPageRenderTask.stop();
			joinFirstPdfPageRenderThread();
		}

		var pdfCoverThumbnailsDisplayTask =
				tasksContainer.getPdfCoverThumbnailsDisplayTask();
		if (pdfCoverThumbnailsDisplayTask != null && pdfCoverThumbnailsDisplayTask.isRunning()) {
			pdfCoverThumbnailsDisplayTask.stop();
			joinPdfCoverThumbnailsDisplayTask();
		}

		try {
			SwingUtilities.invokeAndWait(() -> {
				BAG.getUserInterface().getLeft().clean();
				BAG.getUserInterface().getCenter().clean();
				Messages.cleanTextArea();
				currentPageField.setText("");
				numImagesSelectedField.setText("");
			});
		} catch (InterruptedException | InvocationTargetException e) {
			throw new IllegalStateException(e);
		}

		BAG.cleanSelectedImagesHashMap();
		BAG.cleanInlineSelectedImagesHashMap();
		BAG.cleanPagesRotationHashMap();
		BAG.setSelectedPage(0);
		BAG.setExtractionFolderPath(null);
		BAG.cleanPdfFilesArray();
		
		FileUtils.deletePdfFile(BAG.getPdfFilePath());
		FileUtils.deleteThumbnailFiles(BAG.getThumbnailsFolderPath());

		running.set(false);
	}




}
