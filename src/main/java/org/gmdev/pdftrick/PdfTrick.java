package org.gmdev.pdftrick;

import java.util.Locale;

import javax.swing.*;

import org.apache.log4j.*;
import org.gmdev.pdftrick.factory.PdfTrickBag;
import org.gmdev.pdftrick.factory.PdfTrickFactory;
import org.gmdev.pdftrick.swingmanager.WarningPanel;
import org.gmdev.pdftrick.utils.*;
import org.gmdev.pdftrick.utils.exception.DefaultHandler;

import static org.gmdev.pdftrick.utils.SetupUtils.*;

public class PdfTrick {

    public static void main(String[] args) {
        configureLogger();
        setLocale();
        setDefaultUncaughtExceptionHandler();

        // check operating system
        if (args == null || args.length == 0)
            throw new IllegalArgumentException("Os argument is missing");

        String argOs = args[0];
        String operatingSystem = getOs();
        if (!operatingSystem.equals(argOs))
            throw new IllegalArgumentException(
                    String.format("Os argument should be '%s' or '%s'", WIN_OS, MAC_OS));

        // set some properties in a osx environment
        if (operatingSystem.equals(MAC_OS))
            setMacPreferences();

        // check architecture
        if (!isJvm64())
            WarningPanel.displayArchWarningAndThrow();

        // check single instance running binding a port
        var singleInstanceValidator = PdfTrickFactory.getSingleInstanceValidator();
        singleInstanceValidator.checkPdfTrickAlreadyRunning();

        // create home folder
        String homeFolder = getHomeFolder();

        // extract native lib
        extractNativeLibrary();

        // run
        var pdfTrickBag = PdfTrickBag.getPdfTrickBag();
        pdfTrickBag.initialize(homeFolder, operatingSystem);
    }

    private static void configureLogger() {
        PropertyConfigurator.configure(FileLoader.loadAsStream(Constants.PROPERTY_L4J_FILE));
    }

    private static void setLocale() {
        Locale.setDefault(Locale.ENGLISH);
        JComponent.setDefaultLocale(Locale.ENGLISH);
    }

    public static void setDefaultUncaughtExceptionHandler() {
        Thread.setDefaultUncaughtExceptionHandler(new DefaultHandler());
    }

}
