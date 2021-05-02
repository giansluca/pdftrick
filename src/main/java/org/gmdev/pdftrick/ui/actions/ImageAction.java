package org.gmdev.pdftrick.ui.actions;

import java.awt.Color;
import java.awt.event.*;
import java.util.HashMap;

import javax.swing.*;
import javax.swing.border.Border;

import org.gmdev.pdftrick.rendering.ImageAttr.RenderedImageAttributes;
import org.gmdev.pdftrick.manager.PdfTrickBag;

public class ImageAction implements MouseListener {
	
	private static final PdfTrickBag bag = PdfTrickBag.INSTANCE;
	
	private final JLabel picLabel;
	private final RenderedImageAttributes imageAttributes;
	private final boolean isInlineImg;
	private boolean selected;
	private final boolean enabled;
	
	public ImageAction(
			JLabel picLabel, RenderedImageAttributes imageAttributes, boolean isInlineImg, boolean selected) {

		this.picLabel = picLabel;
		this.imageAttributes = imageAttributes;
		this.isInlineImg = isInlineImg;
		this.selected = selected;
		this.enabled = true;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if (enabled) {
			JTextField numImgSelected = bag.getUserInterface().getRight().getSelectedImagesField();
			HashMap<String, RenderedImageAttributes> imageSelected = bag.getSelectedImages();
			HashMap<String, RenderedImageAttributes> inlineImgSelected = bag.getInlineSelectedImages();
		
			Border borderGray = BorderFactory.createLineBorder(Color.gray);
			Border borderOrange = BorderFactory.createMatteBorder(2, 2, 2, 2, Color.orange);
		
			if (!selected) {
				picLabel.setBorder(borderOrange);
				selected = true;
				
				if (isInlineImg)
					inlineImgSelected.put(imageAttributes.getKey(), imageAttributes);
				else
					imageSelected.put(imageAttributes.getKey(), imageAttributes);
				
				numImgSelected.setText("Selected "+(imageSelected.size()+inlineImgSelected.size()));
			} else {
				picLabel.setBorder(borderGray);
				picLabel.setOpaque(false);
				picLabel.setBackground(Color.WHITE);
				selected = false;
				
				if (isInlineImg)
					inlineImgSelected.remove(imageAttributes.getKey());
				else
					imageSelected.remove(imageAttributes.getKey());
				
				if ((imageSelected.size() + inlineImgSelected.size()) == 0)
					numImgSelected.setText("");
				else
					numImgSelected.setText("Selected "+(imageSelected.size()+inlineImgSelected.size()));
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}
	
	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	

}
