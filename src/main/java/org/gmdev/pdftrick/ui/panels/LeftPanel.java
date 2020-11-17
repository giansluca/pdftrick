package org.gmdev.pdftrick.ui.panels;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import org.gmdev.pdftrick.ui.actions.DragAndDropAction;
import org.gmdev.pdftrick.ui.custom.WrapLayout;
import org.gmdev.pdftrick.utils.external.FileDrop;

public class LeftPanel {
	
	private final JPanel leftPanel;
    private final JScrollPane leftScrollPanel;
	
    public LeftPanel() {
    	leftPanel = new JPanel();
 		leftPanel.setLayout(new WrapLayout());
 		leftPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        leftPanel.setBackground(Color.WHITE);
        leftScrollPanel = new JScrollPane();
        leftScrollPanel.setViewportView(leftPanel);
        leftScrollPanel.getVerticalScrollBar().setUnitIncrement(10);
        
        new FileDrop(leftPanel, new DragAndDropAction());
	}

	public void clean() {
		FileDrop.remove(leftPanel);
		leftPanel.removeAll();
		leftPanel.setBorder(new EmptyBorder(0, 0, 0, 0));

		new FileDrop(leftPanel, new DragAndDropAction());
		leftPanel.revalidate();
		leftPanel.repaint();
	}

	/**
	 * Remove the file drop blue border, in some circumstances it is needed under windows OS
	 */
	public void resetLeftPanelFileDropBorder() {
		leftPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
	}
    
    public JPanel getLeftPanel() {
		return leftPanel;
	}

	public JScrollPane getLeftScrollPanel() {
		return leftScrollPanel;
	}
	
}
