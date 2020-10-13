package org.gmdev.pdftrick.ui.actions;

import java.io.File;
import java.util.Properties;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import org.gmdev.pdftrick.factory.PdfTrickFactory;
import org.gmdev.pdftrick.thread.DragAnDropFileChooser;
import org.gmdev.pdftrick.utils.Consts;
import org.gmdev.pdftrick.utils.PdfTrickMessages;
import org.gmdev.pdftrick.utils.PdfTrickUtils;
import org.gmdev.pdftrick.utils.FileDrop;

public class DragAndDropAction implements FileDrop.Listener {
	
	private static final PdfTrickFactory factory = PdfTrickFactory.getFactory();
	
	public DragAndDropAction() {
	}
	
	/**
	 * Called when user upload file with drop action in a panel left
	 */
	@Override
	public void filesDropped(File[] fileVett) {
		final Properties messages = factory.getMessages();
		
		if ( (factory.gettContainer().getDragAnDropFileChooserThread() != null && factory.gettContainer().getDragAnDropFileChooserThread().isAlive() ) ||
			(factory.gettContainer().getOpenFileChooserThread() != null &&	factory.gettContainer().getOpenFileChooserThread().isAlive())) {
			PdfTrickUtils.resetDropBorder();
			PdfTrickMessages.append("WARNING", messages.getProperty("tmsg_01"));
			
			return;
		}
		if (factory.gettContainer().getShowThumbsThread() != null && factory.gettContainer().getShowThumbsThread().isAlive()) {
			ImageIcon warningIcon = new ImageIcon(getClass().getResource(Consts.WARNING_ICO));	
			PdfTrickUtils.resetDropBorder();
			PdfTrickMessages.displayMessage(null,messages.getProperty("jmsg_02"), messages.getProperty("jmsg_01"),
					JOptionPane.WARNING_MESSAGE, warningIcon);
    		
			return;
    	}  
    	if (factory.gettContainer().getImgExtractionThread()!=null && factory.gettContainer().getImgExtractionThread().isAlive()) {
    		ImageIcon warningIcon = new ImageIcon(getClass().getResource(Consts.WARNING_ICO));
		    PdfTrickUtils.resetDropBorder();
		    PdfTrickMessages.displayMessage(null, messages.getProperty("jmsg_03"), messages.getProperty("jmsg_01"),
					JOptionPane.WARNING_MESSAGE, warningIcon);
    		
		    return;	
    	}
    	if (factory.gettContainer().getImgThumbThread()!=null && factory.gettContainer().getImgThumbThread().isAlive()) {
    		ImageIcon warningIcon = new ImageIcon(getClass().getResource(Consts.WARNING_ICO));
			PdfTrickUtils.resetDropBorder();
			PdfTrickMessages.displayMessage(null, messages.getProperty("jmsg_04"), messages.getProperty("jmsg_01"),
					JOptionPane.WARNING_MESSAGE, warningIcon);
    		
			return;	
    	}
		
		DragAnDropFileChooser dropFileChooser = new DragAnDropFileChooser(fileVett);
		factory.gettContainer().setDragAnDropFileChooser(dropFileChooser);
		
		Thread dragAnDropFileChooserThread = new Thread(dropFileChooser, "dragAnDropFileChooserThread");
		factory.gettContainer().setDragAnDropFileChooserThread(dragAnDropFileChooserThread);
		dragAnDropFileChooserThread.start();
		
	}
	
	

}
