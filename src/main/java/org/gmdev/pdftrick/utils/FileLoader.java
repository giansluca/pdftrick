package org.gmdev.pdftrick.utils;

import java.io.InputStream;
import java.net.URL;

public class FileLoader {

    public static InputStream loadAsStream(String filePath) {
        InputStream in = FileLoader.class.getClassLoader().getResourceAsStream(filePath);
        if (in == null)
            throw new IllegalStateException(String.format("No resource found: %s", filePath));
        return in;
    }

    public static URL loadAsUrl(String filePath) {
        URL url = FileLoader.class.getClassLoader().getResource(filePath);
        if (url == null)
            throw new IllegalStateException(String.format("FATAL! No resource found: %s", filePath));
        return url;
    }
}
