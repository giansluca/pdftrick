package org.gmdev.pdftrick.ui.actions;

import java.io.File;

import org.gmdev.pdftrick.manager.*;
import org.gmdev.pdftrick.tasks.DragAndDropTask;
import org.gmdev.pdftrick.utils.external.FileDrop;

public class DragAndDropAction implements FileDrop.Listener, FileIn {
	
	private static final PdfTrickBag BAG = PdfTrickBag.INSTANCE;

	@Override
	public void filesDropped(File[] files) {
		TasksContainer tasksContainer = BAG.getTasksContainer();

		beforeLoadingCheck(BAG);

		DragAndDropTask newDragAndDropTask = new DragAndDropTask(files);
		tasksContainer.setDragAndDropTask(newDragAndDropTask);
		
		Thread dragAndDropThread = new Thread(newDragAndDropTask);
		tasksContainer.setDragAndDropThread(dragAndDropThread);
		dragAndDropThread.start();
	}

}
