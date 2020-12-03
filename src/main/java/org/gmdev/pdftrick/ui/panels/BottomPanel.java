package org.gmdev.pdftrick.ui.panels;

import java.awt.*;

import javax.swing.*;
import javax.swing.text.DefaultCaret;

public class BottomPanel {
	
	private final JPanel bottomPanel;
    private final JTextArea textArea;

	public BottomPanel() {
		bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.setBackground(Color.WHITE);
        
        textArea = new JTextArea();
		textArea.setText("");
		textArea.setEditable(false);
		textArea.setLineWrap(true);
		textArea.setBackground(Color.WHITE);

		JScrollPane textAreaScrollPane = new JScrollPane();
		textAreaScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		textAreaScrollPane.setViewportView(textArea);
		DefaultCaret caret = (DefaultCaret) textArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
		bottomPanel.add(textAreaScrollPane, BorderLayout.CENTER);
	}

	public void clean() {
		textArea.setText("");
	}
	
	public JPanel getBottomPanel() {
		return bottomPanel;
	}
	
	public JTextArea getTextArea() {
		return textArea;
	}

}
