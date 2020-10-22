package org.gmdev.pdftrick.utils.Exception;

import org.apache.log4j.Logger;
import org.gmdev.pdftrick.PdfTrick;
import org.gmdev.pdftrick.utils.Consts;
import org.gmdev.pdftrick.utils.FileLoader;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;

public class DisplayPanel {

    private static final Logger LOGGER = Logger.getLogger(DisplayPanel.class);

    public static void displayWrongArchWarning() {
        Runnable displayTask = () -> {
            ImageIcon warningIcon = new ImageIcon(FileLoader.loadAsUrl(Consts.WARNING_ICO));
            JOptionPane.showMessageDialog(
                    null,
                    Consts.ERROR_64,
                    Consts.ERROR,
                    JOptionPane.WARNING_MESSAGE,
                    warningIcon);
        };
        display(displayTask);
    }

    private static void display(Runnable doRun) {
        try {
            SwingUtilities.invokeAndWait(doRun);
        } catch (InterruptedException | InvocationTargetException e) {
            LOGGER.error(e);
        }
    }
}
