package org.gmdev.pdftrick.swingmanager;

import org.apache.log4j.Logger;
import org.gmdev.pdftrick.utils.Consts;
import org.gmdev.pdftrick.utils.FileLoader;

import javax.swing.*;

public class WarningPanel {

    private static final Logger LOGGER = Logger.getLogger(WarningPanel.class);

    public static void displayArchWarningAndThrow() {
        String title = "Warning";
        String message = "PdfTrick can run only on a 64 bit machine";

        Runnable displayTask = () -> {
            ImageIcon warningIcon = new ImageIcon(FileLoader.loadAsUrl(Consts.WARNING_ICO));
            JOptionPane.showMessageDialog(
                    null,
                    message,
                    title,
                    JOptionPane.WARNING_MESSAGE,
                    warningIcon);
        };

        SwingInvoker.invokeAndWait(displayTask);
        throw new IllegalStateException(message);
    }

}
