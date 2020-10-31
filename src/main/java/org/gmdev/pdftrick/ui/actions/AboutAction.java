package org.gmdev.pdftrick.ui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.text.MessageFormat;
import java.util.Properties;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

import org.gmdev.pdftrick.factory.PdfTrickBag;
import org.gmdev.pdftrick.utils.Constants;
import org.gmdev.pdftrick.utils.FileLoader;
import org.gmdev.pdftrick.utils.Messages;

public class AboutAction extends AbstractAction {
	
	private static final PdfTrickBag factory = PdfTrickBag.getPdfTrickBag();
	private static final long serialVersionUID = -9051815693784339746L;
	private final ImageIcon about_icon = new ImageIcon(FileLoader.loadAsUrl(Constants.ABOUT_ICO));
	
	public AboutAction() {
		super.putValue(NAME, "About");
		super.putValue(SMALL_ICON, about_icon);
		super.putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_I, ActionEvent.CTRL_MASK));
	}
	
	/**
	 * Called from the ABOUT menu', show a small panel with credits - for Windows only
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		final Properties messages = factory.getMessages();
		final String os = factory.getOs();
		ImageIcon imageIcon = new ImageIcon(FileLoader.loadAsUrl(Constants.MAIN_ICO));
		
		Messages.displayMessage(factory.getUserInterface(), MessageFormat.format(messages.getProperty("dmsg_01_w"), os),
				messages.getProperty("jmsg_07"), 1, imageIcon);
	}
	
}
