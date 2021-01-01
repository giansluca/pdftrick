package org.gmdev.pdftrick.ui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.gmdev.pdftrick.manager.PdfTrickBag;
import org.gmdev.pdftrick.manager.TasksContainer;
import org.gmdev.pdftrick.tasks.CancelTask;

/**
 * Action called when 'Cancel' button is clicked
 */
public class CancelAction extends AbstractAction {
	
	private static final PdfTrickBag bag = PdfTrickBag.INSTANCE;

	@Override
	public void actionPerformed(ActionEvent event) {
		TasksContainer tasksContainer = bag.getTasksContainer();
		var cancelTask = tasksContainer.getCancelTask();
		if (cancelTask != null && cancelTask.isRunning()) return;

		CancelTask newCancelTask = new CancelTask();
		tasksContainer.setCancelTask(newCancelTask);
			
		Thread cancelThread = new Thread(newCancelTask);
		tasksContainer.setCancelThread(cancelThread);
		cancelThread.start();
	}
	
}
