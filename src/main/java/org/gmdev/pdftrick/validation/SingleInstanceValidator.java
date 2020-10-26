package org.gmdev.pdftrick.validation;

import org.apache.log4j.Logger;
import org.gmdev.pdftrick.swingmanager.WarningPanel;

import java.io.IOException;
import java.net.ServerSocket;

public class SingleInstanceValidator {

    private static final Logger logger = Logger.getLogger(SingleInstanceValidator.class);
    private static final int SERVER_PORT = 12345;

    private ServerSocket serverSocket;

    public void checkPdfTrickAlreadyRunning() {
        startFlagServerSocket();
    }

    public void startFlagServerSocket() {
        try {
            serverSocket = new ServerSocket(SERVER_PORT);
        } catch (IOException e) {
            WarningPanel.displayAlreadyRunningAndThrow();
        }
    }

    public void stopFlagServerSocket() {
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                logger.error(e);
            }
        }
    }

}