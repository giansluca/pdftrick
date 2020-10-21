package org.gmdev.pdftrick;

import java.lang.reflect.InvocationTargetException;
import java.net.*;
import java.util.Locale;

import javax.swing.*;

import org.apache.log4j.*;
import org.gmdev.pdftrick.factory.PdfTrickFactory;
import org.gmdev.pdftrick.utils.*;

import static org.gmdev.pdftrick.utils.SetuptUtils.*;

public class PdfTrick {
    private static final Logger LOGGER = Logger.getLogger(PdfTrick.class);
    private static final String ERROR_TYPE = Consts.ERROR_TYPE;

    public static void main(String[] args) {
        // logger config
        PropertyConfigurator.configure(FileLoader.loadAsStream(Consts.PROPERTY_L4J_FILE));

        // check OS
        if (args == null || args.length == 0)
            throw new IllegalStateException("Os argument is missing");

        String argOs = args[0];
        if (isWindows() && !argOs.equals(WIN_OS))
            throw new IllegalStateException(String.format("in Windows system Os argument should be '%s'", WIN_OS));
        else if (isMac() && !argOs.equals(MAC_OS))
            throw new IllegalStateException(String.format("in Mac system Os argument should be '%s'", MAC_OS));

        // checking Architecture
        boolean checkArch = isJvm64();

        // set some properties in a osx environment before the UI initialization
        if (SetuptUtils.isMac())
            SetuptUtils.setMacPreferences();

        Locale.setDefault(Locale.ENGLISH);
        JComponent.setDefaultLocale(Locale.ENGLISH);

        if (checkArch) {
            try{
                // check one instance only, bind a port
                ServerSocket serverSocket = new ServerSocket(15486);

                // create hidden working folder
                String hiddenHomeFolder = SetuptUtils.createHiddenHomeFolder();

                // extract native c lib
                SetuptUtils.extractNativeLibrary();

                // run
                PdfTrickFactory.getFactory().initialize(hiddenHomeFolder, argOs);
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
