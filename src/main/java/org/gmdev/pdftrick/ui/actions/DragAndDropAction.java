package org.gmdev.pdftrick.ui.actions;

import java.io.File;
import java.util.Properties;

import org.gmdev.pdftrick.manager.PdfTrickBag;
import org.gmdev.pdftrick.manager.ThreadContainer;
import org.gmdev.pdftrick.swingmanager.ModalWarningPanel;
import org.gmdev.pdftrick.thread.DragAnDropFileChooser;
import org.gmdev.pdftrick.ui.panels.LeftPanel;
import org.gmdev.pdftrick.utils.Messages;
import org.gmdev.pdftrick.utils.external.FileDrop;

public class DragAndDropAction implements FileDrop.Listener {
	
	private static final PdfTrickBag BAG = PdfTrickBag.INSTANCE;

	@Override
	public void filesDropped(File[] fileVett) {
		LeftPanel leftPanel = BAG.getUserInterface().getLeft();
		Properties messagesProps = BAG.getMessagesProps();

		ThreadContainer threadContainer = BAG.getThreadContainer();

		if ((threadContainer.getDragAnDropFileChooserThread() != null &&
				threadContainer.getDragAnDropFileChooserThread().isAlive() ) ||
			(threadContainer.getOpenFileChooserThread() != null &&
					threadContainer.getOpenFileChooserThread().isAlive())) {

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
		
		DragAnDropFileChooser dropFileChooser = new DragAnDropFileChooser(fileVett);
		BAG.getThreadContainer().setDragAnDropFileChooser(dropFileChooser);
		
		Thread dragAnDropFileChooserThread = new Thread(dropFileChooser, "dragAnDropFileChooserThread");
		BAG.getThreadContainer().setDragAnDropFileChooserThread(dragAnDropFileChooserThread);
		dragAnDropFileChooserThread.start();
	}
	
	

}
