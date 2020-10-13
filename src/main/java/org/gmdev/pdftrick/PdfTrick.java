package org.gmdev.pdftrick;

import java.lang.reflect.InvocationTargetException;
import java.net.BindException;
import java.net.ServerSocket;
import java.util.Locale;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.gmdev.pdftrick.factory.PdfTrickFactory;
import org.gmdev.pdftrick.utils.Consts;
import org.gmdev.pdftrick.utils.FileLoader;
import org.gmdev.pdftrick.utils.PdfTrickPreInitUtils;
import org.gmdev.pdftrick.utils.Exception.SwingExceptionHandler;

public class PdfTrick {

    private static final Logger logger = Logger.getLogger(PdfTrick.class);

    public static ServerSocket serverSocket;
    private static final String errorType = Consts.ERRORTYPE;
    private static final String error_01 = Consts.ERROR_01;
    private static String error_02 = Consts.ERROR_02;
    private static String pdfTrickArch = "";
    private static String pdfTrickOs = "";
    private static String hiddenHomeFolder = "";

    public static void main(String[] args) {
        // get logger configuration
        PropertyConfigurator
                .configure(FileLoader.loadAsStream(Consts.PROPERTYL4JFILE));

        // set the default UncaughtExceptionHandler
        Thread.setDefaultUncaughtExceptionHandler(new SwingExceptionHandler());
        System.setProperty("sun.awt.exception.handler", SwingExceptionHandler.class.getName());

        // check OS
        pdfTrickOs=args[0];
        if (pdfTrickOs.equalsIgnoreCase("win") && !PdfTrickPreInitUtils.isWindows()) {
            System.exit(0);
        } else if (pdfTrickOs.equalsIgnoreCase("mac") && !PdfTrickPreInitUtils.isMac()) {
            System.exit(0);
        }

        // checking Architecture
        pdfTrickArch=args[1];
        boolean checkArch = false;
        if (pdfTrickArch.equalsIgnoreCase("64")) {
            if (PdfTrickPreInitUtils.isJvm64()) {
                checkArch = true;
            } else {
                error_02=Consts.ERROR_64;
            }
        }

        // set some properties in a osx environment before the UI initialisation
        if (PdfTrickPreInitUtils.isMac()) {
            PdfTrickPreInitUtils.setMacPreferencies();
        }

        Locale.setDefault(Locale.ENGLISH);
        JComponent.setDefaultLocale(Locale.ENGLISH);

        if (checkArch) {
            try{
                // check one instance only, bind a port
                serverSocket = new ServerSocket(15486);

                // create hidden working folder
                hiddenHomeFolder = PdfTrickPreInitUtils.createHiddenHomeFolder();

                // extract native c lib
                PdfTrickPreInitUtils.extractNativeLibrary();

                // run
                PdfTrickFactory.getFactory().initialize(hiddenHomeFolder, pdfTrickArch, pdfTrickOs);
            } catch (BindException e1) {
                final ImageIcon warningIcon = new ImageIcon(FileLoader.loadAsUrl(Consts.WARNING_ICO));
                try {
                    SwingUtilities.invokeAndWait(new Runnable() {
                        @Override
                        public void run() {
                            JOptionPane.showMessageDialog(
                                    null, error_01, errorType, JOptionPane.WARNING_MESSAGE, warningIcon);
                        }
                    });
                    System.exit(0);
                } catch (InterruptedException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                SwingUtilities.invokeAndWait(new Runnable() {
                    @Override
                    public void run() {
                        ImageIcon warningIcon = new ImageIcon(FileLoader.loadAsUrl(Consts.WARNING_ICO));
                        JOptionPane.showMessageDialog(
                                null, error_02, errorType, JOptionPane.WARNING_MESSAGE, warningIcon);
                    }
                });
            } catch (InterruptedException | InvocationTargetException e) {
                logger.error("Exception", e);
            }

            System.exit(0);
        }
    }

}
