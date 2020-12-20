package org.gmdev.pdftrick.swingmanager;

import org.gmdev.pdftrick.manager.PdfTrickBag;
import org.gmdev.pdftrick.utils.*;
import javax.swing.*;

import java.awt.*;

import static org.gmdev.pdftrick.utils.Constants.WARNING_ICO;

public class ModalWarningPanel {

    private static final PdfTrickBag BAG = PdfTrickBag.INSTANCE;
    private static final ImageIcon WARNING_ICON = new ImageIcon(FileLoader.loadFileAsUrl(WARNING_ICO));
    private static final String WARNING = "Warning";
    private static final String WAIT = "Wait";

    public static void displayArchWarningAndThrow() {
        String message = "PdfTrick can run only on a 64 bit Jvm";
        SwingInvoker.invokeAndWait(
                () -> displayGenericWarningPanel(null, message, WARNING));

        throw new IllegalStateException(message);
    }

    public static void displayAlreadyRunningWaningAndThrow() {
        String message = "PdfTrick is already running";
        SwingInvoker.invokeAndWait(
                () -> displayGenericWarningPanel(null, message, WARNING));

        throw new IllegalStateException(message);
    }

    public static void displayLoadingPdfThumbnailsWarning() {
        String message = "PdfTrick is loading pages, wait or press cancel to terminate";
        SwingInvoker.invokeLater(
                () -> displayGenericWarningPanel(BAG.getUserInterface(), message, WAIT));
    }

    public static void displayExtractingImagesWarning() {
        String message = "PdfTrick is extracting images, wait!";
        SwingInvoker.invokeLater(
                () -> displayGenericWarningPanel(BAG.getUserInterface(), message, WAIT));
    }

    public static void displayLoadingPageThumbnailImagesWarning() {
        String message = "PdfTrick is loading images, wait!";
        SwingInvoker.invokeLater(
                () -> displayGenericWarningPanel(BAG.getUserInterface(), message, WAIT));
    }

    private static void displayGenericWarningPanel(Component parentComponent,
                                                   String message,
                                                   String title) {
        JOptionPane.showMessageDialog(
                parentComponent,
                message,
                title,
                JOptionPane.WARNING_MESSAGE,
                WARNING_ICON);
    }

}
