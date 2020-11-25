package org.gmdev.pdftrick.utils;

public class ThreadUtils {

    public static void pause(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }
}
