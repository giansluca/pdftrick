package org.gmdev.pdftrick.ui.actions;

import java.io.File;
import java.util.Properties;

import org.gmdev.pdftrick.manager.*;
import org.gmdev.pdftrick.swingmanager.ModalWarningPanel;
import org.gmdev.pdftrick.thread.DragAndDropTask;
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

		var showPdfCoverThumbnailsTask = tasksContainer.getShowPdfCoverThumbnailsTask();
		if (showPdfCoverThumbnailsTask != null && showPdfCoverThumbnailsTask.isRunning()) {

			leftPanel.resetLeftPanelFileDropBorder();
			ModalWarningPanel.displayLoadingPdfThumbnailsWarning();
			return;
    	}

    	if (tasksContainer.getImgExtractionThread() != null &&
				BAG.getTasksContainer().getImgExtractionThread().isAlive()) {

		    leftPanel.resetLeftPanelFileDropBorder();
		    ModalWarningPanel.displayExtractingImagesWarning();
		    return;	
    	}

    	if (tasksContainer.getImgThumbThread() != null &&
				BAG.getTasksContainer().getImgThumbThread().isAlive()) {

    		leftPanel.resetLeftPanelFileDropBorder();
			ModalWarningPanel.displayLoadingPageThumbnailImagesWarning();
			return;	
    	}
		
		DragAndDropTask newDragAndDropTask = new DragAndDropTask(files);
		BAG.getTasksContainer().setDragAndDropTask(newDragAndDropTask);
		
		Thread dragAnDropFileChooserThread = new Thread(newDragAndDropTask, "dragAnDropFileChooserThread");
		BAG.getTasksContainer().setDragAnDropFileChooserThread(dragAnDropFileChooserThread);
		dragAnDropFileChooserThread.start();
	}
	
	

}
