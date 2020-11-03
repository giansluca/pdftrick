package org.gmdev.pdftrick.ui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Properties;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import org.gmdev.pdftrick.factory.PdfTrickBag;
import org.gmdev.pdftrick.nativeutil.NativeObjectManager;
import org.gmdev.pdftrick.utils.*;

public class ExitAction extends AbstractAction {
	
	private static final long serialVersionUID = 4846729705239261046L;
	private static final PdfTrickBag factory = PdfTrickBag.getPdfTrickBag();
	private final ImageIcon exit_icon = new ImageIcon(FileLoader.loadAsUrl(Constants.EXIT_ICO));
	
	public ExitAction() {
		super.putValue(NAME, "Exit");
		super.putValue(SMALL_ICON, exit_icon);
		super.putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
	}
	
	/**
	 * Called from the EXIT menu', exit the application
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		final Properties messages = factory.getMessages();
		
		if (factory.gettContainer().getDivisionThumbs() != null && !factory.gettContainer().getDivisionThumbs().isFinished()) {
			factory.gettContainer().getDivisionThumbs().stop();
			while (!factory.gettContainer().getDivisionThumbs().isFinished()) {
				// wait thread stop
			}
			
			if (factory.gettContainer().getDivisionThumbsThread() != null) {
				while (factory.gettContainer().getDivisionThumbsThread().isAlive()) {
					// wait thread stop
				}
			}
		}
		
		if (factory.gettContainer().getExecPool() != null && !factory.gettContainer().getExecPool().isFinished()) {
			factory.gettContainer().getExecPool().stop();
			if (factory.gettContainer().getExecPoolThread() != null) {
				while (factory.gettContainer().getExecPoolThread().isAlive()) {
					// wait thread stop
				}
			}
		}
		if (factory.gettContainer().getExecutor() != null) {
			factory.gettContainer().getExecutor().shutdownNow();
			while (!factory.gettContainer().getExecutor().isTerminated()) {
				//wait stop all threadPool task
			}
		}
		
		if (factory.gettContainer().getImgExtraction() !=null && !factory.gettContainer().getImgExtraction().isFinished()) {
			factory.gettContainer().getImgExtraction().stop();
			if (factory.gettContainer().getImgExtractionThread() !=null && factory.gettContainer().getImgExtractionThread().isAlive()) {
				ImageIcon warningIcon = new ImageIcon(getClass().getResource(Constants.WARNING_ICO));
				Messages.displayMessage(null, messages.getProperty("jmsg_05"), messages.getProperty("jmsg_06"),
						JOptionPane.WARNING_MESSAGE, warningIcon);
			}
		}
		
		NativeObjectManager nativeManager = factory.getNativeManager();
		nativeManager.unloadNativeLib();
		
		Utils.deleteResultFile();
		Utils.deleteImgFolderAnDFile();
		
		System.exit(0);
	}
	
	
}
