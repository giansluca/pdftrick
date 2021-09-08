package org.gmdev.pdftrick.ui.actions;

import java.awt.Container;
import java.awt.event.*;
import java.io.File;

import javax.swing.*;

import org.gmdev.pdftrick.checking.FileIn;
import org.gmdev.pdftrick.manager.*;
import org.gmdev.pdftrick.ui.custom.CustomFileChooser;
import org.gmdev.pdftrick.utils.*;

import static org.gmdev.pdftrick.utils.Constants.JFC_OPEN_TITLE;
import static org.gmdev.pdftrick.utils.SetupUtils.WIN_OS;

public class FileChooserAction extends AbstractAction implements FileIn {

    private static final PdfTrickBag bag = PdfTrickBag.INSTANCE;
    private static final String ACTION_NAME = "Open";

    public FileChooserAction() {
        ImageIcon openIcon = new ImageIcon(FileLoader.loadFileAsUrl(Constants.OPEN_FILE_ICO));
        super.putValue(NAME, ACTION_NAME);
        super.putValue(SMALL_ICON, openIcon);
        if (bag.getOs().equals(WIN_OS))
            super.putValue(
                    ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
        else
            super.putValue(
                    ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.META_DOWN_MASK));
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        Container contentPanel = bag.getUserInterface().getContentPane();

        CustomFileChooser fileOpen = new CustomFileChooser();
        fileOpen.setMultiSelectionEnabled(true);
        fileOpen.setDialogTitle(JFC_OPEN_TITLE);

        int returnValue = fileOpen.showOpenDialog(contentPanel);
        if (returnValue != JFileChooser.APPROVE_OPTION) return;

        File[] files = fileOpen.getSelectedFiles();
        start(files);
    }

}