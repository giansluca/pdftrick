package org.gmdev.pdftrick.ui.actions;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import org.apache.log4j.Logger;
import org.gmdev.pdftrick.factory.PdfTrickFactory;
import org.gmdev.pdftrick.utils.Consts;
import org.gmdev.pdftrick.utils.FileLoader;
import org.gmdev.pdftrick.utils.PdfTrickPreInitUtils;

public class LicenseAction extends AbstractAction {
	
	private static final PdfTrickFactory factory = PdfTrickFactory.getFactory();
	private static final Logger logger = Logger.getLogger(LicenseAction.class);
	private static final long serialVersionUID = 6003243894996325087L;
	private final ImageIcon license_icon = new ImageIcon(FileLoader.loadAsUrl(Consts.LICENSE_ICO));
	
	public LicenseAction() {
		super.putValue(NAME, "License");
		super.putValue(SMALL_ICON, license_icon);
	}
	
	/**
	 * Called by menu' item Help / View Licence
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		boolean win = PdfTrickPreInitUtils.isWindows();
		JDialog dialog = new JDialog(factory.getUserInterface(), true);
		
		// box
		dialog.setTitle(Consts.LICENCETITLE);
		if (win) {
			dialog.setSize(564, 680);
		} else {
			dialog.setSize(500, 670);
		}
		dialog.setResizable(false);
		dialog.setLocationRelativeTo(null);
		dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		dialog.setLayout(null);
		
		// text area
	    JTextArea licArea = new JTextArea();
	    
	    try {
	    	licArea.read(new InputStreamReader(FileLoader.loadAsStream(Consts.LICENSEFILE)), Consts.LICENCETITLE);
		} catch (IOException ex) {
			logger.error("Exception", ex);
		}
	    
	    // scroll
	    JScrollPane scrollPane_LicArea = new JScrollPane();
	    scrollPane_LicArea.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
	    
	    if (win) {
	    	scrollPane_LicArea.setSize(560, 600);
	    } else {
	    	scrollPane_LicArea.setSize(500, 600);
	    }
	    
	    licArea.setFont(licArea.getFont().deriveFont(12f));	
	    licArea.setLineWrap(true);
	    licArea.setEditable(false);
	    licArea.setBackground(Color.WHITE);
	    scrollPane_LicArea.setViewportView(licArea);
	    
	    // logo
	    ImageIcon imageIcon = new ImageIcon(FileLoader.loadAsUrl(Consts.GPL3_ICO));
	    JLabel logo = new JLabel();
	    logo.setIcon(imageIcon);
	    logo.setBounds(20, 610, imageIcon.getIconWidth(), imageIcon.getIconHeight());
	    
	    // button
	    JButton okButton = new JButton("CLOSE");
	    okButton.addActionListener(new CloseAction(dialog));
	    
	    if (win) {
	    	okButton.setBounds(450, 612, 90, 25);
	    } else {
	    	okButton.setBounds(386, 612, 90, 25);
	    }
	    okButton.setFocusable(false);
	    
	    dialog.getRootPane().setDefaultButton(okButton);
	    dialog.add(scrollPane_LicArea);
	    dialog.add(logo);
	    dialog.add(okButton);
	    dialog.setVisible(true);
	}

	public class CloseAction implements ActionListener {
	
		private final JDialog dialog;
		
		public CloseAction(JDialog dialog) {
			this.dialog = dialog;
		}
		
		/**
		 * Listener for Close button on License box
		 */
		@Override
		public void actionPerformed(ActionEvent arg0) {
			dialog.dispose();
			dialog.setVisible(false);
		}
	}
	
	
}
