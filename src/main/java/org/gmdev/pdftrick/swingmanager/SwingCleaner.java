package org.gmdev.pdftrick.swingmanager;

import org.gmdev.pdftrick.manager.PdfTrickBag;

import javax.swing.*;

public class SwingCleaner {

    private static final PdfTrickBag BAG = PdfTrickBag.INSTANCE;

    public static void cleanUserInterface() {
        JTextField currentPageField = BAG.getUserInterface().getRight().getCurrentPageField();
        JTextField selectedImagesField = BAG.getUserInterface().getRight().getSelectedImagesField();

        SwingInvoker.invokeAndWait(() -> {
            BAG.getUserInterface().getLeft().clean();
            BAG.getUserInterface().getCenter().clean();
            BAG.getUserInterface().getBottom().clean();
            currentPageField.setText("");
            selectedImagesField.setText("");
        });
    }


}
