package org.gmdev.pdftrick.ui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.text.MessageFormat;
import java.util.Properties;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

import org.gmdev.pdftrick.factory.PdfTrickFactory;
import org.gmdev.pdftrick.utils.Consts;
import org.gmdev.pdftrick.utils.FileLoader;
import org.gmdev.pdftrick.utils.PdfTrickMessages;

public class AboutAction extends AbstractAction {
	
	private static final PdfTrickFactory factory = PdfTrickFactory.getFactory();
	private static final long serialVersionUID = -9051815693784339746L;
	private final ImageIcon about_icon = new ImageIcon(FileLoader.loadAsUrl(Consts.ABOUT_ICO));
	
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
		final String arch = factory.getArch();
		final String os = factory.getOs();
		ImageIcon imageIcon = new ImageIcon(FileLoader.loadAsUrl(Consts.MAIN_ICO));
		
		PdfTrickMessages.displayMessage(factory.getUserInterface(), MessageFormat.format(messages.getProperty("dmsg_01_w"), os, arch), 
				messages.getProperty("jmsg_07"), 1, imageIcon);
	}
	
}
