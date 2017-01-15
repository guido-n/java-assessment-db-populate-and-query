package com.guido.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Utils {

    private static final Logger log = LoggerFactory.getLogger(Utils.class);

    public static final String EMPTY_STRING = "";

    /**
     * Load a resource as a String
     *
     * @param resourcePath
     * @param clazz class used to access the resource
     *
     * @return
     */
    public static String loadStringFromResource(String resourcePath, Class<?> clazz) {
        String resource = EMPTY_STRING;
        try {
            resource = new String(Files.readAllBytes(Paths.get(clazz.getResource(resourcePath).toURI())), StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("Couldn't load resource: {}", resourcePath, e);
        }
        return resource;
    }

    /**
     *
     * @param s
     * @return
     */
    public static boolean notNullAndNotEmpty(String s) {
        return s != null && !s.trim().isEmpty();
    }
}
