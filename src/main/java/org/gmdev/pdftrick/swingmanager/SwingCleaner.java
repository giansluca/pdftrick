package org.gmdev.pdftrick.swingmanager;

import org.gmdev.pdftrick.manager.PdfTrickBag;

import javax.swing.*;

public class SwingCleaner {

    private static final PdfTrickBag bag = PdfTrickBag.INSTANCE;

    public static void cleanUserInterface() {
        JTextField currentPageField = bag.getUserInterface().getRight().getCurrentPageField();
        JTextField selectedImagesField = bag.getUserInterface().getRight().getSelectedImagesField();

        SwingInvoker.invokeAndWait(() -> {
            bag.getUserInterface().getLeft().clean();
            bag.getUserInterface().getCenter().clean();
            bag.getUserInterface().getBottom().clean();
            currentPageField.setText("");
            selectedImagesField.setText("");
        });
    }

    public static void cleanPanels() {
        SwingInvoker.invokeLater(() -> {
            bag.getUserInterface().getLeft().clean();
            bag.getUserInterface().getCenter().clean();
            bag.getUserInterface().getBottom().clean();
        });
    }


}
