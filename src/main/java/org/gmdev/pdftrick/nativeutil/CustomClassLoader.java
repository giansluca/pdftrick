package org.gmdev.pdftrick.nativeutil;

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
        
        byte[] classData;
        Class<?> classObject;
        
        try {
            classData = loadClassData(className);
            classObject = defineClass(className, classData, 0, classData.length);
            resolveClass(classObject);
            classes.put(className, classObject);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        
        return classObject;
    }
     
    private byte[] loadClassData(String className) throws IOException {
    	String slashedClassPath = className.replace(".", "/");
        String classFilePath = String.format("/%s.class", slashedClassPath);

        BufferedInputStream in = new BufferedInputStream(
                CustomClassLoader.class.getResourceAsStream(classFilePath));

    	ByteArrayOutputStream out = new ByteArrayOutputStream();
        int i;
        while ((i = in.read()) != -1)
            out.write(i);
        
        in.close();
        byte[] classData = out.toByteArray();
        out.close();
        
        return classData;
    }


}
