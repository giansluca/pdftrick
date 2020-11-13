package org.gmdev.pdftrick.swingmanager;

import org.gmdev.pdftrick.manager.PdfTrickBag;
import org.gmdev.pdftrick.ui.UserInterface;
import org.gmdev.pdftrick.utils.FileLoader;

import javax.swing.*;
import java.awt.*;

import static org.gmdev.pdftrick.utils.Constants.*;

public class ModalInfoPanel {

    private static final PdfTrickBag BAG = PdfTrickBag.INSTANCE;

    public static void displayAboutPanel() {
        UserInterface userInterface = BAG.getUserInterface();
        String os = BAG.getOs();
        ImageIcon pdfTrickIcon = new ImageIcon(FileLoader.loadFileAsUrl(PDFTRICK_ICO));

        String aboutTitle = "PdfTrick Info";
        String aboutMessage = String.format(
                "PdfTrick\nAuthor: Gian Luca Mori\nVersion: 1.3 %s \nLicense: gnu Gpl3", os);

        SwingInvoker.invokeLater(
                () -> displayGenericInfoPanel(userInterface, aboutMessage, aboutTitle, pdfTrickIcon));
    }

    private static void displayGenericInfoPanel(Component parent, String message, String title, ImageIcon icon) {
        JOptionPane.showMessageDialog(
                parent,
                message,
                title,
                JOptionPane.INFORMATION_MESSAGE ,
                icon);
    }


}
