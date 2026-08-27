package web.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ConfigReader {

    private static final String DEFAULT_CONFIG_FILE = "config.properties";
    private static volatile ConfigReader instance;

    private final Properties properties = new Properties();

    private ConfigReader() {
        loadProperties(DEFAULT_CONFIG_FILE);
    }

    public static ConfigReader getInstance() {
        if (instance == null) {
            synchronized (ConfigReader.class) {
                if (instance == null) {
                    instance = new ConfigReader();
                }
            }
        }
        return instance;
    }

    private void loadProperties(String fileName) {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(fileName)) {
            if (input == null) {
                throw new IllegalStateException("Config file not found on classpath: " + fileName);
            }
            properties.load(input);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load config file: " + fileName, e);
        }
    }

    public String get(String key, String defaultValue) {
        String systemProperty = System.getProperty(key);
        if (systemProperty != null && !systemProperty.isEmpty()) {
            return systemProperty;
        }

        String envVar = System.getenv(key.toUpperCase().replace('.', '_'));
        if (envVar != null && !envVar.isEmpty()) {
            return envVar;
        }

        return properties.getProperty(key, defaultValue);
    }

    public String get(String key) {
        String value = get(key, null);
        if (value == null) {
            throw new IllegalStateException("Missing required config property: " + key);
        }
        return value;
    }

    public int getInt(String key, int defaultValue) {
        String value = get(key, null);
        return value != null ? Integer.parseInt(value) : defaultValue;
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        String value = get(key, null);
        return value != null ? Boolean.parseBoolean(value) : defaultValue;
    }

    public String getEnvironment() {
        return get("environment", "qa");
    }

    public String getBaseUrl() {
        return get("base.url");
    }

    public String getBrowser() {
        return get("browser", "chrome");
    }

    public boolean isHeadless() {
        return getBoolean("headless", false);
    }

    public int getImplicitWaitSeconds() {
        return getInt("implicit.wait.seconds", 0);
    }

    public int getExplicitWaitSeconds() {
        return getInt("explicit.wait.seconds", 10);
    }

    public int getPageLoadTimeoutSeconds() {
        return getInt("page.load.timeout.seconds", 30);
    }

    public int getElementTimeoutSeconds() {
        return getInt("element.load.seconds", 2);
    }
}
