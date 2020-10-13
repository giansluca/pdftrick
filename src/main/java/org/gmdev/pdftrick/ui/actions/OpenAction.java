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

import org.gmdev.pdftrick.factory.PdfTrickFactory;
import org.gmdev.pdftrick.thread.OpenFileChooser;
import org.gmdev.pdftrick.ui.custom.CustomFileChooser;
import org.gmdev.pdftrick.utils.*;

public class OpenAction extends AbstractAction {
	
	private static final PdfTrickFactory factory = PdfTrickFactory.getFactory();
	private static final long serialVersionUID = 490332474672907971L;
	private final ImageIcon open_icon = new ImageIcon(FileLoader.loadAsUrl(Consts.OPENFILE_ICO));
	
	public OpenAction() {
		super.putValue(NAME, "Open");
		super.putValue(SMALL_ICON, open_icon);
		if (PdfTrickPreInitUtils.isWindows()) {
			super.putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		} else if (PdfTrickPreInitUtils.isMac()) {
			super.putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.META_MASK));
		}
	}

	/**
	 * Open files menu'
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		final Properties messages = factory.getMessages();
		final Container contentPanel = factory.getUserInterface().getContentPane();
		
		CustomFileChooser fileOpen = new CustomFileChooser();
		fileOpen.setMultiSelectionEnabled(true);
		fileOpen.setDialogTitle(Consts.JFCOPENTITLE);
		int ret = fileOpen.showOpenDialog(contentPanel);
		
		if (ret == JFileChooser.APPROVE_OPTION) {
        	if ( (factory.gettContainer().getDragAnDropFileChooserThread() != null && factory.gettContainer().getDragAnDropFileChooserThread().isAlive()) ||
        		(factory.gettContainer().getOpenFileChooserThread() != null &&	factory.gettContainer().getOpenFileChooserThread().isAlive())) {
        		PdfTrickUtils.resetDropBorder();
    			PdfTrickMessages.append("WARNING", messages.getProperty("tmsg_01"));
    			return;
        	}
        	
        	if (factory.gettContainer().getShowThumbsThread() != null && factory.gettContainer().getShowThumbsThread().isAlive()) {
    			ImageIcon warningIcon = new ImageIcon(getClass().getResource(Consts.WARNING_ICO));	
    			PdfTrickUtils.resetDropBorder();
    			PdfTrickMessages.displayMessage(null, messages.getProperty("jmsg_02"), messages.getProperty("jmsg_01"),
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
    			PdfTrickMessages.displayMessage(null, messages.getProperty("jmsg_02"), messages.getProperty("jmsg_01"),
    					JOptionPane.WARNING_MESSAGE, warningIcon);
    			return;	
        	}
        	
        	File[] files = fileOpen.getSelectedFiles();
        	
        	OpenFileChooser openFileChooser = new OpenFileChooser(files);
        	factory.gettContainer().setOpenFileChooser(openFileChooser);
        	
        	Thread openFileChooserThread = new Thread(openFileChooser, "openFileChooserThread");
        	factory.gettContainer().setOpenFileChooserThread(openFileChooserThread);
        	openFileChooserThread.start();
        }
        	
	}
	
	
}
