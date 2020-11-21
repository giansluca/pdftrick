package org.gmdev.pdftrick.ui.panels;

import java.awt.Desktop;

import javax.swing.*;

import org.gmdev.pdftrick.manager.PdfTrickBag;
import org.gmdev.pdftrick.ui.actions.*;

import static org.gmdev.pdftrick.utils.SetupUtils.MAC_OS;

public class Menu {

	private final static PdfTrickBag BAG = PdfTrickBag.INSTANCE;

	private final JMenuBar menuBar;
	
	public Menu() {
		menuBar = new JMenuBar();

		if (BAG.getOs().equals(MAC_OS)) {
			MacActions macActions = new MacActions();
			Desktop desktop = Desktop.getDesktop();
	        desktop.setAboutHandler(e -> macActions.handleAbout());
	        desktop.setQuitHandler((e, r) -> macActions.handleQuitRequestWith());
		} else {
        	JMenu pdftrick = new JMenu("PdfTrick");

        	JMenuItem aboutMenuItem = new JMenuItem();
        	aboutMenuItem.setAction(new AboutAction());
        	
        	JMenuItem exitMenuItem = new JMenuItem();
        	exitMenuItem.setAction(new ExitAction());
        	
        	pdftrick.add(aboutMenuItem);
        	pdftrick.add(exitMenuItem);
        	menuBar.add(pdftrick);
        }
        
        JMenu file = new JMenu("File");
        JMenu help = new JMenu("Help");
  
		JMenuItem openMenuItem = new JMenuItem();
		openMenuItem.setAction(new OpenAction());
    	file.add(openMenuItem);
    	
    	JMenuItem licence = new JMenuItem();
    	licence.setAction(new LicenseAction());

    	help.add(licence);
    	menuBar.add(file);
    	menuBar.add(help);
	}
	
	public JMenuBar getMenuBar() {
		return menuBar;
	}

}
