package org.gmdev.pdftrick.ui.actions;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Properties;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.gmdev.pdftrick.manager.PdfTrickBag;
import org.gmdev.pdftrick.thread.ImgExtraction;
import org.gmdev.pdftrick.ui.custom.CustomFileChooser;
import org.gmdev.pdftrick.utils.Constants;
import org.gmdev.pdftrick.utils.Messages;
import org.gmdev.pdftrick.utils.SetupUtils;

public class GetImgAction extends AbstractAction  {
	
	private static final long serialVersionUID = 5066094189763059556L;
	private static final PdfTrickBag BAG = PdfTrickBag.INSTANCE;
	
	public GetImgAction() {
	}
	
	/**
	 * Called from the GET IMG button, extract images
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		final Properties messages = BAG.getMessages();
		final Container contentPanel = BAG.getUserInterface().getContentPane();
		
		if (BAG.getThreadContainer().getImgExtractionThread() != null && BAG.getThreadContainer().getImgExtractionThread().isAlive()) {
			Messages.append("WARNING", messages.getProperty("tmsg_02"));
			return;
		}
		if (BAG.getThreadContainer().getOpenFileChooserThread() != null && BAG.getThreadContainer().getOpenFileChooserThread().isAlive()) {
			Messages.append("WARNING", messages.getProperty("tmsg_01"));
			return;
		}
		if (BAG.getThreadContainer().getDragAnDropFileChooserThread() != null && BAG.getThreadContainer().getDragAnDropFileChooserThread().isAlive()) {
			Messages.append("WARNING", messages.getProperty("tmsg_01"));
			return;
		}
		if (BAG.getThreadContainer().getShowThumbsThread() != null && BAG.getThreadContainer().getShowThumbsThread().isAlive()) {
			ImageIcon warningIcon = new ImageIcon(getClass().getResource(Constants.WARNING_ICO));
			Messages.displayMessage(null, messages.getProperty("jmsg_02"), messages.getProperty("jmsg_01"),
					JOptionPane.WARNING_MESSAGE, warningIcon);
			return;
		}
		
		boolean extract = true;
		File resultFile = new File(BAG.getPdfFilePath());
		if (resultFile != null && resultFile.exists() && resultFile.length() > 0) {
			CustomFileChooser choosefolderToSave = new CustomFileChooser();
			choosefolderToSave.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			choosefolderToSave.setDialogTitle(Constants.JFC_EXTRACT_TITLE);
			
			String selectedFolderToSave = "";
			Set<String> keys = BAG.getImageSelected().keySet();
			Set<String> kk = BAG.getInlineImgSelected().keySet();
			
			if (keys.size() > 0 || kk.size() > 0) {
				if (choosefolderToSave.showSaveDialog(contentPanel) == JFileChooser.APPROVE_OPTION) { 
					if (SetupUtils.isWindows()) {
						selectedFolderToSave = choosefolderToSave.getSelectedFile().getAbsolutePath();
					} else if (SetupUtils.isMac()) {
						selectedFolderToSave = choosefolderToSave.getCurrentDirectory().getAbsolutePath();
					}
					BAG.setFolderToSave(selectedFolderToSave);
				} else {
					extract = false;
				}
			} else {
				Messages.append("INFO", messages.getProperty("tmsg_03"));
				extract = false;
			}
		} else {
			Messages.append("INFO", messages.getProperty("tmsg_04"));
			extract = false;
		}
		if (extract) {
			ImgExtraction imgExtraction = new ImgExtraction();
			BAG.getThreadContainer().setImgExtraction(imgExtraction);
			
			Thread imgExtractionThread = new Thread(imgExtraction, "imgExtractionThread");
			BAG.getThreadContainer().setImgExtractionThread(imgExtractionThread);
			
			imgExtractionThread.start();
		}
	}


}
