package org.gmdev.pdftrick.ui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Properties;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import org.gmdev.pdftrick.manager.PdfTrickBag;
import org.gmdev.pdftrick.nativeutil.NativeObjectManager;
import org.gmdev.pdftrick.utils.*;

public class ExitAction extends AbstractAction {
	
	private static final long serialVersionUID = 4846729705239261046L;
	private static final PdfTrickBag factory = PdfTrickBag.getBag();
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
		
		if (factory.getThreadContainer().getDivisionThumbs() != null && !factory.getThreadContainer().getDivisionThumbs().isFinished()) {
			factory.getThreadContainer().getDivisionThumbs().stop();
			while (!factory.getThreadContainer().getDivisionThumbs().isFinished()) {
				// wait thread stop
			}
			
			if (factory.getThreadContainer().getDivisionThumbsThread() != null) {
				while (factory.getThreadContainer().getDivisionThumbsThread().isAlive()) {
					// wait thread stop
				}
			}
		}
		
		if (factory.getThreadContainer().getExecPool() != null && !factory.getThreadContainer().getExecPool().isFinished()) {
			factory.getThreadContainer().getExecPool().stop();
			if (factory.getThreadContainer().getExecPoolThread() != null) {
				while (factory.getThreadContainer().getExecPoolThread().isAlive()) {
					// wait thread stop
				}
			}
		}
		if (factory.getThreadContainer().getExecutor() != null) {
			factory.getThreadContainer().getExecutor().shutdownNow();
			while (!factory.getThreadContainer().getExecutor().isTerminated()) {
				//wait stop all threadPool task
			}
		}
		
		if (factory.getThreadContainer().getImgExtraction() !=null && !factory.getThreadContainer().getImgExtraction().isFinished()) {
			factory.getThreadContainer().getImgExtraction().stop();
			if (factory.getThreadContainer().getImgExtractionThread() !=null && factory.getThreadContainer().getImgExtractionThread().isAlive()) {
				ImageIcon warningIcon = new ImageIcon(getClass().getResource(Constants.WARNING_ICO));
				Messages.displayMessage(null, messages.getProperty("jmsg_05"), messages.getProperty("jmsg_06"),
						JOptionPane.WARNING_MESSAGE, warningIcon);
			}
		}
		
		NativeObjectManager nativeManager = factory.getNativeObjectManager();
		nativeManager.unloadNativeLib();
		
		Utils.deletePdfFile();
		Utils.deleteImgFolderAnDFiles();
		
		System.exit(0);
	}
	
	
}
