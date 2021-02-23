package org.gmdev.pdftrick.ui.actions;

import java.awt.Color;
import java.awt.event.*;
import java.io.*;

import javax.swing.*;

import org.gmdev.pdftrick.manager.PdfTrickBag;
import org.gmdev.pdftrick.utils.*;

import static org.gmdev.pdftrick.utils.Constants.*;
import static org.gmdev.pdftrick.utils.SetupUtils.*;

public class LicenseAction extends AbstractAction {

    private static final PdfTrickBag bag = PdfTrickBag.INSTANCE;
    private static final String CLOSE = "Close";
    private static final String LICENSE = "License";

    public LicenseAction() {
        ImageIcon licenseIcon = new ImageIcon(FileLoader.loadFileAsUrl(Constants.LICENSE_ICO));
        super.putValue(NAME, LICENSE);
        super.putValue(SMALL_ICON, licenseIcon);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        JDialog dialog = buildDialogBox();
        JTextArea licenseArea = buildLicenseArea();

        JScrollPane scrollLicenseArea = buildScrollLicenseArea();
        scrollLicenseArea.setViewportView(licenseArea);

        JLabel logo = buildLogo();

        JButton okButton = buildOkButton();
        okButton.addActionListener(new CloseAction(dialog));

        dialog.getRootPane().setDefaultButton(okButton);
        dialog.add(scrollLicenseArea);
        dialog.add(logo);
        dialog.add(okButton);
        dialog.setVisible(true);
    }

    private JDialog buildDialogBox() {
        JDialog dialog = new JDialog(bag.getUserInterface(), true);
        dialog.setTitle(Constants.LICENSE_TITLE);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(null);
        dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        dialog.setLayout(null);

        if (bag.getOs().equals(WIN_OS))
            dialog.setSize(564, 680);
        else
            dialog.setSize(500, 670);

        return dialog;
    }

    private JTextArea buildLicenseArea() {
        JTextArea licenseArea = new JTextArea();
        try (InputStream in = FileLoader.loadFileAsStream(LICENSE_FILE);
             InputStreamReader inReader = new InputStreamReader(in)) {

            licenseArea.read(inReader, LICENSE_TITLE);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }

        licenseArea.setFont(licenseArea.getFont().deriveFont(12f));
        licenseArea.setLineWrap(true);
        licenseArea.setEditable(false);
        licenseArea.setBackground(Color.WHITE);

        return licenseArea;
    }

    private JScrollPane buildScrollLicenseArea() {
        JScrollPane scrollLicenseArea = new JScrollPane();
        scrollLicenseArea.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        if (bag.getOs().equals(WIN_OS))
            scrollLicenseArea.setSize(560, 600);
        else
            scrollLicenseArea.setSize(500, 600);

        return scrollLicenseArea;
    }

    private JLabel buildLogo() {
        ImageIcon imageIcon = new ImageIcon(FileLoader.loadFileAsUrl(Constants.GPL3_ICO));
        JLabel logo = new JLabel();
        logo.setIcon(imageIcon);
        logo.setBounds(20, 610, imageIcon.getIconWidth(), imageIcon.getIconHeight());

        return logo;
    }

    private JButton buildOkButton() {
        JButton okButton = new JButton(CLOSE);
        okButton.setFocusable(false);

        if (bag.getOs().equals(WIN_OS))
            okButton.setBounds(450, 612, 90, 25);
        else
            okButton.setBounds(386, 612, 90, 25);

        return okButton;
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
