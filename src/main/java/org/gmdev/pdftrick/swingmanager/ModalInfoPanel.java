package org.gmdev.pdftrick.swingmanager;

import javax.swing.*;
import java.awt.*;

public class ModalInfoPanel {

    public static void displayAboutPanel(Component parent, String message, String title, ImageIcon icon) {



        SwingInvoker.invokeLater(
                () -> displayInfoPanel(parent, message, title, icon));
    }

    public static void displayInfoPanel(Component parent, String message, String title, ImageIcon icon) {
        JOptionPane.showMessageDialog(
                parent,
                message,
                title,
                JOptionPane.INFORMATION_MESSAGE ,
                icon);
    }


}
