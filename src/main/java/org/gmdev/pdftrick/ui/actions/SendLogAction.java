package org.gmdev.pdftrick.ui.actions;

import java.awt.event.ActionEvent;
import java.util.Properties;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import org.gmdev.pdftrick.factory.PdfTrickFactory;
import org.gmdev.pdftrick.utils.Consts;
import org.gmdev.pdftrick.utils.FileLoader;
import org.gmdev.pdftrick.utils.PdfTrickMessages;
import org.gmdev.pdftrick.utils.PdfTrickUtils;

public class SendLogAction extends AbstractAction {
	
	private static final long serialVersionUID = 4422793984411438906L;
	private static final PdfTrickFactory factory = PdfTrickFactory.getFactory();
	private final ImageIcon sendLog_icon = new ImageIcon(FileLoader.loadAsUrl(Consts.SEND_LOG_ICO));
	
	public SendLogAction() {
		super.putValue(NAME, "Send Log");
		super.putValue(SMALL_ICON, sendLog_icon);
	}
	
	/**
	 * Called by menu' item Help / Send Log
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		final Properties messages = factory.getMessages();
		
		if (factory.gettContainer().getSendLogThread() != null && factory.gettContainer().getSendLogThread().isAlive()) {
			PdfTrickMessages.append("INFO", messages.getProperty("tmsg_05"));
			return;
		}
		
		if (factory.gettContainer().getOpenFileChooserThread() != null && factory.gettContainer().getOpenFileChooserThread().isAlive()) {
			PdfTrickMessages.append("INFO", messages.getProperty("tmsg_01"));
			return;
		}
		
		if (factory.gettContainer().getDragAnDropFileChooserThread() != null && factory.gettContainer().getDragAnDropFileChooserThread().isAlive()) {
			PdfTrickMessages.append("INFO", messages.getProperty("tmsg_01"));
			return;
		}
		
		if (factory.gettContainer().getShowThumbsThread() != null && factory.gettContainer().getShowThumbsThread().isAlive()) {
			ImageIcon warningIcon = new ImageIcon(getClass().getResource(Consts.WARNING_ICO));
			PdfTrickMessages.displayMessage(null, messages.getProperty("jmsg_02"), messages.getProperty("jmsg_01"),
					JOptionPane.WARNING_MESSAGE, warningIcon);
			return;
		}
		
		Thread th = new Thread(new Runnable() {
			@Override
			public void run() {
				sendLog();
				
			}
		});
		factory.gettContainer().setSendLogThread(th);
		th.start();
	}
	
	/**
	 * Send log to pdftrick.org ftp server
	 */
	public void sendLog() {
		Properties messages = factory.getMessages();
		PdfTrickMessages.append("INFO", messages.getProperty("tmsg_06"));
		PdfTrickUtils.sendLog();
		PdfTrickMessages.append("INFO", messages.getProperty("tmsg_07"));
	}
	
	

}
