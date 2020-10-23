package org.gmdev.pdftrick;

import java.net.*;
import java.util.Locale;

import javax.swing.*;

import org.apache.log4j.*;
import org.gmdev.pdftrick.factory.PdfTrickFactory;
import org.gmdev.pdftrick.swingmanager.WarningPanel;
import org.gmdev.pdftrick.utils.*;

import static org.gmdev.pdftrick.utils.SetupUtils.*;

public class PdfTrick {
    private static final Logger LOGGER = Logger.getLogger(PdfTrick.class);
    private static String argOs;

    public static void main(String[] args) throws Exception {
        // logger config
        PropertyConfigurator.configure(FileLoader.loadAsStream(Consts.PROPERTY_L4J_FILE));

        // locale config
        Locale.setDefault(Locale.ENGLISH);
        JComponent.setDefaultLocale(Locale.ENGLISH);

        // check OS
        if (args == null || args.length == 0)
            throw new IllegalArgumentException("Os argument is missing");

        argOs = args[0];
        if (!getOs().equals(argOs))
            throw new IllegalArgumentException(
                    String.format("Os argument should be '%s' or '%s'", WIN_OS, MAC_OS));

        // set some properties in a osx environment before the UI initialization
        if (argOs.equals(MAC_OS))
            setMacPreferences();

        // checking Architecture
        if (!isJvm64())
            WarningPanel.displayArchWarningAndThrow();

        try {
            // check one instance only binding a port
            new ServerSocket(15486);

            // create hidden working folder
            String homeFolder = createHomeFolder();

            // extract native lib
            extractNativeLibrary();

            // run
            PdfTrickFactory.getFactory().initialize(homeFolder, argOs);
        } catch (BindException e) {
            ImageIcon warningIcon = new ImageIcon(FileLoader.loadAsUrl(Consts.WARNING_ICO));
            SwingUtilities.invokeAndWait(() ->
                    JOptionPane.showMessageDialog(
                            null,
                            Consts.ERROR_RUNNING,
                            "Warning",
                            JOptionPane.WARNING_MESSAGE,
                            warningIcon));

            throw new IllegalStateException(Consts.ERROR_RUNNING);
        }

    }

}
