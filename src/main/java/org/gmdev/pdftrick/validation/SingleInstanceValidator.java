package org.gmdev.pdftrick.validation;

import org.gmdev.pdftrick.swingmanager.ModalWarningPanel;
import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;

public class SingleInstanceValidator {

    private static final SingleInstanceValidator INSTANCE = new SingleInstanceValidator();
    private static final int SERVER_PORT = 12345;

    private ServerSocket serverSocket;

    private SingleInstanceValidator() {}

    public static SingleInstanceValidator getInstance() {
        return INSTANCE;
    }

    public void checkPdfTrickAlreadyRunning() {
        startFlagServerSocket();
    }

    public void startFlagServerSocket() {
        try {
            serverSocket = new ServerSocket(SERVER_PORT);
        } catch (BindException e) {
            ModalWarningPanel.displayAlreadyRunningAndThrow();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public void stopFlagServerSocket() {
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
    }

}
