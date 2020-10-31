package org.gmdev.pdftrick.swingmanager;

import org.gmdev.pdftrick.utils.*;
import javax.swing.*;

public class WarningPanel {

    private static final ImageIcon WARNING_ICON = new ImageIcon(FileLoader.loadAsUrl(Constants.WARNING_ICO));
    private static final String WARNING_TITLE = "Warning";

    public static void displayArchWarningAndThrow() {
        String message = "PdfTrick can run only on a 64 bit Jvm";

        Runnable displayTask = () -> JOptionPane.showMessageDialog(
                null,
                message,
                WARNING_TITLE,
                JOptionPane.WARNING_MESSAGE,
                WARNING_ICON);

        SwingInvoker.invokeAndWait(displayTask);
        throw new IllegalStateException(message);
    }

    public static void displayAlreadyRunningAndThrow() {
        String message = "PdfTrick is already running";

        Runnable displayTask = () -> JOptionPane.showMessageDialog(
                null,
                message,
                WARNING_TITLE,
                JOptionPane.WARNING_MESSAGE,
                WARNING_ICON);

        SwingInvoker.invokeAndWait(displayTask);
        throw new IllegalStateException(message);
    }

}
