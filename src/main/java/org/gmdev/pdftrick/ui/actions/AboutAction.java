package org.gmdev.pdftrick.ui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.text.MessageFormat;
import java.util.Properties;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

import org.gmdev.pdftrick.manager.PdfTrickBag;
import org.gmdev.pdftrick.swingmanager.ModalInfoPanel;
import org.gmdev.pdftrick.utils.Constants;
import org.gmdev.pdftrick.utils.FileLoader;
import org.gmdev.pdftrick.utils.Messages;

public class AboutAction extends AbstractAction {
	
	private static final PdfTrickBag BAG = PdfTrickBag.INSTANCE;
	private static final String ACTION_NAME = "About";

	public AboutAction() {
		ImageIcon aboutIcon = new ImageIcon(FileLoader.loadFileAsUrl(Constants.ABOUT_ICO));
		super.putValue(NAME, ACTION_NAME);
		super.putValue(SMALL_ICON, aboutIcon);
		super.putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_I, ActionEvent.CTRL_MASK));
	}
	
	/**
	 * Called from the ABOUT menu, Windows only
	 */
	@Override
	public void actionPerformed(ActionEvent event) {
		Properties messagesProps = BAG.getMessagesProps();
		String os = BAG.getOs();
		ImageIcon imageIcon = new ImageIcon(FileLoader.loadFileAsUrl(Constants.MAIN_ICO));

		String message = MessageFormat.format(messagesProps.getProperty("dmsg_01_w"), os);
		String title = messagesProps.getProperty("jmsg_07");
		ModalInfoPanel.displayAboutPanel(BAG.getUserInterface(), message, title, imageIcon);
	}
	
}
