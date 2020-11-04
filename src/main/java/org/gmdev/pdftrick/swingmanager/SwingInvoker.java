package org.gmdev.pdftrick.swingmanager;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;

public class SwingInvoker {

    public static void invokeAndWait(Runnable doRun) {
        try {
            SwingUtilities.invokeAndWait(doRun);
        } catch (InterruptedException | InvocationTargetException e) {
            throw new IllegalStateException(e);
        }
    }

    public static void invokeLater(Runnable doRun) {
        SwingUtilities.invokeLater(doRun);
    }
}
