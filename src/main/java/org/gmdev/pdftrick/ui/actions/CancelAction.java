package org.gmdev.pdftrick.ui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.gmdev.pdftrick.manager.PdfTrickBag;
import org.gmdev.pdftrick.tasks.Cancel;

public class CancelAction extends AbstractAction {
	
	private static final PdfTrickBag BAG = PdfTrickBag.INSTANCE;
	
	/**
	 * Called from the CANCEL SELECTION button, clean everything
	 */
	@Override
	public void actionPerformed(ActionEvent event) {
		if (BAG.getTasksContainer().getCancelThread() !=null && BAG.getTasksContainer().getCancelThread().isAlive()) {
			// wait thread stop Cancel already running
			// this to prevent massive click on cancel button and instantiate lots of threads.
		} else {
			Cancel cancel = new Cancel();
			BAG.getTasksContainer().setCancel(cancel);
			
			Thread newCalcelThread = new Thread(cancel, "cancelThread"); 
			BAG.getTasksContainer().setCancelThread(newCalcelThread);
			
			newCalcelThread.start();
		}
	}
	
}
