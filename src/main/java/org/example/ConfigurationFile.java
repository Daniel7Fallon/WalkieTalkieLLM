package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public final class ConfigurationFile {
    //Stores configuration
    private static  final Map<String, String> configMap = new HashMap<>();

    //Must be called before any other method
    public static void initialise(String filePath) {
        try(BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if(line.isEmpty() || line.startsWith("#")) continue;

                String[] tokens = line.split("\t", 2);
                if(tokens.length == 2) {
                    String key = tokens[0].trim();
                    String value = tokens[1].trim();
                    configMap.put(key, value);
                }
            }
            System.out.println("Configuration file loaded successfully.");
            System.out.println("ConfigMap contents: " + configMap);
        } catch (IOException e) {
            System.err.println("Error reading config file: " + e.getMessage());
        }
    }

    public static String getValue(String key) {
        String value = configMap.get(key);
        if(value == null) {
            throw new IllegalArgumentException("Config Property " + key + " not found.");
        }
        return value;
    }
    public static boolean containsKey(String key) {
        return configMap.containsKey(key);
    }
}
