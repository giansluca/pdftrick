package org.gmdev.pdftrick.ui.actions;

import java.io.File;
import java.util.Properties;

import org.gmdev.pdftrick.manager.PdfTrickBag;
import org.gmdev.pdftrick.manager.ThreadContainer;
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
		ThreadContainer threadContainer = BAG.getThreadContainer();

		var dragAndDropTask = threadContainer.getDragAndDropTask();
		var fileChooserTask = threadContainer.getFileChooserTask();
		if ( (dragAndDropTask != null && dragAndDropTask.isRunning()) ||
				(fileChooserTask != null && fileChooserTask.isRunning()) ) {

			leftPanel.resetLeftPanelFileDropBorder();
			Messages.append("WARNING", messagesProps.getProperty("tmsg_01"));
			return;
		}

		if (threadContainer.getShowThumbsThread() != null &&
				threadContainer.getShowThumbsThread().isAlive()) {

			leftPanel.resetLeftPanelFileDropBorder();
			ModalWarningPanel.displayLoadingPdfThumbnailsWarning();
			return;
    	}

    	if (threadContainer.getImgExtractionThread() != null &&
				BAG.getThreadContainer().getImgExtractionThread().isAlive()) {

		    leftPanel.resetLeftPanelFileDropBorder();
		    ModalWarningPanel.displayExtractingImagesWarning();
		    return;	
    	}

    	if (threadContainer.getImgThumbThread() != null &&
				BAG.getThreadContainer().getImgThumbThread().isAlive()) {

    		leftPanel.resetLeftPanelFileDropBorder();
			ModalWarningPanel.displayLoadingPageThumbnailImagesWarning();
			return;	
    	}
		
		DragAndDropTask newDragAndDropTask = new DragAndDropTask(files);
		BAG.getThreadContainer().setDragAndDropTask(newDragAndDropTask);
		
		Thread dragAnDropFileChooserThread = new Thread(newDragAndDropTask, "dragAnDropFileChooserThread");
		BAG.getThreadContainer().setDragAnDropFileChooserThread(dragAnDropFileChooserThread);
		dragAnDropFileChooserThread.start();
	}
	
	

}
