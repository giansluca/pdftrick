package org.gmdev.pdftrick.ui.actions;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.Properties;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

import org.gmdev.pdftrick.engine.ImageAttr.RenderedImageAttributes;
import org.gmdev.pdftrick.manager.PdfTrickBag;
import org.gmdev.pdftrick.render.ImageAction;
import org.gmdev.pdftrick.utils.Messages;

public class CleanSelectionAction extends AbstractAction {
	
	private static final PdfTrickBag BAG = PdfTrickBag.INSTANCE;
	
	/**
	 * Called from the CLEAN SELECTION button, clean image selection
	 */
	@Override
	public void actionPerformed(ActionEvent event) {
		Properties messagesProps = BAG.getMessagesProps();
		JPanel centerPanel = BAG.getUserInterface().getCenter().getCenterPanel();
		JTextField numImgSelectedField = BAG.getUserInterface().getRight().getNumImgSelectedField();
		HashMap<String, RenderedImageAttributes> inlineImgSelected = BAG.getInlineSelectedImages();
		
		if (BAG.getTasksContainer().getImgExtractionThread() != null &&
				BAG.getTasksContainer().getImgExtractionThread().isAlive()) {

			Messages.append("WARNING", messagesProps.getProperty("tmsg_02"));
			return;
		}
		
		if (BAG.getTasksContainer().getImgThumbThread() !=null &&
				BAG.getTasksContainer().getImgThumbThread().isAlive()) {

			Messages.append("WARNING", messagesProps.getProperty("tmsg_23"));
			return;
		}
		
		if (BAG.getTasksContainer().getOpenFileChooserThread() != null &&
				BAG.getTasksContainer().getOpenFileChooserThread().isAlive()) {
			return;
		}
		
		if (BAG.getTasksContainer().getDragAnDropFileChooserThread() != null &&
				BAG.getTasksContainer().getDragAnDropFileChooserThread().isAlive()) {
			return;
		}
		
		if (BAG.getTasksContainer().getShowThumbsThread() != null &&
				BAG.getTasksContainer().getShowThumbsThread().isAlive()) {
			return;
		}
		
		if (BAG.getSelectedImages().size() == 0 && inlineImgSelected.size() == 0) {
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
			
			BAG.cleanSelectedImagesHashMap();
			BAG.cleanInlineSelectedImagesHashMap();
			numImgSelectedField.setText("");
		}
		
	}
	
}
