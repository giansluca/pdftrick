package org.gmdev.pdftrick.utils;

import java.io.InputStream;
import java.net.URL;

public class FileLoader {

    private FileLoader() {
        throw new AssertionError("FileLoader class should never be instantiated");
    }

    public static InputStream loadFileAsStream(String filePath) {
        InputStream in = FileLoader.class.getClassLoader().getResourceAsStream(filePath);
        if (in == null)
            throw new IllegalStateException(String.format("No resource found: %s", filePath));
        return in;
    }

    public static InputStream loadClassAsStream(String classPath) {
        InputStream in = FileLoader.class.getResourceAsStream(classPath);
        if (in == null)
            throw new IllegalStateException(String.format("No class found: %s", classPath));
        return in;
    }

    public static URL loadFileAsUrl(String filePath) {
        URL url = FileLoader.class.getClassLoader().getResource(filePath);
        if (url == null)
            throw new IllegalStateException(String.format("FATAL! No resource found: %s", filePath));
        return url;
    }
}
