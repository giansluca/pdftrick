package org.gmdev.pdftrick.ui.custom;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.gmdev.pdftrick.utils.Constants;
import org.gmdev.pdftrick.utils.FileLoader;

public class GlassPane extends JPanel implements MouseListener, KeyListener {
	
	private static final long serialVersionUID = -2219084606428817436L;
	
	private JLabel thumbSpinner;
	private JLabel extractSpinner;
	
	public GlassPane() {
		initThumbSpinner();
		initExtractSpinner();
		addKeyListener(this);
		addMouseListener(this);
		setCursor(new Cursor(Cursor.WAIT_CURSOR));
		setOpaque(false);
	}
	
	/**
	 * Set icon wait for thumb mode (green)
	 */
	private void initThumbSpinner() {	
		ImageIcon imageIcon = new ImageIcon(FileLoader.loadFileAsUrl(Constants.WAIT_IMG));
		
		thumbSpinner = new JLabel();
		thumbSpinner.setHorizontalAlignment(JLabel.CENTER);
		thumbSpinner.setHorizontalTextPosition(JLabel.CENTER);
		thumbSpinner.setVerticalTextPosition(JLabel.BOTTOM);
		thumbSpinner.setFont(new Font("loading", Font.PLAIN, 13));
		thumbSpinner.setForeground(Color.decode("#506D00"));
		thumbSpinner.setIcon(imageIcon);
		thumbSpinner.setText("loading");	
	}
	
	/**
	 * Set icon wait for extract mode (red)
	 */
	private void initExtractSpinner() {
		ImageIcon imageIcon = new ImageIcon(FileLoader.loadFileAsUrl(Constants.WAIT_EXT));
		
		extractSpinner = new JLabel();
		extractSpinner.setHorizontalAlignment(JLabel.CENTER);
		extractSpinner.setHorizontalTextPosition(JLabel.CENTER);
		extractSpinner.setVerticalTextPosition(JLabel.BOTTOM);
		extractSpinner.setFont(new Font("loading", Font.PLAIN, 13));
		extractSpinner.setForeground(Color.decode("#D62828"));
		extractSpinner.setIcon(imageIcon);
		extractSpinner.setText("extracting");
	}
	
	@Override
	public void keyPressed(final KeyEvent pArg0) {}
		
	@Override
	public void keyReleased(final KeyEvent pArg0) {}
		
	@Override
	public void keyTyped(final KeyEvent pArg0) {}
		
	@Override
	public void mouseClicked(final MouseEvent pArg0) {}
		
	@Override
	public void mouseEntered(final MouseEvent pArg0) {}
		
	@Override
	public void mouseExited(final MouseEvent pArg0) {}
		
	@Override
	public void mousePressed(final MouseEvent pArg0) {}
		
	@Override
	public void mouseReleased(final MouseEvent pArg0) {}
	
	public JLabel getThumbSpinner() {
		return thumbSpinner;
	}
	
	public JLabel getExtractSpinner() {
		return extractSpinner;
	}
	
	
		
}


