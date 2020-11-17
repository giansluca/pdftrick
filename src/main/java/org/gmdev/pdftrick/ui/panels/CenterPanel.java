package org.gmdev.pdftrick.ui.panels;

import java.awt.*;

import javax.swing.*;

import org.gmdev.pdftrick.ui.custom.WrapLayout;
import org.gmdev.pdftrick.utils.FileLoader;

import static org.gmdev.pdftrick.utils.Constants.WAIT;

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

	public void startWaitIconLoadPdf() {
		ImageIcon imageIcon = new ImageIcon(FileLoader.loadFileAsUrl(WAIT));
		JLabel waitLabel = new JLabel(imageIcon);

		waitLabel.setHorizontalAlignment(JLabel.CENTER);
		waitLabel.setVerticalAlignment(JLabel.CENTER);
		centerPanel.setLayout(new GridBagLayout());
		centerPanel.add(waitLabel);
		centerPanel.revalidate();
		centerPanel.repaint();
	}

	public void stopWaitIcon() {
		centerPanel.setLayout(new WrapLayout());
		centerPanel.removeAll();
		centerPanel.revalidate();
		centerPanel.repaint();
	}

	public void clean() {
		centerPanel.removeAll();
		centerPanel.revalidate();
		centerPanel.repaint();
	}
	
	public JPanel getCenterPanel() {
		return centerPanel;
	}
	
	public JScrollPane getCenterScrollPanel() {
		return centerScrollPanel;
	}

}
