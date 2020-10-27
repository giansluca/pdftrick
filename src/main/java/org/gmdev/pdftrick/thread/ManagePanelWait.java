package org.gmdev.pdftrick.thread;

import org.gmdev.pdftrick.factory.PdfTrickBag;
import org.gmdev.pdftrick.ui.UI2;

public class ManagePanelWait implements Runnable {
	
	private static final PdfTrickBag factory = PdfTrickBag.getPdfTrickBag();
	
	private final String mode;
	private final String key;
	
	public ManagePanelWait(String mode, String key) {
		this.mode=mode;
		this.key=key;
	}
	
	@Override
	public void run() {
		final UI2 ui = factory.getUserInterface();
		if (mode.equalsIgnoreCase("thumb")) {
			if (key.equalsIgnoreCase("thumb_show")) {
				// lock screen
				ui.lockScreen(mode);
			} else if (key.equalsIgnoreCase("thumb_hide")) {
				// unlock screen
				ui.unlockScreen();
			}
		} else if (mode.equalsIgnoreCase("extract")) {
			if (key.equalsIgnoreCase("extract_show")) {
				// lock screen
				ui.lockScreen(mode);
			} else if (key.equalsIgnoreCase("extract_hide")) {
				// unlock screen
				ui.unlockScreen();
			}
		}
	}
	
	
}

