package org.gmdev.pdftrick.ui.panels;

import java.awt.Desktop;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import org.gmdev.pdftrick.ui.actions.AboutAction;
import org.gmdev.pdftrick.ui.actions.ExitAction;
import org.gmdev.pdftrick.ui.actions.LicenseAction;
import org.gmdev.pdftrick.ui.actions.MacActions;
import org.gmdev.pdftrick.ui.actions.OpenAction;
import org.gmdev.pdftrick.ui.actions.SendLogAction;
import org.gmdev.pdftrick.utils.SetupUtils;

public class Menu {
	
	private final JMenuBar menubar;
	
	public Menu() {
		if (SetupUtils.isMac()) {
			MacActions macActions = new MacActions();
			Desktop desktop = Desktop.getDesktop();

	        desktop.setAboutHandler(e -> {
	        		macActions.handleAbout();
	        	}
	        );
	        
	        desktop.setQuitHandler((e, r) -> {
	        		macActions.handleQuitRequestWith();
	            }
	        );
		}
		
		menubar = new JMenuBar();
        if (SetupUtils.isWindows()) {
        	JMenu pdftrick = new JMenu("PdfTrick");
        	
        	JMenuItem aboutMenuItem = new JMenuItem();
        	aboutMenuItem.setAction(new AboutAction());
        	
        	JMenuItem exitMenuItem = new JMenuItem();
        	exitMenuItem.setAction(new ExitAction());
        	
        	pdftrick.add(aboutMenuItem);
        	pdftrick.add(exitMenuItem);
        	menubar.add(pdftrick);
        }
        
        JMenu file = new JMenu("File");
        JMenu help = new JMenu("Help");
  
		JMenuItem openMenuItem = new JMenuItem();
		openMenuItem.setAction(new OpenAction());
    	file.add(openMenuItem);
    	
    	JMenuItem licence = new JMenuItem();
    	licence.setAction(new LicenseAction());
    	
    	JMenuItem sendLog = new JMenuItem();
    	sendLog.setAction(new SendLogAction());
    	
    	help.add(licence);
    	help.add(sendLog);
    	menubar.add(file);
    	menubar.add(help);
	}
	
	public JMenuBar getMenubar() {
		return menubar;
	}
	
	
}
