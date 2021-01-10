package org.gmdev.pdftrick.ui.custom;

import java.awt.*;
import java.awt.event.*;
import java.io.File;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;

import org.gmdev.pdftrick.utils.*;

public class CustomFileChooser extends JFileChooser {
	
	private static final long serialVersionUID = -5896368165854589643L;
	private final ImageIcon up_icon = new ImageIcon(FileLoader.loadFileAsUrl(Constants.UP_ICO));
	private final ImageIcon home_icon = new ImageIcon(FileLoader.loadFileAsUrl(Constants.HOME_ICO));
	private final ImageIcon desktop_icon = new ImageIcon(FileLoader.loadFileAsUrl(Constants.DESKTOP_ICO));
	
	public CustomFileChooser() {
		if (SetupUtils.isMac()) {
			setupMac();
		} else if (SetupUtils.isWindows()) {
			setupWin();
		}
	}
	
	/**
	 * Setup a custom JFileChooser for mac platform
	 */
	public void setupMac() {
		final JTextField tf = getTF(this);
		tf.setEditable(false);
		
		addPropertyChangeListener(DIRECTORY_CHANGED_PROPERTY,
				arg0 -> tf.setText(getCurrentDirectory().getName()));
		
		setAcceptAllFileFilterUsed(false);
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setPreferredSize(new Dimension(80, 120));
		
		final JLabel parentDir = new JLabel();		
		parentDir.setIcon(up_icon);
		parentDir.setFocusable(false);
		parentDir.setMaximumSize(new Dimension(40, 40));
		parentDir.setAlignmentX(JButton.CENTER_ALIGNMENT);
		parentDir.setHorizontalAlignment(JLabel.CENTER);
		parentDir.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {}
			@Override
			public void mousePressed(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseClicked(MouseEvent e) {
				File f = getCurrentDirectory();
				setCurrentDirectory(f.getParentFile());
			}
		});
		
		final JLabel homeDir = new JLabel();
		homeDir.setIcon(home_icon);
		homeDir.setFocusable(false);
		homeDir.setMaximumSize(new Dimension(40, 40));
		homeDir.setAlignmentX(JButton.CENTER_ALIGNMENT);
		homeDir.setHorizontalAlignment(JLabel.CENTER);
		homeDir.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {}
			@Override
			public void mousePressed(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseClicked(MouseEvent e) {
				FileSystemView filesys = FileSystemView.getFileSystemView();
				File f = filesys.getHomeDirectory();
				setCurrentDirectory(f);
			}
		});
		
		final JLabel descktopDir = new JLabel();
		descktopDir.setIcon(desktop_icon);
		descktopDir.setFocusable(false);
		descktopDir.setMaximumSize(new Dimension(40, 40));
		descktopDir.setAlignmentX(JButton.CENTER_ALIGNMENT);
		descktopDir.setHorizontalAlignment(JLabel.CENTER);
		descktopDir.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {}
			@Override
			public void mousePressed(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseClicked(MouseEvent e) {
				FileSystemView filesys = FileSystemView.getFileSystemView();
				String path = filesys.getHomeDirectory().getPath();
				File f = new File (path+File.separator+"Desktop");
				if (f.exists()) {
					setCurrentDirectory(f);
				}
			}
		});
		
		panel.add(parentDir);
		panel.add(Box.createRigidArea(new Dimension(3, 3)));
		panel.add(homeDir);
		panel.add(Box.createRigidArea(new Dimension(3, 3)));
		panel.add(descktopDir);
		setAccessory(panel);
		setPreferredSize(new Dimension(700, 500));
	}
	
	/**
	 * Setup a custom JFileChooser for win platform
	 */
	public void setupWin() {
		setPreferredSize(new Dimension(700, 500));
	}
	
	/**
	 * Get JTextField in a JFileChooser (used for customization)
	 */
	public JTextField getTF(Container c) {
	    Component[] cmps = c.getComponents();
	    JTextField tf = null;
	    
	    for (Component cmp : cmps) {
	        if (cmp instanceof JTextField) {
	        	tf = (JTextField) cmp;
	        	return tf;
	        }
	        
	        if (cmp instanceof Container) {
	        	tf = getTF((Container) cmp);
	        	if (tf != null) return tf;
	        }
	    }
	    
	    return tf;
	}
	
	/**
	 * Get first JComboBox in a JFileChooser (not used now)
	 */
	public JComboBox<?> getCB(Container c) {
		Component[] cmps = c.getComponents();
		JComboBox<?> cb = null;
		
		for (Component cmp : cmps) {
			if (cmp instanceof JComboBox) {
				cb = (JComboBox<?>) cmp;
	        	return cb;
			}
			
			if (cmp instanceof Container) {
				cb = getCB((Container) cmp);
				if (cb != null) return cb;
			}
		}
		
		return cb;
	}
	

}
