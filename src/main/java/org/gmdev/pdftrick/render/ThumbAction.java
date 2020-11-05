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

import org.gmdev.pdftrick.manager.PdfTrickBag;
import org.gmdev.pdftrick.thread.ImgThumb;
import org.gmdev.pdftrick.ui.custom.WrapLayout;
import org.gmdev.pdftrick.utils.Utils;

public class ThumbAction implements MouseListener {
	
	private static final PdfTrickBag bag = PdfTrickBag.getBag();
	
	private final int pageNumber;
	
	public ThumbAction(int pageNumber) {
		this.pageNumber = pageNumber;
	}
	
	/**
	 * Called when the user click on a page pdf cover
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		final String selected = bag.getSelected();
		final JPanel leftPanel = bag.getUserInterface().getLeft().getLeftPanel();
		final JTextField currentPageField = bag.getUserInterface().getRight().getCurrentPageField();
		
		Border borderGray = BorderFactory.createLineBorder(Color.gray);
		Border borderGreen = BorderFactory.createMatteBorder(2, 2, 2, 2, Color.green);
		
		if (!selected.equalsIgnoreCase("")) {
			JLabel picLabelSelected = (JLabel) leftPanel.getComponent(Integer.parseInt(selected));
			picLabelSelected.setBorder(borderGray);
		}
		
		JLabel picLabel = (JLabel) leftPanel.getComponent(pageNumber - 1);
		
		// deselect page
		if (selected.equalsIgnoreCase(String.valueOf(pageNumber - 1))) {
			picLabel.setBorder(borderGray);
			bag.setSelected("");
			currentPageField.setText("");
			Utils.cleanCenterPanel();
			
		// select page	
		} else {
			// when previous page selected don't have images i need to reset the layout to FlowLayout 
			// (the same layout with wait icon during pdf rendering)
			final JPanel centerPanel = bag.getUserInterface().getCenter().getCenterPanel();
			
			if (centerPanel.getLayout() instanceof GridBagLayout) {
				centerPanel.setLayout(new WrapLayout());
			}
			
			picLabel.setBorder(borderGreen);
			bag.setSelected(String.valueOf(pageNumber - 1));
			currentPageField.setText("Page " + pageNumber);
			Utils.cleanCenterPanel();
			
			ImgThumb imgThumb = new ImgThumb(pageNumber);
			bag.getThreadContainer().setImgThumb(imgThumb);
			
			Thread imgThumbThread = new Thread(imgThumb, "imgThumbThread");
			bag.getThreadContainer().setImgThumbThread(imgThumbThread);
			imgThumbThread.start();

			// TODO Itext 7 migration
			//var renderPageThumbnailsTask = new RenderPageThumbnailsTask(pageNumber);
			//ServiceScheduler.getServiceScheduler().schedule(renderPageThumbnailsTask);
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
