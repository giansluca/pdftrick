package org.gmdev.pdftrick;

import java.net.*;
import java.util.Locale;

import javax.swing.*;

import org.apache.log4j.*;
import org.gmdev.pdftrick.factory.PdfTrickFactory;
import org.gmdev.pdftrick.utils.*;

import static org.gmdev.pdftrick.utils.SetupUtils.*;

public class PdfTrick {
    private static final Logger LOGGER = Logger.getLogger(PdfTrick.class);
    public static String os;

    public static void main(String[] args) throws Exception {
        // logger config
        PropertyConfigurator.configure(FileLoader.loadAsStream(Consts.PROPERTY_L4J_FILE));

        // check OS
        if (args == null || args.length == 0)
            throw new IllegalArgumentException("Os argument is missing");

        os = args[0];
        if (!getOs().equals(os))
            throw new IllegalArgumentException(
                    String.format("Os argument should be '%s' or '%s'", WIN_OS, MAC_OS));

        // set some properties in a osx environment before the UI initialization
        if (os.equals(MAC_OS))
            setMacPreferences();

        // locale config
        Locale.setDefault(Locale.ENGLISH);
        JComponent.setDefaultLocale(Locale.ENGLISH);

        // checking Architecture
        if (!isJvm64()) {
            SwingUtilities.invokeAndWait(() -> {
                ImageIcon warningIcon = new ImageIcon(FileLoader.loadAsUrl(Consts.WARNING_ICO));
                JOptionPane.showMessageDialog(
                        null,
                        Consts.ERROR_64,
                        Consts.ERROR,
                        JOptionPane.WARNING_MESSAGE,
                        warningIcon);
            });

            throw new IllegalStateException("Wrong architecture");
        }

        try {
            // check one instance only binding a port
            new ServerSocket(15486);

            // create hidden working folder
            String homeFolder = createHomeFolder();

            // extract native lib
            extractNativeLibrary();

            // run
            PdfTrickFactory.getFactory().initialize(homeFolder, os);
        } catch (BindException e) {
            ImageIcon warningIcon = new ImageIcon(FileLoader.loadAsUrl(Consts.WARNING_ICO));
            SwingUtilities.invokeAndWait(() ->
                    JOptionPane.showMessageDialog(
                            null,
                            Consts.ERROR_RUNNING,
                            Consts.ERROR,
                            JOptionPane.WARNING_MESSAGE,
                            warningIcon));

            throw new IllegalStateException(Consts.ERROR_RUNNING);
        }

    }

}
