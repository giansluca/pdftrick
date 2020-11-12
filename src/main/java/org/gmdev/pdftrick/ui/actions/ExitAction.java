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
	private static final PdfTrickBag BAG = PdfTrickBag.INSTANCE;
	private final ImageIcon exit_icon = new ImageIcon(FileLoader.loadFileAsUrl(Constants.EXIT_ICO));
	
	public ExitAction() {
		super.putValue(NAME, "Exit");
		super.putValue(SMALL_ICON, exit_icon);
		super.putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
	}
	
	/**
	 * Called from the EXIT menu', exit the application
	 */
	@Override
	public void actionPerformed(ActionEvent event) {
		final Properties messages = BAG.getMessages();
		
		if (BAG.getThreadContainer().getDivisionThumbs() != null && !BAG.getThreadContainer().getDivisionThumbs().isFinished()) {
			BAG.getThreadContainer().getDivisionThumbs().stop();
			while (!BAG.getThreadContainer().getDivisionThumbs().isFinished()) {
				// wait thread stop
			}
			
			if (BAG.getThreadContainer().getDivisionThumbsThread() != null) {
				while (BAG.getThreadContainer().getDivisionThumbsThread().isAlive()) {
					// wait thread stop
				}
			}
		}
		
		if (BAG.getThreadContainer().getExecPool() != null && !BAG.getThreadContainer().getExecPool().isFinished()) {
			BAG.getThreadContainer().getExecPool().stop();
			if (BAG.getThreadContainer().getExecPoolThread() != null) {
				while (BAG.getThreadContainer().getExecPoolThread().isAlive()) {
					// wait thread stop
				}
			}
		}
		if (BAG.getThreadContainer().getExecutor() != null) {
			BAG.getThreadContainer().getExecutor().shutdownNow();
			while (!BAG.getThreadContainer().getExecutor().isTerminated()) {
				//wait stop all threadPool task
			}
		}
		
		if (BAG.getThreadContainer().getImgExtraction() !=null && !BAG.getThreadContainer().getImgExtraction().isFinished()) {
			BAG.getThreadContainer().getImgExtraction().stop();
			if (BAG.getThreadContainer().getImgExtractionThread() !=null && BAG.getThreadContainer().getImgExtractionThread().isAlive()) {
				ImageIcon warningIcon = new ImageIcon(getClass().getResource(Constants.WARNING_ICO));
				Messages.displayMessage(null, messages.getProperty("jmsg_05"), messages.getProperty("jmsg_06"),
						JOptionPane.WARNING_MESSAGE, warningIcon);
			}
		}
		
		NativeObjectManager nativeManager = BAG.getNativeObjectManager();
		nativeManager.unloadNativeLib();
		
		Utils.deletePdfFile(BAG.getPdfFilePath());
		Utils.deleteThumbnailFiles(BAG.getThumbnailsFolderPath());
		
		System.exit(0);
	}
	
	
}
