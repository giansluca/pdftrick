package org.gmdev.pdftrick.swingmanager;

import org.apache.log4j.Logger;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;

public class SwingInvoker {

    private static final Logger LOGGER = Logger.getLogger(SwingInvoker.class);

    public static void invokeAndWait(Runnable doRun) {
        try {
            SwingUtilities.invokeAndWait(doRun);
        } catch (InterruptedException | InvocationTargetException e) {
            LOGGER.error(e);
        }
    }
}
