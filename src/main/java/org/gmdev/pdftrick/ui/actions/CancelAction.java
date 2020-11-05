package org.gmdev.pdftrick.ui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.gmdev.pdftrick.manager.PdfTrickBag;
import org.gmdev.pdftrick.thread.Cancel;

public class CancelAction extends AbstractAction {
	
	private static final long serialVersionUID = 5288882061002308348L;
	private static final PdfTrickBag factory = PdfTrickBag.getBag();
	
	public CancelAction() {
	}
	
	/**
	 * Called from the CANCEL SELECTION button, clean everything
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (factory.getThreadContainer().getCancelThread() !=null && factory.getThreadContainer().getCancelThread().isAlive()) {
			// wait thread stop Cancel already running
			// this to prevent massive click on cancel button and instantiate lots of threads.
		} else {
			Cancel cancel = new Cancel();
			factory.getThreadContainer().setCancel(cancel);
			
			Thread newCalcelThread = new Thread(cancel, "cancelThread"); 
			factory.getThreadContainer().setCancelThread(newCalcelThread);
			
			newCalcelThread.start();
		}
	}
	
}
