package ru.appline.homework.baseTest;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import ru.appline.homework.managers.DriverManager;
import ru.appline.homework.managers.InitManager;
import ru.appline.homework.managers.PageManager;
import ru.appline.homework.managers.PropertyManager;
import ru.appline.homework.utils.PropertyConst;

public class BaseTest {

    protected PageManager app = PageManager.getPageManager();

    @BeforeAll
    public static void beforeAll() {
        InitManager.initFramework();
    }

    @BeforeEach
    public void beforeEach() {
        DriverManager.getWebDriver().get(PropertyManager.getPropertyManager().getProperty(PropertyConst.APP_URL));
    }

    @AfterAll
    public static void afterAll() {
        InitManager.quitFramework();
    }
}
