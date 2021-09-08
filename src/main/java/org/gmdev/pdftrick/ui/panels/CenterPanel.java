package org.gmdev.pdftrick.ui.panels;

import java.awt.*;
import java.awt.event.MouseListener;

import javax.swing.*;
import javax.swing.border.Border;

import org.gmdev.pdftrick.ui.actions.ImageAction;
import org.gmdev.pdftrick.ui.custom.WrapLayout;
import org.gmdev.pdftrick.utils.FileLoader;

import static org.gmdev.pdftrick.rendering.tasks.PageThumbnailsDisplayTask.NO_PICTURES;
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

	public void cleanSelection() {
		Border borderGray = BorderFactory.createLineBorder(Color.gray);
		Component[] components = centerPanel.getComponents();
		for (Component c : components) {
			if (!(c instanceof JLabel)) continue;

			JLabel picLabel = (JLabel) c;
			String name = picLabel.getName() != null ? picLabel.getName() : "";

			if (name.equals(NO_PICTURES)) continue;

			picLabel.setBorder(borderGray);
			picLabel.setOpaque(true);
			picLabel.setBackground(Color.WHITE);

			MouseListener[] mouseListeners = (picLabel.getListeners(MouseListener.class));
			if (mouseListeners.length == 0) continue;

			ImageAction imageAction = (ImageAction) mouseListeners[0];
			imageAction.setSelected(false);
		}
	}
	
	public JPanel getCenterPanel() {
		return centerPanel;
	}
	
	public JScrollPane getCenterScrollPanel() {
		return centerScrollPanel;
	}

}
