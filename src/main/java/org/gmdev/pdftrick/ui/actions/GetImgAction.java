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
	private static final PdfTrickBag factory = PdfTrickBag.getBag();
	
	public GetImgAction() {
	}
	
	/**
	 * Called from the GET IMG button, extract images
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		final Properties messages = factory.getMessages();
		final Container contentPanel = factory.getUserInterface().getContentPane();
		
		if (factory.getThreadContainer().getImgExtractionThread() != null && factory.getThreadContainer().getImgExtractionThread().isAlive()) {
			Messages.append("WARNING", messages.getProperty("tmsg_02"));
			return;
		}
		if (factory.getThreadContainer().getOpenFileChooserThread() != null && factory.getThreadContainer().getOpenFileChooserThread().isAlive()) {
			Messages.append("WARNING", messages.getProperty("tmsg_01"));
			return;
		}
		if (factory.getThreadContainer().getDragAnDropFileChooserThread() != null && factory.getThreadContainer().getDragAnDropFileChooserThread().isAlive()) {
			Messages.append("WARNING", messages.getProperty("tmsg_01"));
			return;
		}
		if (factory.getThreadContainer().getShowThumbsThread() != null && factory.getThreadContainer().getShowThumbsThread().isAlive()) {
			ImageIcon warningIcon = new ImageIcon(getClass().getResource(Constants.WARNING_ICO));
			Messages.displayMessage(null, messages.getProperty("jmsg_02"), messages.getProperty("jmsg_01"),
					JOptionPane.WARNING_MESSAGE, warningIcon);
			return;
		}
		
		boolean extract = true;
		File resultFile = new File(factory.getPdfFilePath());
		if (resultFile != null && resultFile.exists() && resultFile.length() > 0) {
			CustomFileChooser choosefolderToSave = new CustomFileChooser();
			choosefolderToSave.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			choosefolderToSave.setDialogTitle(Constants.JFC_EXTRACT_TITLE);
			
			String selectedFolderToSave = "";
			Set<String> keys = factory.getImageSelected().keySet();
			Set<String> kk = factory.getInlineImgSelected().keySet();
			
			if (keys.size() > 0 || kk.size() > 0) {
				if (choosefolderToSave.showSaveDialog(contentPanel) == JFileChooser.APPROVE_OPTION) { 
					if (SetupUtils.isWindows()) {
						selectedFolderToSave = choosefolderToSave.getSelectedFile().getAbsolutePath();
					} else if (SetupUtils.isMac()) {
						selectedFolderToSave = choosefolderToSave.getCurrentDirectory().getAbsolutePath();
					}
					factory.setFolderToSave(selectedFolderToSave);
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
			factory.getThreadContainer().setImgExtraction(imgExtraction);
			
			Thread imgExtractionThread = new Thread(imgExtraction, "imgExtractionThread");
			factory.getThreadContainer().setImgExtractionThread(imgExtractionThread);
			
			imgExtractionThread.start();
		}
	}


}
