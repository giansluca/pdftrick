package org.gmdev.pdftrick.swingmanager;

import org.gmdev.pdftrick.manager.PdfTrickBag;
import org.gmdev.pdftrick.utils.FileLoader;

import javax.swing.*;
import java.awt.*;

import static org.gmdev.pdftrick.utils.Constants.*;

public class ModalInfoPanel {

    private static final PdfTrickBag BAG = PdfTrickBag.INSTANCE;

    public static void displayAboutPanel() {
        String os = BAG.getOs();
        ImageIcon pdfTrickIcon = new ImageIcon(FileLoader.loadFileAsUrl(PDFTRICK_ICO));

        String aboutTitle = "PdfTrick Info";
        String aboutMessage = String.format(
                "PdfTrick\nAuthor: Gian Luca Mori\nVersion: %s %s \nLicense: gnu Gpl3", VERSION, os);

        SwingInvoker.invokeLater(
                () -> displayGenericInfoPanel(BAG.getUserInterface(), aboutMessage, aboutTitle, pdfTrickIcon));
    }

    private static void displayGenericInfoPanel(Component parentComponent,
                                                String message,
                                                String title,
                                                ImageIcon icon) {
        JOptionPane.showMessageDialog(
                parentComponent,
                message,
                title,
                JOptionPane.INFORMATION_MESSAGE ,
                icon);
    }


}
