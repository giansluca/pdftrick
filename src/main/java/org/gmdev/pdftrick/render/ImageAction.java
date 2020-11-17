package org.gmdev.pdftrick.render;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.border.Border;

import org.gmdev.pdftrick.engine.ImageAttr.RenderedImageAttributes;
import org.gmdev.pdftrick.manager.PdfTrickBag;

public class ImageAction implements MouseListener {
	
	private static final PdfTrickBag BAG = PdfTrickBag.INSTANCE;
	
	private final JLabel picLabel;
	RenderedImageAttributes imageAttrs;
	private final boolean isInlineImg;
	private boolean selected;
	private final boolean enabled;
	
	public ImageAction(JLabel picLabel, RenderedImageAttributes imageAttrs, boolean isInlineImg, boolean selected) {
		this.picLabel = picLabel;
		this.imageAttrs = imageAttrs;
		this.isInlineImg = isInlineImg;
		this.selected = selected;
		this.enabled = true;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if (enabled) {
			final JTextField numImgSelected = BAG.getUserInterface().getRight().getNumImgSelectedField();
			final HashMap<String, RenderedImageAttributes> imageSelected = BAG.getSelectedImages();
			final HashMap<String, RenderedImageAttributes> inlineImgSelected = BAG.getInlineSelectedImages();
		
			Border borderGray = BorderFactory.createLineBorder(Color.gray);
			Border borderOrange = BorderFactory.createMatteBorder(2, 2, 2, 2, Color.orange);
		
			if (!selected) {
				picLabel.setBorder(borderOrange);
				selected = true;
				
				if (isInlineImg) {
					inlineImgSelected.put(imageAttrs.getKey(), imageAttrs);
				} else {
					imageSelected.put(imageAttrs.getKey(), imageAttrs);
				}
				
				numImgSelected.setText("Selected "+(imageSelected.size()+inlineImgSelected.size()));
			} else {
				picLabel.setBorder(borderGray);
				picLabel.setOpaque(false);
				picLabel.setBackground(Color.WHITE);
				selected = false;
				
				if (isInlineImg) {
					inlineImgSelected.remove(imageAttrs.getKey());
				} else {
					imageSelected.remove(imageAttrs.getKey());
				}
				
				if ((imageSelected.size() + inlineImgSelected.size()) == 0) {
					numImgSelected.setText("");
				} else {
					numImgSelected.setText("Selected "+(imageSelected.size()+inlineImgSelected.size()));
				}
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
