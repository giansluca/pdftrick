package org.gmdev.pdftrick.render;

import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

import org.gmdev.pdftrick.factory.PdfTrickFactory;
import org.gmdev.pdftrick.thread.ImgThumb;
import org.gmdev.pdftrick.ui.custom.WrapLayout;
import org.gmdev.pdftrick.utils.PdfTrickUtils;

public class ThumbAction implements MouseListener {
	
	private static final PdfTrickFactory factory = PdfTrickFactory.getFactory();
	
	private final int numberPage;
	
	public ThumbAction(int numberPage) {
		this.numberPage = numberPage;
	}
	
	/**
	 * Called when the user click on a page pdf cover
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		final String selected = factory.getSelected();
		final JPanel leftPanel = factory.getUserInterface().getLeft().getLeftPanel();
		final JTextField currentPageField = factory.getUserInterface().getRight().getCurrentPageField();
		
		Border borderGray = BorderFactory.createLineBorder(Color.gray);
		Border borderGreen = BorderFactory.createMatteBorder(2, 2, 2, 2, Color.green);
		
		if (!selected.equalsIgnoreCase("")) {
			JLabel picLabelSelected = (JLabel) leftPanel.getComponent(Integer.parseInt(selected));
			picLabelSelected.setBorder(borderGray);
		}
		
		JLabel picLabel = (JLabel) leftPanel.getComponent(numberPage-1);
		
		// deselect page
		if (selected.equalsIgnoreCase(String.valueOf(numberPage-1))) {
			picLabel.setBorder(borderGray);
			factory.setSelected("");
			currentPageField.setText("");
			PdfTrickUtils.cleanCenterPanel();
			
		// select page	
		} else {
			// when previous page selected don't have images i need to reset the layout to FlowLayout 
			// (the same layout with wait icon during pdf rendering)
			final JPanel centerPanel = factory.getUserInterface().getCenter().getCenterPanel();
			
			if (centerPanel.getLayout() instanceof GridBagLayout) {
				centerPanel.setLayout(new WrapLayout());
			}
			
			picLabel.setBorder(borderGreen);
			factory.setSelected(String.valueOf(numberPage-1));
			currentPageField.setText("Page "+numberPage);
			PdfTrickUtils.cleanCenterPanel();
			
			ImgThumb imgThumb = new ImgThumb(numberPage);
			factory.gettContainer().setImgThumb(imgThumb);
			
			Thread imgThumbThread = new Thread(imgThumb, "imgThumbThread");
			factory.gettContainer().setImgThumbThread(imgThumbThread);
			imgThumbThread.start();
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}


}
