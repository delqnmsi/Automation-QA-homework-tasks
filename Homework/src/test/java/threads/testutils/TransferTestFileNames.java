package threads.testutils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class TransferTestFileNames {

    private static final String RESOURCE_NAME = "threads.properties";

    private final String alphaName;
    private final String betaName;
    private final String gammaName;
    private final String alphaKey;
    private final String betaKey;
    private final String gammaKey;
    private final String duplicateName;
    private final String good1Name;
    private final String brokenName;
    private final String good2Name;
    private final String good1Key;
    private final String good2Key;
    private final String rejectedName;
    private final String slowName;
    private final String f1Name;
    private final String f2Name;
    private final String f3Name;
    private final String f4Name;
    private final String big1Name;
    private final String big2Name;
    private final String big3Name;
    private final String big4Name;
    private final String big5Name;
    private final String reportName;

    public TransferTestFileNames() {
        Properties properties = load(RESOURCE_NAME);
        alphaName = properties.getProperty("file.alpha");
        betaName = properties.getProperty("file.beta");
        gammaName = properties.getProperty("file.gamma");
        alphaKey = properties.getProperty("key.alpha");
        betaKey = properties.getProperty("key.beta");
        gammaKey = properties.getProperty("key.gamma");
        duplicateName = properties.getProperty("file.duplicate");
        good1Name = properties.getProperty("file.good1");
        brokenName = properties.getProperty("file.broken");
        good2Name = properties.getProperty("file.good2");
        good1Key = properties.getProperty("key.good1");
        good2Key = properties.getProperty("key.good2");
        rejectedName = properties.getProperty("file.rejected");
        slowName = properties.getProperty("file.slow");
        f1Name = properties.getProperty("file.f1");
        f2Name = properties.getProperty("file.f2");
        f3Name = properties.getProperty("file.f3");
        f4Name = properties.getProperty("file.f4");
        big1Name = properties.getProperty("file.big1");
        big2Name = properties.getProperty("file.big2");
        big3Name = properties.getProperty("file.big3");
        big4Name = properties.getProperty("file.big4");
        big5Name = properties.getProperty("file.big5");
        reportName = properties.getProperty("file.report");
    }

    public static Properties load(String resourceName) {
        Properties properties = new Properties();
        try (InputStream input = TransferTestFileNames.class.getClassLoader().getResourceAsStream(resourceName)) {
            if (input == null) {
                throw new IllegalStateException(resourceName + " not found on classpath");
            }
            properties.load(input);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load " + resourceName, e);
        }
        return properties;
    }

    public String alphaName() {
        return alphaName;
    }

    public String betaName() {
        return betaName;
    }

    public String gammaName() {
        return gammaName;
    }

    public String alphaKey() {
        return alphaKey;
    }

    public String betaKey() {
        return betaKey;
    }

    public String gammaKey() {
        return gammaKey;
    }

    public String duplicateName() {
        return duplicateName;
    }

    public String good1Name() {
        return good1Name;
    }

    public String brokenName() {
        return brokenName;
    }

    public String good2Name() {
        return good2Name;
    }

    public String good1Key() {
        return good1Key;
    }

    public String good2Key() {
        return good2Key;
    }

    public String rejectedName() {
        return rejectedName;
    }

    public String slowName() {
        return slowName;
    }

    public String f1Name() {
        return f1Name;
    }

    public String f2Name() {
        return f2Name;
    }

    public String f3Name() {
        return f3Name;
    }

    public String f4Name() {
        return f4Name;
    }

    public String big1Name() {
        return big1Name;
    }

    public String big2Name() {
        return big2Name;
    }

    public String big3Name() {
        return big3Name;
    }

    public String big4Name() {
        return big4Name;
    }

    public String big5Name() {
        return big5Name;
    }

    public String reportName() {
        return reportName;
    }
}
