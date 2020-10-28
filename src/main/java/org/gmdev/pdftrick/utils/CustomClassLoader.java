package org.gmdev.pdftrick.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

public class CustomClassLoader extends ClassLoader {
	
	private static final Logger logger = Logger.getLogger(CustomClassLoader.class);

    private final Map<String, Class<?>> classes = new HashMap<String, Class<?>>();
    
    @Override
    public String toString() {
        return CustomClassLoader.class.getName();
    }
    
    @Override
    public Class<?> findClass(String name) throws ClassNotFoundException {
        if (classes.containsKey(name)) {
            return classes.get(name);
        }
        
        byte[] classData;
        Class<?> c=null;
        
        try {
            classData = loadClassData(name);
            c = defineClass(name, classData, 0, classData.length);
            resolveClass(c);
            classes.put(name, c);
        } catch (IOException e) {
        	logger.error("Exception", e);
        }
        
        return c;
    }
     
    private byte[] loadClassData(String name) throws IOException {
    	BufferedInputStream in = new BufferedInputStream(CustomClassLoader.class.getResourceAsStream(
    	        "/" + name.replace(".", "/")+ ".class"));

    	ByteArrayOutputStream out = new ByteArrayOutputStream();
        int i;
        while ((i = in.read()) != -1) {
            out.write(i);
        }
        
        in.close();
        byte[] classData = out.toByteArray();
        out.close();
        
        return classData;
    }


}
