package org.gmdev.pdftrick.nativeutil;

import java.io.*;
import java.util.*;
import java.util.Map;

import org.apache.log4j.Logger;

public class CustomClassLoader extends ClassLoader {
	
	private static final Logger logger = Logger.getLogger(CustomClassLoader.class);

    private final Map<String, Class<?>> classes = new HashMap<>();
    
    @Override
    public String toString() {
        return CustomClassLoader.class.getName();
    }
    
    @Override
    public Class<?> findClass(String name) {
        if (classes.containsKey(name))
            return classes.get(name);
        
        byte[] classData;
        Class<?> classObject = null;
        
        try {
            classData = loadClassData(name);
            classObject = defineClass(name, classData, 0, classData.length);
            resolveClass(classObject);
            classes.put(name, classObject);
        } catch (IOException e) {
        	logger.error("Exception", e);
        }
        
        return classObject;
    }
     
    private byte[] loadClassData(String name) throws IOException {
    	BufferedInputStream in = new BufferedInputStream(CustomClassLoader.class.getResourceAsStream(
    	        "/" + name.replace(".", "/")+ ".class"));

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
