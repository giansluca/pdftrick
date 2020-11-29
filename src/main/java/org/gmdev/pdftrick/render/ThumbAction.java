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
import org.gmdev.pdftrick.tasks.PageThumbnailsDisplayTask;
import org.gmdev.pdftrick.ui.custom.WrapLayout;
import org.gmdev.pdftrick.ui.panels.CenterPanel;

public class ThumbAction implements MouseListener {
	
	private static final PdfTrickBag BAG = PdfTrickBag.INSTANCE;
	
	private final int pageNumber;
	
	public ThumbAction(int pageNumber) {
		this.pageNumber = pageNumber;
	}
	
	/**
	 * Called when the user click on a page pdf cover
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		int selectedPage = BAG.getSelectedPage();
		JPanel leftPanel = BAG.getUserInterface().getLeft().getLeftPanel();
		JTextField currentPageField = BAG.getUserInterface().getRight().getCurrentPageField();
		CenterPanel centerPanel = BAG.getUserInterface().getCenter();
		
		Border borderGray = BorderFactory.createLineBorder(Color.gray);
		Border borderGreen = BorderFactory.createMatteBorder(2, 2, 2, 2, Color.green);
		
		if (selectedPage != 0) {
			JLabel picLabelSelected = (JLabel) leftPanel.getComponent(selectedPage);
			picLabelSelected.setBorder(borderGray);
		}
		
		JLabel picLabel = (JLabel) leftPanel.getComponent(pageNumber - 1);
		
		// deselect page
		if (selectedPage == pageNumber - 1) {
			picLabel.setBorder(borderGray);
			BAG.setSelectedPage(0);
			currentPageField.setText("");
			centerPanel.clean();
			
		// select page	
		} else {
			// when previous page selected don't have images i need to reset the layout to FlowLayout 
			// (the same layout with wait icon during pdf rendering)
			JPanel jCenterPanel = centerPanel.getCenterPanel();
			
			if (jCenterPanel.getLayout() instanceof GridBagLayout) {
				jCenterPanel.setLayout(new WrapLayout());
			}
			
			picLabel.setBorder(borderGreen);
			BAG.setSelectedPage(pageNumber - 1);
			currentPageField.setText("Page " + pageNumber);
			centerPanel.clean();
			
			PageThumbnailsDisplayTask pageThumbnailsDisplayTask = new PageThumbnailsDisplayTask(pageNumber);
			BAG.getTasksContainer().setPageThumbnailsDisplayTask(pageThumbnailsDisplayTask);
			
			Thread pageThumbnailsDisplayThread = new Thread(pageThumbnailsDisplayTask);
			BAG.getTasksContainer().setPageThumbnailsDisplayThread(pageThumbnailsDisplayThread);
			pageThumbnailsDisplayThread.start();

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
