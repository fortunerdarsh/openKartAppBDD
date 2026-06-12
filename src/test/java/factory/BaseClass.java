package factory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.Duration;
import java.util.Properties;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

public class BaseClass {

    private static WebDriver driver;
    private static Properties p;
    private static Logger logger = LogManager.getLogger(BaseClass.class);

    public static WebDriver initilizeBrowser() throws IOException {
        Properties props = getProperties();

        String env = props.getProperty("execution_env");
        String os = props.getProperty("os");
        String browser = props.getProperty("browser");

        if (env == null || browser == null) {
            throw new IllegalArgumentException("Missing execution_env or browser property in config.properties");
        }

        if (env.equalsIgnoreCase("remote")) {
            DesiredCapabilities capabilities = new DesiredCapabilities();

            if (os != null) {
                switch (os.toLowerCase()) {
                    case "windows": capabilities.setPlatform(Platform.WIN11); break;
                    case "mac": capabilities.setPlatform(Platform.MAC); break;
                    case "linux": capabilities.setPlatform(Platform.LINUX); break;
                    default: System.out.println("No matching OS"); break;
                }
            }

            switch (browser.toLowerCase()) {
                case "chrome": capabilities.setBrowserName("chrome"); break;
                case "edge": capabilities.setBrowserName("MicrosoftEdge"); break;
                default: System.out.println("No matching browser"); break;
            }

            driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), capabilities);

        } else if (env.equalsIgnoreCase("local")) {
            switch (browser.toLowerCase()) {
                case "chrome": driver = new ChromeDriver(); break;
                case "edge": driver = new EdgeDriver(); break;
                default: System.out.println("No matching browser"); driver = null; break;
            }
        }

        if (driver != null) {
            driver.manage().deleteAllCookies();
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(5));
        }

        return driver;
    }

    public static WebDriver getDriver() {
        return driver;
    }

    public static Properties getProperties() throws IOException {
        if (p == null) {
            try (InputStream input = BaseClass.class.getClassLoader()
                    .getResourceAsStream("config.properties")) {
                if (input == null) {
                    throw new FileNotFoundException("config.properties not found in classpath");
                }
                p = new Properties();
                p.load(input);
            }
        }
        return p;
    }

    public static Logger getLogger() {
        return logger;
    }

    public static String randomeString() {
        return RandomStringUtils.randomAlphabetic(5);
    }

    public static String randomeNumber() {
        return RandomStringUtils.randomNumeric(10);
    }

    public static String randomAlphaNumeric() {
        return RandomStringUtils.randomAlphabetic(5) + RandomStringUtils.randomNumeric(10);
    }
}
