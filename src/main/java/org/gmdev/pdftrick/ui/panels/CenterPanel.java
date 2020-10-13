package org.gmdev.pdftrick.ui.panels;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.gmdev.pdftrick.ui.custom.WrapLayout;

public class CenterPanel {
	
	private final JPanel centerPanel;
	private final JScrollPane centerScrollPanel;
	
	public CenterPanel() {
		centerPanel = new JPanel();
		centerPanel.setLayout(new WrapLayout());
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        centerPanel.setBackground(Color.WHITE);
        centerScrollPanel = new JScrollPane();
        centerScrollPanel.setViewportView(centerPanel);
        centerScrollPanel.getVerticalScrollBar().setUnitIncrement(20);
	}
	
	public JPanel getCenterPanel() {
		return centerPanel;
	}
	
	public JScrollPane getCenterScrollPanel() {
		return centerScrollPanel;
	}

}
