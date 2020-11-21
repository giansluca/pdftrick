package org.gmdev.pdftrick.ui.actions;

import java.awt.Container;
import java.awt.event.*;
import java.io.File;
import java.util.Properties;

import javax.swing.*;

import org.gmdev.pdftrick.manager.*;
import org.gmdev.pdftrick.swingmanager.ModalWarningPanel;
import org.gmdev.pdftrick.thread.OpenFileChooser;
import org.gmdev.pdftrick.ui.custom.CustomFileChooser;
import org.gmdev.pdftrick.ui.panels.LeftPanel;
import org.gmdev.pdftrick.utils.*;

import static org.gmdev.pdftrick.utils.Constants.JFC_OPEN_TITLE;
import static org.gmdev.pdftrick.utils.SetupUtils.WIN_OS;

public class OpenAction extends AbstractAction {

    private static final PdfTrickBag BAG = PdfTrickBag.INSTANCE;
    private static final String ACTION_NAME = "Open";

    public OpenAction() {
        ImageIcon open_icon = new ImageIcon(FileLoader.loadFileAsUrl(Constants.OPEN_FILE_ICO));
        super.putValue(NAME, ACTION_NAME);
        super.putValue(SMALL_ICON, open_icon);
        if (BAG.getOs().equals(WIN_OS))
            super.putValue(
            		ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
        else
            super.putValue(
            		ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.META_DOWN_MASK));
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        LeftPanel leftPanel = BAG.getUserInterface().getLeft();
        Properties messagesProps = BAG.getMessagesProps();
        Container contentPanel = BAG.getUserInterface().getContentPane();

        CustomFileChooser fileOpen = new CustomFileChooser();
        fileOpen.setMultiSelectionEnabled(true);
        fileOpen.setDialogTitle(JFC_OPEN_TITLE);

        int ret = fileOpen.showOpenDialog(contentPanel);
        if (ret != JFileChooser.APPROVE_OPTION) return;

		ThreadContainer threadContainer = BAG.getThreadContainer();

		if ((threadContainer.getDragAnDropFileChooserThread() != null &&
				threadContainer.getDragAnDropFileChooserThread().isAlive()) ||
                (threadContainer.getOpenFileChooserThread() != null &&
						threadContainer.getOpenFileChooserThread().isAlive())) {

            leftPanel.resetLeftPanelFileDropBorder();
            Messages.append("WARNING", messagesProps.getProperty("tmsg_01"));
            return;
        }

        if (threadContainer.getShowThumbsThread() != null && threadContainer.getShowThumbsThread().isAlive()) {
        	leftPanel.resetLeftPanelFileDropBorder();
            ModalWarningPanel.displayLoadingPdfThumbnailsWarning();
            return;
        }

        if (threadContainer.getImgExtractionThread() != null && threadContainer.getImgExtractionThread().isAlive()) {
        	leftPanel.resetLeftPanelFileDropBorder();
            ModalWarningPanel.displayLoadingPageThumbnailImagesWarning();
            return;
        }

        if (threadContainer.getImgThumbThread() != null && threadContainer.getImgThumbThread().isAlive()) {
            leftPanel.resetLeftPanelFileDropBorder();
            ModalWarningPanel.displayLoadingPageThumbnailImagesWarning();
            return;
        }

        File[] files = fileOpen.getSelectedFiles();

        OpenFileChooser openFileChooser = new OpenFileChooser(files);
        BAG.getThreadContainer().setOpenFileChooser(openFileChooser);

        Thread openFileChooserThread = new Thread(openFileChooser, "openFileChooserThread");
        BAG.getThreadContainer().setOpenFileChooserThread(openFileChooserThread);
        openFileChooserThread.start();
    }

}
