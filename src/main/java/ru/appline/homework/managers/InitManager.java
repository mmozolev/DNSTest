package ru.appline.homework.managers;

import java.util.concurrent.TimeUnit;

import static ru.appline.homework.managers.DriverManager.getWebDriver;
import static ru.appline.homework.managers.DriverManager.quitWebDriver;
import static ru.appline.homework.utils.PropertyConst.*;

public class InitManager {

    private static final PropertyManager properties = PropertyManager.getPropertyManager();

    public static void initFramework() {
        DriverManager.getWebDriver().manage().window().maximize();
        DriverManager.getWebDriver().manage().timeouts().implicitlyWait(Integer.parseInt(properties.getProperty(IMPLICITLY_WAIT)), TimeUnit.SECONDS);
        DriverManager.getWebDriver().manage().timeouts().pageLoadTimeout(Integer.parseInt(properties.getProperty(PAGE_LOAD_WAIT)), TimeUnit.SECONDS);
    }

    public static void quitFramework() {
        DriverManager.quitWebDriver();
    }
}
