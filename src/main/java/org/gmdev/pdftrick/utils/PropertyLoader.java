package org.gmdev.pdftrick.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static org.gmdev.pdftrick.utils.Constants.MESSAGES_PROPERTY_FILE;

public class PropertyLoader {

    public static Properties loadMessagesPropertyFile() {
        return loadPropertyFile(MESSAGES_PROPERTY_FILE);
    }

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
