package org.gmdev.pdftrick.ui.panels;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.gmdev.pdftrick.ui.actions.CancelAction;
import org.gmdev.pdftrick.ui.actions.CleanSelectionAction;
import org.gmdev.pdftrick.ui.actions.GetImgAction;
import org.gmdev.pdftrick.utils.Constants;

import net.miginfocom.swing.MigLayout;
import org.gmdev.pdftrick.utils.FileLoader;

public class RightPanel {
	
	private final ImageIcon getImages_icon = new ImageIcon(FileLoader.loadAsUrl(Constants.GET_IMG_ICO));
	private final ImageIcon cancel_icon = new ImageIcon(FileLoader.loadAsUrl(Constants.CANCEL_ICO));
	private final ImageIcon cleanSelection_icon = new ImageIcon(FileLoader.loadAsUrl(Constants.CLEAN_SEL_ICO));
	
	private final JPanel rightPanel;
    private final JPanel rightTopPanel;
    private final JPanel rightBottomPanel;
    private final JButton getImagesButton;
    private final JButton cancelButton;
    private final JButton cleanButton;
    private final JTextField currentPageField;
    private final JTextField numImgSelectedField;
	
    public RightPanel() {
    	rightTopPanel = new JPanel(new MigLayout("insets 3 3 3 0, right, ttb", "", ""));
		rightBottomPanel = new JPanel(new MigLayout("insets 3 3 3 0, right, btt", "", ""));
		
	    getImagesButton = new JButton("Get Img");
	    getImagesButton.setFocusable(false);
	    getImagesButton.setAction(new GetImgAction());
	    getImagesButton.setText("GET IMG");
	    getImagesButton.setIcon(getImages_icon);
	   
	    cancelButton = new JButton("Cancel");
	    cancelButton.setFocusable(false);
	    cancelButton.setAction(new CancelAction());
	    cancelButton.setText("CANCEL");
	    cancelButton.setIcon(cancel_icon);
	    
	    cleanButton = new JButton("Clean");
	    cleanButton.setFocusable(false);
	    cleanButton.setAction(new CleanSelectionAction());
	    cleanButton.setText("CLEAN");
	    cleanButton.setIcon(cleanSelection_icon);
	    
	    currentPageField = new JTextField();
	    currentPageField.setEditable(false);
	    currentPageField.setFocusable(false);
	    
	    numImgSelectedField = new JTextField();
	    numImgSelectedField.setEditable(false);
	    numImgSelectedField.setFocusable(false);
	    
	    rightTopPanel.add(getImagesButton, "h 25:30:35, w 80:80%:150, gaptop 10, wrap");
	    rightTopPanel.add(cancelButton, "h 25:30:35, w 80:80%:150, gaptop 5, wrap");
	    rightTopPanel.add(cleanButton, "h 25:30:35, w 80:80%:150, gaptop 5, wrap");
	    
	    rightBottomPanel.add(currentPageField, "h 15:25:28, w 60:80%, gaptop 10, wrap");
	    rightBottomPanel.add(numImgSelectedField, "h 15:25:28, w 60:80%, wrap");
	   
	    rightPanel = new JPanel(new MigLayout("insets 0 0 0 0, flowy, fill"));
	    rightPanel.add(rightTopPanel, "grow");
	    rightPanel.add(rightBottomPanel, "grow");
	}
    
    public JPanel getRightPanel() {
		return rightPanel;
	}
   
	public JPanel getRightTopPanel() {
		return rightTopPanel;
	}
	
	public JPanel getRightBottomPanel() {
		return rightBottomPanel;
	}
	
	public JButton getGetImagesButton() {
		return getImagesButton;
	}
	
	public JButton getCancelButton() {
		return cancelButton;
	}
	
	public JButton getCleanButton() {
		return cleanButton;
	}
	
	public JTextField getCurrentPageField() {
		return currentPageField;
	}
	
	public JTextField getNumImgSelectedField() {
		return numImgSelectedField;
	}
    
}
