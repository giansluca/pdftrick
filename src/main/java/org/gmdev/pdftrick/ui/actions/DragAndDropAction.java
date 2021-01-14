package org.gmdev.pdftrick.ui.actions;

import java.io.File;

import org.gmdev.pdftrick.engine.FileIn;
import org.gmdev.pdftrick.utils.external.FileDrop;

/**
 * Action called when the pdf file il loaded with drag and drop
 */
public class DragAndDropAction implements FileDrop.Listener, FileIn {
	
	@Override
	public void filesDropped(File[] files) {
		start(files);
	}

}
