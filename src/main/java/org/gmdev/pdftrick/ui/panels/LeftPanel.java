package org.gmdev.pdftrick.ui.panels;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.gmdev.pdftrick.ui.actions.DragAndDropAction;
import org.gmdev.pdftrick.ui.custom.WrapLayout;
import org.gmdev.pdftrick.utils.FileDrop;

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
    
    public JPanel getLeftPanel() {
		return leftPanel;
	}

	public JScrollPane getLeftScrollPanel() {
		return leftScrollPanel;
	}
	
}
