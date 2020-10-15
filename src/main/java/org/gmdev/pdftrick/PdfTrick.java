package org.gmdev.pdftrick;

import java.lang.reflect.InvocationTargetException;
import java.net.*;
import java.util.Locale;

import javax.swing.*;

import org.apache.log4j.*;
import org.gmdev.pdftrick.factory.PdfTrickFactory;
import org.gmdev.pdftrick.utils.*;
import org.gmdev.pdftrick.utils.Exception.SwingExceptionHandler;

public class PdfTrick {
    private static final Logger LOGGER = Logger.getLogger(PdfTrick.class);
    private static final String ERROR_TYPE = Consts.ERROR_TYPE;

    public static void main(String[] args) {
        PropertyConfigurator.configure(FileLoader.loadAsStream(Consts.PROPERTY_L4J_FILE));

        // set the default UncaughtExceptionHandler
        Thread.setDefaultUncaughtExceptionHandler(new SwingExceptionHandler());
        System.setProperty("sun.awt.exception.handler", SwingExceptionHandler.class.getName());

        // check OS
        String pdfTrickOs = args[0];
        if (pdfTrickOs.equalsIgnoreCase("win") && !PdfTrickPreInitUtils.isWindows())
            System.exit(0);
        else if (pdfTrickOs.equalsIgnoreCase("mac") && !PdfTrickPreInitUtils.isMac())
            System.exit(0);

        // checking Architecture
        boolean checkArch = PdfTrickPreInitUtils.isJvm64();

        // set some properties in a osx environment before the UI initialisation
        if (PdfTrickPreInitUtils.isMac())
            PdfTrickPreInitUtils.setMacPreferencies();

        Locale.setDefault(Locale.ENGLISH);
        JComponent.setDefaultLocale(Locale.ENGLISH);

        if (checkArch) {
            try{
                // check one instance only, bind a port
                ServerSocket serverSocket = new ServerSocket(15486);

                // create hidden working folder
                String hiddenHomeFolder = PdfTrickPreInitUtils.createHiddenHomeFolder();

                // extract native c lib
                PdfTrickPreInitUtils.extractNativeLibrary();

                // run
                PdfTrickFactory.getFactory().initialize(hiddenHomeFolder, pdfTrickOs);
            } catch (BindException e) {
                final ImageIcon warningIcon = new ImageIcon(FileLoader.loadAsUrl(Consts.WARNING_ICO));
                try {
                    SwingUtilities.invokeAndWait(new Runnable() {
                        @Override
                        public void run() {
                            JOptionPane.showMessageDialog(
                                    null, Consts.ERROR_RUNNING, ERROR_TYPE, JOptionPane.WARNING_MESSAGE, warningIcon);
                        }
                    });
                    System.exit(0);
                } catch (InterruptedException | InvocationTargetException e1) {
                    e1.printStackTrace();
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
                                null, Consts.ERROR_64, ERROR_TYPE, JOptionPane.WARNING_MESSAGE, warningIcon);
                    }
                });
            } catch (InterruptedException | InvocationTargetException e) {
                LOGGER.error("Exception", e);
            }

            System.exit(0);
        }
    }

}
