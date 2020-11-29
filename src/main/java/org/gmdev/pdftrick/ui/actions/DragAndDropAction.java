package org.gmdev.pdftrick.ui.actions;

import java.io.File;
import java.util.Properties;

import org.gmdev.pdftrick.manager.*;
import org.gmdev.pdftrick.swingmanager.ModalWarningPanel;
import org.gmdev.pdftrick.tasks.DragAndDropTask;
import org.gmdev.pdftrick.tasks.PageThumbnailsDisplayTask;
import org.gmdev.pdftrick.ui.panels.LeftPanel;
import org.gmdev.pdftrick.utils.Messages;
import org.gmdev.pdftrick.utils.external.FileDrop;

public class DragAndDropAction implements FileDrop.Listener {
	
	private static final PdfTrickBag BAG = PdfTrickBag.INSTANCE;

	@Override
	public void filesDropped(File[] files) {
		LeftPanel leftPanel = BAG.getUserInterface().getLeft();
		Properties messagesProps = BAG.getMessagesProps();
		TasksContainer tasksContainer = BAG.getTasksContainer();

		var dragAndDropTask = tasksContainer.getDragAndDropTask();
		var fileChooserTask = tasksContainer.getFileChooserTask();
		if ( (dragAndDropTask != null && dragAndDropTask.isRunning()) ||
				(fileChooserTask != null && fileChooserTask.isRunning()) ) {

			leftPanel.resetLeftPanelFileDropBorder();
			Messages.append("WARNING", messagesProps.getProperty("tmsg_01"));
			return;
		}

		var showPdfCoverThumbnailsTask = tasksContainer.getPdfCoverThumbnailsDisplayTask();
		if (showPdfCoverThumbnailsTask != null && showPdfCoverThumbnailsTask.isRunning()) {
			leftPanel.resetLeftPanelFileDropBorder();
			ModalWarningPanel.displayLoadingPdfThumbnailsWarning();
			return;
    	}

		var imagesExtractionTask = tasksContainer.getImagesExtractionTask();
		if (imagesExtractionTask != null && imagesExtractionTask.isRunning()) {
			leftPanel.resetLeftPanelFileDropBorder();
			ModalWarningPanel.displayExtractingImagesWarning();
			return;
		}

		PageThumbnailsDisplayTask pageThumbnailsDisplayTask = tasksContainer.getPageThumbnailsDisplayTask();
    	if (pageThumbnailsDisplayTask != null && pageThumbnailsDisplayTask.isRunning()) {
    		leftPanel.resetLeftPanelFileDropBorder();
			ModalWarningPanel.displayLoadingPageThumbnailImagesWarning();
			return;	
    	}
		
		DragAndDropTask newDragAndDropTask = new DragAndDropTask(files);
		tasksContainer.setDragAndDropTask(newDragAndDropTask);
		
		Thread dragAndDropThread = new Thread(newDragAndDropTask);
		tasksContainer.setDragAndDropThread(dragAndDropThread);
		dragAndDropThread.start();
	}

}
