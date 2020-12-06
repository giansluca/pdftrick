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

    public static final String OS = "os";
    public static final String VERSION = "version";

    private static Jargs arguments;
    private static Path homeFolderPath;
    private static Path nativeLibraryPath;

    public static void main(String[] args) {
        configureLogger();
        setLocale();
        checkArchitecture();
        checkSingleInstanceRunning();
        parseOsArguments(args);

        String operatingSystem = checkAndGetSystemOs();
        if (checkAndGetSystemOs().equals(MAC_OS))
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

    private static String checkAndGetSystemOs() {
        String osArgument = arguments.getString(OS);
        String systemOs = getOs();
        if (!systemOs.equals(osArgument))
            throw new IllegalArgumentException(
                    String.format("Os argument should be '%s' or '%s'", WIN_OS, MAC_OS));

        return systemOs;
    }

    private static void parseOsArguments(String[] args) {
        if (args == null)
            throw new IllegalArgumentException("Argument object cannot be null");

        String schema = String.format("%s*, %s*", OS, VERSION);
        try {
            arguments = new Jargs(schema, args);
            if (!arguments.has(OS))
                throw new IllegalArgumentException("Os argument is missing");
            if (!arguments.has(VERSION))
                throw new IllegalStateException("Version argument is missing");
        } catch (JargsException e) {
            throw new IllegalStateException(e);
        }
    }

    private static void checkArchitecture() {
        if (!isJvm64())
            ModalWarningPanel.displayArchWarningAndThrow();
    }

    private static void checkSingleInstanceRunning() {
        var singleInstanceValidator = SingleInstanceValidator.getInstance();
        singleInstanceValidator.checkAlreadyRunning();
    }

    private static void run() {
        PdfTrickStarter.start(arguments, homeFolderPath, nativeLibraryPath);
    }


}
