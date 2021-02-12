package ru.appline.homework.managers;

import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;

import static ru.appline.homework.utils.PropertyConst.*;

public class DriverManager {

    private static WebDriver webDriver;

    private static DriverManager INSTANCE = null;

    private final PropertyManager properties = PropertyManager.getPropertyManager();

    private DriverManager() {
        initDriver(PATH_GECKO_WEBDRIVER, PATH_CHROME_WEBDRIVER);
    }

    public static WebDriver getWebDriver() {
        if (INSTANCE == null) {
            INSTANCE = new DriverManager();
        }
        return webDriver;
    }

    public static void quitWebDriver() {
        if (webDriver != null) {
            webDriver.quit();
            webDriver = null;
        }
    }

    private void initDriver(String gecko, String chrome) {
        switch (properties.getProperty(TYPE_BROWSER)) {
            case "firefox":
                System.setProperty("webdriver.gecko.driver", properties.getProperty(gecko));
                webDriver = new FirefoxDriver();
                break;
            case "chrome":
                System.setProperty("webdriver.chrome.driver", properties.getProperty(chrome));
                ChromeOptions ops = new ChromeOptions();
                ops.setPageLoadStrategy(PageLoadStrategy.EAGER);
                webDriver = new ChromeDriver(ops);
                break;
            default:
                Assertions.fail("Типа браузера '" + properties.getProperty(TYPE_BROWSER) + "' не существует во фреймворке");
        }
    }
}
