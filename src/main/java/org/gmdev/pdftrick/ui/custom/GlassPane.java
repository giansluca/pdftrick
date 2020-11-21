package org.gmdev.pdftrick.ui.custom;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import org.gmdev.pdftrick.utils.FileLoader;

import static org.gmdev.pdftrick.utils.Constants.WAIT_EXT;
import static org.gmdev.pdftrick.utils.Constants.WAIT_IMG;

public class GlassPane extends JPanel implements MouseListener, KeyListener {

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
	 * Set green spinner during page thumbnails loading
	 */
	private void initThumbSpinner() {	
		ImageIcon imageIcon = new ImageIcon(FileLoader.loadFileAsUrl(WAIT_IMG));
		
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
	 * Set red spinner during image extraction
	 */
	private void initExtractSpinner() {
		ImageIcon imageIcon = new ImageIcon(FileLoader.loadFileAsUrl(WAIT_EXT));
		
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


