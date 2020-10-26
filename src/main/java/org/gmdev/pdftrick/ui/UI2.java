package org.gmdev.pdftrick.ui;

import java.awt.Dimension;
import java.awt.MediaTracker;
import java.awt.Taskbar;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

import org.apache.log4j.Logger;
import org.gmdev.pdftrick.ui.actions.WindowsActions;
import org.gmdev.pdftrick.ui.custom.GlassPane;
import org.gmdev.pdftrick.ui.panels.BottomPanel;
import org.gmdev.pdftrick.ui.panels.CenterPanel;
import org.gmdev.pdftrick.ui.panels.LeftPanel;
import org.gmdev.pdftrick.ui.panels.Menu;
import org.gmdev.pdftrick.ui.panels.RightPanel;
import org.gmdev.pdftrick.utils.Consts;
import org.gmdev.pdftrick.utils.FileLoader;
import org.gmdev.pdftrick.utils.SetupUtils;
import net.miginfocom.swing.MigLayout;

public class UI2 extends JFrame {
	
	private static final Logger logger = Logger.getLogger(UI2.class);
	private static final long serialVersionUID = 3445384439912025476L;
	private JPanel contentPane;
	private final LeftPanel left;
    private final CenterPanel center;
    private final RightPanel right;
    private final BottomPanel bottom;
    private final GlassPane glassPanel;
    private final Menu menu;
    
	public UI2() {
		super();
		try {
			// Set native system look and feel
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	    } catch(Exception e) {
	    	logger.error("Exception", e);
	    }
		
		// Set icon image in windows and osx system
	    if (SetupUtils.isWindows()) {
	    	int loadingDone = MediaTracker.ABORTED | MediaTracker.ERRORED | MediaTracker.COMPLETE;
	    	ImageIcon imageIcon = new ImageIcon(FileLoader.loadAsUrl(Consts.MAIN_ICO));
	    	
	    	while ((imageIcon.getImageLoadStatus() & loadingDone) == 0) {
	    		// look a bit ...
	    	}
	    	
	    	if (imageIcon.getImageLoadStatus() == MediaTracker.COMPLETE) {
	    		super.setIconImage(imageIcon.getImage());
	    	}	
	    } else if (SetupUtils.isMac()) {
	    	int loadingDone = MediaTracker.ABORTED | MediaTracker.ERRORED | MediaTracker.COMPLETE;
	    	ImageIcon imageIcon = new ImageIcon(FileLoader.loadAsUrl(Consts.MAIN_ICO));
	    	
	    	while ((imageIcon.getImageLoadStatus() & loadingDone) == 0) {
	    		// look a bit ...
	    	}
	    	
	    	if (imageIcon.getImageLoadStatus() == MediaTracker.COMPLETE) {
	    		Taskbar.getTaskbar().setIconImage(imageIcon.getImage());
	    	}
	    }
		
	    // init graphic components
		left = new LeftPanel();
		center = new CenterPanel();
		right = new RightPanel();
        bottom = new BottomPanel();
        glassPanel = new GlassPane();
        menu = new Menu();
        
        setTitle(Consts.APP_NAME);
        addWindowListener(new WindowsActions());
        setJMenuBar(menu.getMenubar());
        getRootPane().setGlassPane(glassPanel);
        setContentPane(contentPanelSetUp());
        pack();
	}
	
	public JPanel contentPanelSetUp() {
		contentPane = new JPanel();
        contentPane.setLayout(new MigLayout());
        contentPane.setPreferredSize(new Dimension(1250, 800));
        contentPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        contentPane.add(left.getLeftScrollPanel(), "h 100%, w 20%, span 1 2");
        contentPane.add(center.getCenterScrollPanel(), "h 77%, w 68%");
        contentPane.add(right.getRightPanel(), "h 77%, w 12%, wrap");
        contentPane.add(bottom.getBottomPanel(), "h 23%, w 82%, span 2 1");
        return contentPane;
	}
	
	/**
	 * Set visible the glass panel - for freeze screen (little time) in some situation
	 */
	public void lockScreen(String mode) {
		if (mode.equalsIgnoreCase("thumb")) {
			right.getRightBottomPanel().add(glassPanel.getThumbSpinner(), "gaptop 10, center, wrap");
		} else {
			right.getRightBottomPanel().add(glassPanel.getExtractSpinner(), "gaptop 10, center, wrap");
		}
		
		right.getRightBottomPanel().revalidate();
		right.getRightBottomPanel().repaint();
		glassPanel.setVisible(true);
	}
	
	/**
	 * Remove the glass panel 
	 */
	public void unlockScreen() {
		// the spinner is the last component added in that panel, because it is dynamically added whit the glass pane
		int spinnerIndex = right.getRightBottomPanel().getComponents().length -1;
		right.getRightBottomPanel().remove(spinnerIndex);
		right.getRightBottomPanel().revalidate();
		right.getRightBottomPanel().repaint();
		glassPanel.setVisible(false);
	}
	
	public LeftPanel getLeft() {
		return left;
	}
	
	public CenterPanel getCenter() {
		return center;
	}

	public RightPanel getRight() {
		return right;
	}

	public BottomPanel getBottom() {
		return bottom;
	}
	
	public GlassPane getGlassPanel() {
		return glassPanel;
	}
	
	public Menu getMenu() {
		return menu;
	}
	

}
