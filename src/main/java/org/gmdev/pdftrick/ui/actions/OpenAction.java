package org.gmdev.pdftrick.ui.actions;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Properties;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import org.gmdev.pdftrick.manager.PdfTrickBag;
import org.gmdev.pdftrick.thread.OpenFileChooser;
import org.gmdev.pdftrick.ui.custom.CustomFileChooser;
import org.gmdev.pdftrick.utils.*;

public class OpenAction extends AbstractAction {
	
	private static final PdfTrickBag BAG = PdfTrickBag.INSTANCE;
	private static final long serialVersionUID = 490332474672907971L;
	private final ImageIcon open_icon = new ImageIcon(FileLoader.loadAsUrl(Constants.OPEN_FILE_ICO));
	
	public OpenAction() {
		super.putValue(NAME, "Open");
		super.putValue(SMALL_ICON, open_icon);
		if (SetupUtils.isWindows()) {
			super.putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		} else if (SetupUtils.isMac()) {
			super.putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.META_MASK));
		}
	}

	/**
	 * Open files menu'
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		final Properties messages = BAG.getMessages();
		final Container contentPanel = BAG.getUserInterface().getContentPane();
		
		CustomFileChooser fileOpen = new CustomFileChooser();
		fileOpen.setMultiSelectionEnabled(true);
		fileOpen.setDialogTitle(Constants.JFC_OPEN_TITLE);
		int ret = fileOpen.showOpenDialog(contentPanel);
		
		if (ret == JFileChooser.APPROVE_OPTION) {
        	if ( (BAG.getThreadContainer().getDragAnDropFileChooserThread() != null && BAG.getThreadContainer().getDragAnDropFileChooserThread().isAlive()) ||
        		(BAG.getThreadContainer().getOpenFileChooserThread() != null &&	BAG.getThreadContainer().getOpenFileChooserThread().isAlive())) {
        		Utils.resetDropBorder();
    			Messages.append("WARNING", messages.getProperty("tmsg_01"));
    			return;
        	}
        	
        	if (BAG.getThreadContainer().getShowThumbsThread() != null && BAG.getThreadContainer().getShowThumbsThread().isAlive()) {
    			ImageIcon warningIcon = new ImageIcon(getClass().getResource(Constants.WARNING_ICO));
    			Utils.resetDropBorder();
    			Messages.displayMessage(null, messages.getProperty("jmsg_02"), messages.getProperty("jmsg_01"),
    					JOptionPane.WARNING_MESSAGE, warningIcon);
    			return;
        	}  
        	
        	if (BAG.getThreadContainer().getImgExtractionThread()!=null && BAG.getThreadContainer().getImgExtractionThread().isAlive()) {
        		ImageIcon warningIcon = new ImageIcon(getClass().getResource(Constants.WARNING_ICO));
    		    Utils.resetDropBorder();
    		    Messages.displayMessage(null, messages.getProperty("jmsg_03"), messages.getProperty("jmsg_01"),
    					JOptionPane.WARNING_MESSAGE, warningIcon);
    		    return;	
        	}
        	
        	if (BAG.getThreadContainer().getImgThumbThread()!=null && BAG.getThreadContainer().getImgThumbThread().isAlive()) {
        		ImageIcon warningIcon = new ImageIcon(getClass().getResource(Constants.WARNING_ICO));
    			Utils.resetDropBorder();
    			Messages.displayMessage(null, messages.getProperty("jmsg_02"), messages.getProperty("jmsg_01"),
    					JOptionPane.WARNING_MESSAGE, warningIcon);
    			return;	
        	}
        	
        	File[] files = fileOpen.getSelectedFiles();
        	
        	OpenFileChooser openFileChooser = new OpenFileChooser(files);
        	BAG.getThreadContainer().setOpenFileChooser(openFileChooser);
        	
        	Thread openFileChooserThread = new Thread(openFileChooser, "openFileChooserThread");
        	BAG.getThreadContainer().setOpenFileChooserThread(openFileChooserThread);
        	openFileChooserThread.start();
        }
        	
	}
	
	
}
