package org.gmdev.pdftrick.ui.actions;

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
import org.gmdev.pdftrick.rendering.tasks.PageThumbnailsDisplayTask;
import org.gmdev.pdftrick.rendering.tasks.PageThumbnailsDisplayTask_7;
import org.gmdev.pdftrick.serviceprocessor.ServiceScheduler;
import org.gmdev.pdftrick.ui.custom.WrapLayout;
import org.gmdev.pdftrick.ui.panels.CenterPanel;

public class PdfPageAction implements MouseListener {
	
	private static final PdfTrickBag bag = PdfTrickBag.INSTANCE;
	
	private final int pageNumber;
	
	public PdfPageAction(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int selectedPage = bag.getSelectedPage();
		JPanel leftPanel = bag.getUserInterface().getLeft().getLeftPanel();
		JTextField currentPageField = bag.getUserInterface().getRight().getCurrentPageField();
		CenterPanel centerPanel = bag.getUserInterface().getCenter();
		
		Border borderGray = BorderFactory.createLineBorder(Color.gray);
		Border borderGreen = BorderFactory.createMatteBorder(2, 2, 2, 2, Color.green);
		
		if (selectedPage != 0) {
			JLabel picLabelSelected = (JLabel) leftPanel.getComponent(selectedPage - 1);
			picLabelSelected.setBorder(borderGray);
		}
		
		JLabel picLabel = (JLabel) leftPanel.getComponent(pageNumber - 1);
		
		// deselect page
		if (selectedPage == pageNumber) {
			picLabel.setBorder(borderGray);
			bag.setSelectedPage(0);
			currentPageField.setText("");
			centerPanel.clean();
			
		// select page	
		} else {
			// when previous page selected don't have images i need to reset the layout to FlowLayout 
			// (the same layout with wait icon during pdf rendering)
			JPanel jCenterPanel = centerPanel.getCenterPanel();
			
			if (jCenterPanel.getLayout() instanceof GridBagLayout)
				jCenterPanel.setLayout(new WrapLayout());

			picLabel.setBorder(borderGreen);
			bag.setSelectedPage(pageNumber);
			currentPageField.setText("Page " + pageNumber);
			centerPanel.clean();
			
			PageThumbnailsDisplayTask pageThumbnailsDisplayTask = new PageThumbnailsDisplayTask(pageNumber);
			bag.getTasksContainer().setPageThumbnailsDisplayTask(pageThumbnailsDisplayTask);
			
			Thread pageThumbnailsDisplayThread = new Thread(pageThumbnailsDisplayTask);
			bag.getTasksContainer().setPageThumbnailsDisplayThread(pageThumbnailsDisplayThread);
			pageThumbnailsDisplayThread.start();

			// TODO Itext 7 migration
			var renderPageThumbnailsTask = new PageThumbnailsDisplayTask_7(pageNumber);
			ServiceScheduler.getServiceScheduler().schedule(renderPageThumbnailsTask);
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
