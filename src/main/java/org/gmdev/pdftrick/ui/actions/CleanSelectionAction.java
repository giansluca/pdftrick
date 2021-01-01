package org.gmdev.pdftrick.ui.actions;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.border.Border;

import org.gmdev.pdftrick.engine.ImageAttr.RenderedImageAttributes;
import org.gmdev.pdftrick.manager.*;
import org.gmdev.pdftrick.render.ImageAction;
import org.gmdev.pdftrick.utils.Messages;

public class CleanSelectionAction extends AbstractAction {
	
	private static final PdfTrickBag bag = PdfTrickBag.INSTANCE;

	@Override
	public void actionPerformed(ActionEvent event) {
		Properties messagesProps = bag.getMessagesProps();
		JPanel centerPanel = bag.getUserInterface().getCenter().getCenterPanel();
		JTextField numImgSelectedField = bag.getUserInterface().getRight().getSelectedImagesField();
		HashMap<String, RenderedImageAttributes> inlineImgSelected = bag.getInlineSelectedImages();
		TasksContainer tasksContainer = bag.getTasksContainer();
		
		if (tasksContainer.getImagesExtractionThread() != null &&
				tasksContainer.getImagesExtractionThread().isAlive()) {

			Messages.append("WARNING", messagesProps.getProperty("tmsg_02"));
			return;
		}
		
		if (tasksContainer.getPageThumbnailsDisplayThread() !=null &&
				tasksContainer.getPageThumbnailsDisplayThread().isAlive()) {

			Messages.append("WARNING", messagesProps.getProperty("tmsg_23"));
			return;
		}
		
		if (tasksContainer.getFileChooserThread() != null &&
				tasksContainer.getFileChooserThread().isAlive()) {
			return;
		}
		
		if (tasksContainer.getDragAndDropThread() != null &&
				tasksContainer.getDragAndDropThread().isAlive()) {
			return;
		}
		
		if (tasksContainer.getPdfCoverThumbnailsDisplayThread() != null &&
				tasksContainer.getPdfCoverThumbnailsDisplayThread().isAlive()) {
			return;
		}
		
		if (bag.getSelectedImages().size() == 0 && inlineImgSelected.size() == 0) {
			Messages.append("INFO", messagesProps.getProperty("tmsg_24"));
		} else {
			Border borderGray = BorderFactory.createLineBorder(Color.gray);
			Component[] comps =  centerPanel.getComponents();
			Component component;

			for (Component comp : comps) {
				component = comp;

				if (component instanceof JLabel) {
					JLabel picLabel = (JLabel) comp;
					String name = "" + picLabel.getName();

					if (!name.equalsIgnoreCase("NoPicsImg")) {
						picLabel.setBorder(borderGray);
						picLabel.setOpaque(true);
						picLabel.setBackground(Color.WHITE);
						MouseListener[] mls = (picLabel.getListeners(MouseListener.class));

						if (mls.length > 0) {
							ImageAction act = (ImageAction) mls[0];
							act.setSelected(false);
						}
					}
				}
			}
			
			bag.cleanSelectedImagesHashMap();
			bag.cleanInlineSelectedImagesHashMap();
			numImgSelectedField.setText("");
		}
	}

	
}
