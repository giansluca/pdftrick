package org.gmdev.pdftrick.utils;

import java.io.InputStream;
import java.net.URL;

public class FileLoader {

    public static InputStream loadAsStream(String filename) {
        InputStream in = FileLoader.class.getClassLoader().getResourceAsStream(filename);
        if (in == null)
            throw new IllegalStateException(String.format("FATAL! No resource found: %s", filename));
        return in;
    }

    public static URL loadAsUrl(String filename) {
        URL url = FileLoader.class.getClassLoader().getResource(filename);
        if (url == null)
            throw new IllegalStateException(String.format("FATAL! No resource found: %s", filename));
        return url;
    }
}
