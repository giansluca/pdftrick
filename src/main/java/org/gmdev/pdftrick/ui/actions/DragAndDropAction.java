package org.gmdev.pdftrick.ui.actions;

import java.io.File;

import org.gmdev.pdftrick.checking.FileIn;
import org.gmdev.pdftrick.utils.external.FileDrop;

public class DragAndDropAction implements FileDrop.Listener, FileIn {
	
	@Override
	public void filesDropped(File[] files) {
		start(files);
	}

}
