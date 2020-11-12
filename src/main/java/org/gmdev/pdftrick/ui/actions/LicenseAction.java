package org.gmdev.pdftrick.ui.actions;

import java.awt.Color;
import java.awt.event.*;
import java.io.*;

import javax.swing.*;

import org.gmdev.pdftrick.manager.PdfTrickBag;
import org.gmdev.pdftrick.utils.*;

import static org.gmdev.pdftrick.utils.Constants.*;

public class LicenseAction extends AbstractAction {

    private static final PdfTrickBag BAG = PdfTrickBag.INSTANCE;
    private static final long serialVersionUID = 6003243894996325087L;

    public LicenseAction() {
        ImageIcon licenseIcon = new ImageIcon(FileLoader.loadFileAsUrl(Constants.LICENSE_ICO));
        super.putValue(NAME, "License");
        super.putValue(SMALL_ICON, licenseIcon);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        boolean win = SetupUtils.isWindows();
        JDialog dialog = new JDialog(BAG.getUserInterface(), true);

        // box
        dialog.setTitle(Constants.LICENSE_TITLE);
        if (win)
            dialog.setSize(564, 680);
        else
            dialog.setSize(500, 670);

        dialog.setResizable(false);
        dialog.setLocationRelativeTo(null);
        dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        dialog.setLayout(null);

        // text area
        JTextArea licenseArea = new JTextArea();
        try (InputStream in = FileLoader.loadFileAsStream(LICENSE_FILE);
             InputStreamReader inReader = new InputStreamReader(in)) {
            licenseArea.read(inReader, LICENSE_TITLE);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }

        // scroll
        JScrollPane scrollPaneLicenseArea = new JScrollPane();
        scrollPaneLicenseArea.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        if (win)
            scrollPaneLicenseArea.setSize(560, 600);
        else
            scrollPaneLicenseArea.setSize(500, 600);

        licenseArea.setFont(licenseArea.getFont().deriveFont(12f));
        licenseArea.setLineWrap(true);
        licenseArea.setEditable(false);
        licenseArea.setBackground(Color.WHITE);
        scrollPaneLicenseArea.setViewportView(licenseArea);

        // logo
        ImageIcon imageIcon = new ImageIcon(FileLoader.loadFileAsUrl(Constants.GPL3_ICO));
        JLabel logo = new JLabel();
        logo.setIcon(imageIcon);
        logo.setBounds(20, 610, imageIcon.getIconWidth(), imageIcon.getIconHeight());

        // button
        JButton okButton = new JButton("CLOSE");
        okButton.addActionListener(new CloseAction(dialog));

        if (win)
            okButton.setBounds(450, 612, 90, 25);
        else
            okButton.setBounds(386, 612, 90, 25);

        okButton.setFocusable(false);

        dialog.getRootPane().setDefaultButton(okButton);
        dialog.add(scrollPaneLicenseArea);
        dialog.add(logo);
        dialog.add(okButton);
        dialog.setVisible(true);
    }

    public static class CloseAction implements ActionListener {

        private final JDialog dialog;

        public CloseAction(JDialog dialog) {
            this.dialog = dialog;
        }

        @Override
        public void actionPerformed(ActionEvent arg0) {
            dialog.dispose();
            dialog.setVisible(false);
        }
    }


}
