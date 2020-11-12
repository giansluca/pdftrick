package org.gmdev.pdftrick.ui.actions;

import java.io.File;
import java.util.Properties;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import org.gmdev.pdftrick.manager.PdfTrickBag;
import org.gmdev.pdftrick.thread.DragAnDropFileChooser;
import org.gmdev.pdftrick.utils.Constants;
import org.gmdev.pdftrick.utils.Messages;
import org.gmdev.pdftrick.utils.Utils;
import org.gmdev.pdftrick.utils.external.FileDrop;

public class DragAndDropAction implements FileDrop.Listener {
	
	private static final PdfTrickBag BAG = PdfTrickBag.INSTANCE;
	
	public DragAndDropAction() {
	}
	
	/**
	 * Called when user upload file with drop action in a panel left
	 */
	@Override
	public void filesDropped(File[] fileVett) {
		final Properties messages = BAG.getMessagesProps();
		
		if ( (BAG.getThreadContainer().getDragAnDropFileChooserThread() != null && BAG.getThreadContainer().getDragAnDropFileChooserThread().isAlive() ) ||
			(BAG.getThreadContainer().getOpenFileChooserThread() != null &&	BAG.getThreadContainer().getOpenFileChooserThread().isAlive())) {
			Utils.resetLeftPanelFileDropBorder();
			Messages.append("WARNING", messages.getProperty("tmsg_01"));
			
			return;
		}
		if (BAG.getThreadContainer().getShowThumbsThread() != null && BAG.getThreadContainer().getShowThumbsThread().isAlive()) {
			ImageIcon warningIcon = new ImageIcon(getClass().getResource(Constants.WARNING_ICO));
			Utils.resetLeftPanelFileDropBorder();
			Messages.displayMessage(null,messages.getProperty("jmsg_02"), messages.getProperty("jmsg_01"),
					JOptionPane.WARNING_MESSAGE, warningIcon);
    		
			return;
    	}  
    	if (BAG.getThreadContainer().getImgExtractionThread()!=null && BAG.getThreadContainer().getImgExtractionThread().isAlive()) {
    		ImageIcon warningIcon = new ImageIcon(getClass().getResource(Constants.WARNING_ICO));
		    Utils.resetLeftPanelFileDropBorder();
		    Messages.displayMessage(null, messages.getProperty("jmsg_03"), messages.getProperty("jmsg_01"),
					JOptionPane.WARNING_MESSAGE, warningIcon);
    		
		    return;	
    	}
    	if (BAG.getThreadContainer().getImgThumbThread()!=null && BAG.getThreadContainer().getImgThumbThread().isAlive()) {
    		ImageIcon warningIcon = new ImageIcon(getClass().getResource(Constants.WARNING_ICO));
			Utils.resetLeftPanelFileDropBorder();
			Messages.displayMessage(null, messages.getProperty("jmsg_04"), messages.getProperty("jmsg_01"),
					JOptionPane.WARNING_MESSAGE, warningIcon);
    		
			return;	
    	}
		
		DragAnDropFileChooser dropFileChooser = new DragAnDropFileChooser(fileVett);
		BAG.getThreadContainer().setDragAnDropFileChooser(dropFileChooser);
		
		Thread dragAnDropFileChooserThread = new Thread(dropFileChooser, "dragAnDropFileChooserThread");
		BAG.getThreadContainer().setDragAnDropFileChooserThread(dragAnDropFileChooserThread);
		dragAnDropFileChooserThread.start();
		
	}
	
	

}
