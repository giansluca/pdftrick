package org.gmdev.pdftrick.swingmanager;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;

public class SwingInvoker {

    public static void invokeAndWait(Runnable doRun) {
        try {
            if(!SwingUtilities.isEventDispatchThread())
                SwingUtilities.invokeAndWait(doRun);
            else
                doRun.run();
        } catch (InterruptedException | InvocationTargetException e) {
            throw new IllegalStateException(e);
        }
    }

    public static void invokeLater(Runnable doRun) {
        SwingUtilities.invokeLater(doRun);
    }
}
