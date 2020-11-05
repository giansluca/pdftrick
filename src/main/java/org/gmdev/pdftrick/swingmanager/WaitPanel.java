package org.gmdev.pdftrick.swingmanager;

import org.gmdev.pdftrick.manager.PdfTrickBag;
import org.gmdev.pdftrick.ui.UserInterface;

import static org.gmdev.pdftrick.swingmanager.WaitPanel.WaitPanelMode.EXTRACTING_IMAGES;
import static org.gmdev.pdftrick.swingmanager.WaitPanel.WaitPanelMode.LOADING_THUMBNAILS;

public class WaitPanel {
	
	private static final UserInterface ui = PdfTrickBag.getBag().getUserInterface();

	public static void setLoadingThumbnailsWaitPanel() {
		SwingInvoker.invokeLater(
				() -> ui.lockScreen(LOADING_THUMBNAILS));
	}

	public static void setExtractingImagesWaitPanel() {
		SwingInvoker.invokeLater(
				() -> ui.lockScreen(EXTRACTING_IMAGES));
	}

	public static void removeWaitPanel() {
		SwingInvoker.invokeLater(
				ui::unlockScreen);
	}

	public enum WaitPanelMode {
		LOADING_THUMBNAILS, EXTRACTING_IMAGES
	}
	
	
}

