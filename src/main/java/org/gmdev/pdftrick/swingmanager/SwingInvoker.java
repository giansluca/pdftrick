package org.gmdev.pdftrick.swingmanager;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;

public class SwingInvoker {

    public static void invokeAndWait(Runnable task) {
        try {
            if(!SwingUtilities.isEventDispatchThread())
                SwingUtilities.invokeAndWait(task);
            else
                task.run();
        } catch (InterruptedException | InvocationTargetException e) {
            throw new IllegalStateException(e);
        }
    }

    public static void invokeLater(Runnable task) {
        SwingUtilities.invokeLater(task);
    }
}
