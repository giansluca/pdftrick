package org.gmdev.pdftrick.ui.actions;

import java.awt.Container;
import java.awt.event.*;
import java.io.File;
import java.util.Properties;

import javax.swing.*;

import org.gmdev.pdftrick.manager.PdfTrickBag;
import org.gmdev.pdftrick.swingmanager.ModalWarningPanel;
import org.gmdev.pdftrick.thread.OpenFileChooser;
import org.gmdev.pdftrick.ui.custom.CustomFileChooser;
import org.gmdev.pdftrick.ui.panels.LeftPanel;
import org.gmdev.pdftrick.utils.*;

public class OpenAction extends AbstractAction {
	
	private static final PdfTrickBag BAG = PdfTrickBag.INSTANCE;
	private static final String ACTION_NAME = "Open";

	public OpenAction() {
		ImageIcon open_icon = new ImageIcon(FileLoader.loadFileAsUrl(Constants.OPEN_FILE_ICO));
		super.putValue(NAME, ACTION_NAME);
		super.putValue(SMALL_ICON, open_icon);
		if (SetupUtils.isWindows())
			super.putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
		else
			super.putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.META_DOWN_MASK));
	}

	/**
	 * Open files menu
	 */
	@Override
	public void actionPerformed(ActionEvent event) {
		LeftPanel leftPanel = BAG.getUserInterface().getLeft();
		Properties messagesProps = BAG.getMessagesProps();
		Container contentPanel = BAG.getUserInterface().getContentPane();
		
		CustomFileChooser fileOpen = new CustomFileChooser();
		fileOpen.setMultiSelectionEnabled(true);
		fileOpen.setDialogTitle(Constants.JFC_OPEN_TITLE);
		int ret = fileOpen.showOpenDialog(contentPanel);
		
		if (ret == JFileChooser.APPROVE_OPTION) {
        	if ((BAG.getThreadContainer().getDragAnDropFileChooserThread() != null &&
					BAG.getThreadContainer().getDragAnDropFileChooserThread().isAlive()) ||
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
        	
        	if (BAG.getThreadContainer().getImgExtractionThread()!=null &&
					BAG.getThreadContainer().getImgExtractionThread().isAlive()) {
        		leftPanel.resetLeftPanelFileDropBorder();
				ModalWarningPanel.displayLoadingPageThumbnailImagesWarning();
    		    return;	
        	}
        	
        	if (BAG.getThreadContainer().getImgThumbThread()!=null &&
					BAG.getThreadContainer().getImgThumbThread().isAlive()) {
        		leftPanel.resetLeftPanelFileDropBorder();
				ModalWarningPanel.displayLoadingPageThumbnailImagesWarning();
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
