package hooks;

import java.io.IOException;
import java.util.Properties;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import factory.BaseClass;
import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

public class Hooks {
    WebDriver driver;
    Properties p;

    @Before
    public void setup() throws IOException {
        driver = BaseClass.initilizeBrowser();
        p = BaseClass.getProperties();
        driver.get(p.getProperty("appURL")); // matches config.properties
        driver.manage().window().maximize();
    }

    @After
    public void tearDown(Scenario scenario) {
        WebDriver driver = BaseClass.getDriver();
        if (driver != null) {
            driver.quit();
        }
    }

    @AfterStep
    public void addScreenshot(Scenario scenario) {
        if (scenario.isFailed() && driver != null) {
            TakesScreenshot ts = (TakesScreenshot) driver;
            byte[] screenshot = ts.getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png", scenario.getName());
        }
    }
}
