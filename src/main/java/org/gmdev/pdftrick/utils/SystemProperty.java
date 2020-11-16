package org.gmdev.pdftrick.utils;

public class SystemProperty {

    private SystemProperty() {
        throw new AssertionError("SystemProperty class should never be instantiated");
    }

    public static String getSystemProperty(String propertyName) {
        return System.getProperty(propertyName);
    }

}
