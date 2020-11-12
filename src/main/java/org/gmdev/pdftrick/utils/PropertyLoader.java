package org.gmdev.pdftrick.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyLoader {

    public static Properties loadPropertyFile(String propertyFile) {
        try (InputStream in = FileLoader.loadFileAsStream(propertyFile)) {
            Properties props = new Properties();
            props.load(in);
            return props;
        } catch (IOException e) {
           throw new IllegalStateException(e);
        }
    }

}
