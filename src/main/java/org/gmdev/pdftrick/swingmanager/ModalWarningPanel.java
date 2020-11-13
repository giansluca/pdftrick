package org.gmdev.pdftrick.swingmanager;

import org.gmdev.pdftrick.utils.*;
import javax.swing.*;

import static org.gmdev.pdftrick.utils.Constants.WARNING_ICO;

public class ModalWarningPanel {

    private static final ImageIcon WARNING_ICON = new ImageIcon(FileLoader.loadFileAsUrl(WARNING_ICO));
    private static final String WARNING_TITLE = "Warning";

    public static void displayArchWarningPanelAndThrow() {
        String message = "PdfTrick can run only on a 64 bit Jvm";

        SwingInvoker.invokeAndWait(() -> displayGenericWarningPanel(message));
        throw new IllegalStateException(message);
    }

    public static void displayAlreadyRunningPanelAndThrow() {
        String message = "PdfTrick is already running";

        SwingInvoker.invokeAndWait(() -> displayGenericWarningPanel(message));
        throw new IllegalStateException(message);
    }

    private static void displayGenericWarningPanel(String message) {
        JOptionPane.showMessageDialog(
                null,
                message,
                WARNING_TITLE,
                JOptionPane.WARNING_MESSAGE,
                WARNING_ICON);
    }

}
