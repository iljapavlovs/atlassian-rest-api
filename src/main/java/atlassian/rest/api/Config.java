package atlassian.rest.api;


import atlassian.rest.api.properties.ConfigProperty;

import java.util.Properties;

public class Config {

    private static Properties properties = new Properties();

    public static void loadConfigProperties(String[] args) {
        if (args.length != ConfigProperty.values().length) throw new RuntimeException("Invalid number of arguments");
        for (int i = 0; i < args.length; i++) {
            properties.put(ConfigProperty.findById(i), args[i]);
        }
    }

    public static String getValue(ConfigProperty property) {
        Object value = properties.get(property);
        if (value != null) {
            return String.valueOf(value).trim();
        }
        throw new RuntimeException("Property " + property.name() + " is not found in the config file!");
    }
}