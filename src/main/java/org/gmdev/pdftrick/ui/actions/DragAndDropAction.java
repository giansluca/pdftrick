package org.gmdev.pdftrick.ui.actions;

import java.io.File;

import org.gmdev.pdftrick.manager.*;
import org.gmdev.pdftrick.tasks.DragAndDropTask;
import org.gmdev.pdftrick.engine.FileIn;
import org.gmdev.pdftrick.utils.external.FileDrop;

public class DragAndDropAction implements FileDrop.Listener, FileIn {
	
	private static final PdfTrickBag bag = PdfTrickBag.INSTANCE;

	@Override
	public void filesDropped(File[] files) {
		TasksContainer tasksContainer = bag.getTasksContainer();

		beforeLoadingCheck();

		DragAndDropTask newDragAndDropTask = new DragAndDropTask(files);
		tasksContainer.setDragAndDropTask(newDragAndDropTask);
		
		Thread dragAndDropThread = new Thread(newDragAndDropTask);
		tasksContainer.setDragAndDropThread(dragAndDropThread);
		dragAndDropThread.start();
	}

}
