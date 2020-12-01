package org.gmdev.pdftrick.ui.actions;

import java.awt.event.*;

import javax.swing.*;

import org.gmdev.pdftrick.swingmanager.ModalInfoPanel;
import org.gmdev.pdftrick.utils.FileLoader;

import static org.gmdev.pdftrick.utils.Constants.*;

public class AboutAction extends AbstractAction {
	
	private static final String ACTION_NAME = "About";

	public AboutAction() {
		ImageIcon aboutIcon = new ImageIcon(FileLoader.loadFileAsUrl(ABOUT_ICO));
		super.putValue(NAME, ACTION_NAME);
		super.putValue(SMALL_ICON, aboutIcon);
		super.putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_I, InputEvent.CTRL_DOWN_MASK));
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		ModalInfoPanel.displayAboutPanel();
	}
	
}
