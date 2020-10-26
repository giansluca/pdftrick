package org.gmdev.pdftrick;

import java.util.Locale;

import javax.swing.*;

import org.apache.log4j.*;
import org.gmdev.pdftrick.factory.PdfTrickFactory;
import org.gmdev.pdftrick.swingmanager.WarningPanel;
import org.gmdev.pdftrick.utils.*;
import org.gmdev.pdftrick.validation.SingleInstanceValidator;

import static org.gmdev.pdftrick.utils.SetupUtils.*;

public class PdfTrick {

    public static void main(String[] args) {
        // check operating system
        if (args == null || args.length == 0)
            throw new IllegalArgumentException("Os argument is missing");

        String argOs = args[0];
        if (!getOs().equals(argOs))
            throw new IllegalArgumentException(
                    String.format("Os argument should be '%s' or '%s'", WIN_OS, MAC_OS));

        // check architecture
        if (!isJvm64())
            WarningPanel.displayArchWarningAndThrow();

        // check single instance running binding a port
        var singleInstanceValidator = new SingleInstanceValidator();
        singleInstanceValidator.checkPdfTrickAlreadyRunning();

        // logger config
        PropertyConfigurator.configure(FileLoader.loadAsStream(Consts.PROPERTY_L4J_FILE));

        // locale config
        Locale.setDefault(Locale.ENGLISH);
        JComponent.setDefaultLocale(Locale.ENGLISH);

        // set some properties in a osx environment before the UI initialization
        if (argOs.equals(MAC_OS))
            setMacPreferences();

        // create home folder
        String homeFolder = createHomeFolder();

        // extract native lib
        extractNativeLibrary();

        // run
        PdfTrickFactory factory = PdfTrickFactory.getFactory();
        factory.initialize(homeFolder, argOs);
        singleInstanceValidator.stopFlagServerSocket();
    }

}
