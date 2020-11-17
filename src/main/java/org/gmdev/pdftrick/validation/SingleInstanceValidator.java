package org.gmdev.pdftrick.validation;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import org.gmdev.pdftrick.swingmanager.ModalWarningPanel;
import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;

public class SingleInstanceValidator {

    private static final int SERVER_PORT = 12345;

    private ServerSocket serverSocket;

    @CanIgnoreReturnValue
    public static SingleInstanceValidator getInstance() {
        return new SingleInstanceValidator();
    }

    public void checkAlreadyRunning() {
        startFlagServerSocket();
    }

    public void startFlagServerSocket() {
        try {
            serverSocket = new ServerSocket(SERVER_PORT);
        } catch (BindException e) {
            ModalWarningPanel.displayAlreadyRunningWaningAndThrow();
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
