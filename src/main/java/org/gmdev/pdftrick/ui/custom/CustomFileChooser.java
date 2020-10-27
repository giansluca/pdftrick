package org.gmdev.pdftrick.ui.custom;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileSystemView;

import org.gmdev.pdftrick.utils.Constants;
import org.gmdev.pdftrick.utils.FileLoader;
import org.gmdev.pdftrick.utils.SetupUtils;

public class CustomFileChooser extends JFileChooser {
	
	private static final long serialVersionUID = -5896368165854589643L;
	private final ImageIcon up_icon = new ImageIcon(FileLoader.loadAsUrl(Constants.UP_ICO));
	private final ImageIcon home_icon = new ImageIcon(FileLoader.loadAsUrl(Constants.HOME_ICO));
	private final ImageIcon desktop_icon = new ImageIcon(FileLoader.loadAsUrl(Constants.DESKTOP_ICO));
	
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
		
		addPropertyChangeListener(DIRECTORY_CHANGED_PROPERTY, new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent arg0) {
				tf.setText(getCurrentDirectory().getName());
				
			}
		});
		
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
	 * Get JTextField in a JfileChooser (used for customization)
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
	        	if (tf!=null) return tf;
	        }
	    }
	    
	    return tf;
	}
	
	/**
	 * Get first JcomboBox in a JfileChooser (not used now)
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
				if (cb!=null) return cb;
			}
		}
		
		return cb;
	}
	

}
