package org.gmdev.pdftrick.ui.actions;

import java.io.File;
import java.util.Properties;

import org.gmdev.pdftrick.manager.PdfTrickBag;
import org.gmdev.pdftrick.swingmanager.ModalWarningPanel;
import org.gmdev.pdftrick.thread.DragAnDropFileChooser;
import org.gmdev.pdftrick.ui.panels.LeftPanel;
import org.gmdev.pdftrick.utils.FileUtils;
import org.gmdev.pdftrick.utils.Messages;
import org.gmdev.pdftrick.utils.external.FileDrop;

public class DragAndDropAction implements FileDrop.Listener {
	
	private static final PdfTrickBag BAG = PdfTrickBag.INSTANCE;
	
	/**
	 * Called when user upload file with drop action in a panel left
	 */
	@Override
	public void filesDropped(File[] fileVett) {
		LeftPanel leftPanel = BAG.getUserInterface().getLeft();
		Properties messagesProps = BAG.getMessagesProps();
		
		if ((BAG.getThreadContainer().getDragAnDropFileChooserThread() != null &&
				BAG.getThreadContainer().getDragAnDropFileChooserThread().isAlive() ) ||
			(BAG.getThreadContainer().getOpenFileChooserThread() != null &&
					BAG.getThreadContainer().getOpenFileChooserThread().isAlive())) {

			leftPanel.resetLeftPanelFileDropBorder();
			Messages.append("WARNING", messagesProps.getProperty("tmsg_01"));
			return;
		}

		if (BAG.getThreadContainer().getShowThumbsThread() != null &&
				BAG.getThreadContainer().getShowThumbsThread().isAlive()) {

			leftPanel.resetLeftPanelFileDropBorder();
			ModalWarningPanel.displayLoadingPdfThumbnailsWarning();
			return;
    	}

    	if (BAG.getThreadContainer().getImgExtractionThread() != null &&
				BAG.getThreadContainer().getImgExtractionThread().isAlive()) {

		    leftPanel.resetLeftPanelFileDropBorder();
		    ModalWarningPanel.displayExtractingImagesWarning();
		    return;	
    	}

    	if (BAG.getThreadContainer().getImgThumbThread() != null &&
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
