package org.gmdev.pdftrick;

import java.nio.file.Path;
import java.util.Locale;
import java.util.Properties;
import javax.swing.*;

import io.github.giansluca.jargs.Jargs;
import io.github.giansluca.jargs.exception.JargsException;
import org.apache.log4j.*;
import org.gmdev.pdftrick.manager.PdfTrickStarter;
import org.gmdev.pdftrick.swingmanager.ModalWarningPanel;
import org.gmdev.pdftrick.utils.*;
import org.gmdev.pdftrick.validation.SingleInstanceValidator;

import static org.gmdev.pdftrick.utils.Constants.*;
import static org.gmdev.pdftrick.utils.SetupUtils.*;

public class PdfTrick {

    private static String operatingSystem;
    private static Path homeFolderPath;
    private static Path nativeLibraryPath;

    public static void main(String[] args) {
        configureLogger();
        setLocale();
        checkArchitecture();
        checkSingleInstanceRunning();

        operatingSystem = checkAndGetSystemOs(args);
        if (operatingSystem.equals(MAC_OS))
            setMacPreferences();

        homeFolderPath = setAndGetHomeFolder(operatingSystem);
        nativeLibraryPath = setAndGetNativeLibrary(homeFolderPath, operatingSystem);
        run();
    }

    private static void configureLogger() {
        Properties loggerProperty = PropertyLoader.loadPropertyFile(PROPERTY_L4J_FILE);
        PropertyConfigurator.configure(loggerProperty);
    }

    private static void setLocale() {
        Locale.setDefault(Locale.ENGLISH);
        JComponent.setDefaultLocale(Locale.ENGLISH);
    }

    private static String checkAndGetSystemOs(String[] args) {
        String osArgument = parseOsArguments(args);
        String systemOs = getOs();
        if (!systemOs.equals(osArgument))
            throw new IllegalArgumentException(
                    String.format("Os argument should be '%s' or '%s'", WIN_OS, MAC_OS));

        return systemOs;
    }

    private static String parseOsArguments(String[] args) {
        if (args == null)
            throw new IllegalArgumentException("Argument object cannot be null");

        String os = "os";
        String schema = os + "*";
        Jargs arguments;
        try {
            arguments = new Jargs(schema, args);
            if (!arguments.has(os))
                throw new IllegalArgumentException("Os argument is missing");
        } catch (JargsException e) {
            throw new IllegalStateException(e);
        }

        return arguments.getString(os);
    }

    private static void checkArchitecture() {
        if (!isJvm64())
            ModalWarningPanel.displayArchWarningPanelAndThrow();
    }

    private static void setMacPreferences() {
        System.setProperty("com.apple.mrj.application.apple.menu.about.name", "PdfTrick");
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        System.setProperty("apple.awt.fileDialogForDirectories", "true");
    }

    private static void checkSingleInstanceRunning() {
        var singleInstanceValidator = SingleInstanceValidator.getInstance();
        singleInstanceValidator.checkAlreadyRunning();
    }

    private static void run() {
        PdfTrickStarter.start(operatingSystem, homeFolderPath, nativeLibraryPath);
    }


}
