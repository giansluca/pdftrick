package org.gmdev.pdftrick.ui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.gmdev.pdftrick.factory.PdfTrickFactory;
import org.gmdev.pdftrick.thread.Cancel;

public class CancelAction extends AbstractAction {
	
	private static final long serialVersionUID = 5288882061002308348L;
	private static final PdfTrickFactory factory = PdfTrickFactory.getFactory();
	
	public CancelAction() {
	}
	
	/**
	 * Called from the CANCEL SELECTION button, clean everything
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (factory.gettContainer().getCancelThread() !=null && factory.gettContainer().getCancelThread().isAlive()) {
			// wait thread stop Cancel already running
			// this to prevent massive click on cancel button and instantiate lots of threads.
		} else {
			Cancel cancel = new Cancel();
			factory.gettContainer().setCancel(cancel);
			
			Thread newCalcelThread = new Thread(cancel, "cancelThread"); 
			factory.gettContainer().setCancelThread(newCalcelThread);
			
			newCalcelThread.start();
		}
	}
	
}
