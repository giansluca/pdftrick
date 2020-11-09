package org.gmdev.pdftrick.swingmanager;

import org.gmdev.pdftrick.manager.PdfTrickBag;
import org.gmdev.pdftrick.ui.UserInterface;

import static org.gmdev.pdftrick.swingmanager.WaitPanel.WaitPanelMode.EXTRACTING_IMAGES;
import static org.gmdev.pdftrick.swingmanager.WaitPanel.WaitPanelMode.PAGE_LOADING_THUMBNAILS;

public class WaitPanel {
	
	private static final UserInterface userInterface = PdfTrickBag.INSTANCE.getUserInterface();

	public static void setLoadingThumbnailsWaitPanel() {
		SwingInvoker.invokeLater(
				() -> userInterface.lockScreen(PAGE_LOADING_THUMBNAILS));
	}

	public static void setExtractingImagesWaitPanel() {
		SwingInvoker.invokeLater(
				() -> userInterface.lockScreen(EXTRACTING_IMAGES));
	}

	public static void removeWaitPanel() {
		SwingInvoker.invokeLater(
				userInterface::unlockScreen);
	}

	public enum WaitPanelMode {
		PAGE_LOADING_THUMBNAILS, EXTRACTING_IMAGES
	}
	
	
}

