package org.gmdev.pdftrick.nativeutil;

import org.gmdev.pdftrick.utils.FileLoader;

import java.io.*;
import java.util.*;

public class CustomClassLoader extends ClassLoader {

    private final Map<String, Class<?>> classes = new HashMap<>();

    @Override
    public String toString() {
        return CustomClassLoader.class.getName();
    }

    @Override
    public Class<?> findClass(String className) {
        if (classes.containsKey(className))
            return classes.get(className);

        byte[] classDataBytes = loadClassDataBytes(className);
        Class<?> classObject = defineClass(className, classDataBytes, 0, classDataBytes.length);
        resolveClass(classObject);
        classes.put(className, classObject);

        return classObject;
    }

    private byte[] loadClassDataBytes(String packagedClassPath) {
        String slashedClassPath = packagedClassPath.replace(".", "/");
        String classFilePath = String.format("/%s.class", slashedClassPath);

        byte[] classDataBytes;
        try (InputStream in = FileLoader.loadClassAsStream(classFilePath)) {
            classDataBytes = in.readAllBytes();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        return classDataBytes;
    }

    public void removeClass(String className) {
        classes.remove(className);
    }


}
