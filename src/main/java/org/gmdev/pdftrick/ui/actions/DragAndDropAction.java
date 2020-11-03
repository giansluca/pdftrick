package org.gmdev.pdftrick.ui.actions;

import java.io.File;
import java.util.Properties;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import org.gmdev.pdftrick.factory.PdfTrickBag;
import org.gmdev.pdftrick.thread.DragAnDropFileChooser;
import org.gmdev.pdftrick.utils.Constants;
import org.gmdev.pdftrick.utils.Messages;
import org.gmdev.pdftrick.utils.Utils;
import org.gmdev.pdftrick.utils.external.FileDrop;

public class DragAndDropAction implements FileDrop.Listener {
	
	private static final PdfTrickBag factory = PdfTrickBag.getPdfTrickBag();
	
	public DragAndDropAction() {
	}
	
	/**
	 * Called when user upload file with drop action in a panel left
	 */
	@Override
	public void filesDropped(File[] fileVett) {
		final Properties messages = factory.getMessages();
		
		if ( (factory.getThreadContainer().getDragAnDropFileChooserThread() != null && factory.getThreadContainer().getDragAnDropFileChooserThread().isAlive() ) ||
			(factory.getThreadContainer().getOpenFileChooserThread() != null &&	factory.getThreadContainer().getOpenFileChooserThread().isAlive())) {
			Utils.resetDropBorder();
			Messages.append("WARNING", messages.getProperty("tmsg_01"));
			
			return;
		}
		if (factory.getThreadContainer().getShowThumbsThread() != null && factory.getThreadContainer().getShowThumbsThread().isAlive()) {
			ImageIcon warningIcon = new ImageIcon(getClass().getResource(Constants.WARNING_ICO));
			Utils.resetDropBorder();
			Messages.displayMessage(null,messages.getProperty("jmsg_02"), messages.getProperty("jmsg_01"),
					JOptionPane.WARNING_MESSAGE, warningIcon);
    		
			return;
    	}  
    	if (factory.getThreadContainer().getImgExtractionThread()!=null && factory.getThreadContainer().getImgExtractionThread().isAlive()) {
    		ImageIcon warningIcon = new ImageIcon(getClass().getResource(Constants.WARNING_ICO));
		    Utils.resetDropBorder();
		    Messages.displayMessage(null, messages.getProperty("jmsg_03"), messages.getProperty("jmsg_01"),
					JOptionPane.WARNING_MESSAGE, warningIcon);
    		
		    return;	
    	}
    	if (factory.getThreadContainer().getImgThumbThread()!=null && factory.getThreadContainer().getImgThumbThread().isAlive()) {
    		ImageIcon warningIcon = new ImageIcon(getClass().getResource(Constants.WARNING_ICO));
			Utils.resetDropBorder();
			Messages.displayMessage(null, messages.getProperty("jmsg_04"), messages.getProperty("jmsg_01"),
					JOptionPane.WARNING_MESSAGE, warningIcon);
    		
			return;	
    	}
		
		DragAnDropFileChooser dropFileChooser = new DragAnDropFileChooser(fileVett);
		factory.getThreadContainer().setDragAnDropFileChooser(dropFileChooser);
		
		Thread dragAnDropFileChooserThread = new Thread(dropFileChooser, "dragAnDropFileChooserThread");
		factory.getThreadContainer().setDragAnDropFileChooserThread(dragAnDropFileChooserThread);
		dragAnDropFileChooserThread.start();
		
	}
	
	

}
