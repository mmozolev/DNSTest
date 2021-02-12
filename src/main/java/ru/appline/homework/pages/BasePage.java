package ru.appline.homework.pages;

import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.appline.homework.items.Item;
import ru.appline.homework.managers.DriverManager;
import ru.appline.homework.managers.PageManager;
import ru.appline.homework.managers.PropertyManager;

import java.util.concurrent.TimeUnit;

import static ru.appline.homework.managers.DriverManager.getWebDriver;
import static ru.appline.homework.utils.PropertyConst.IMPLICITLY_WAIT;

public class BasePage {

    @FindBy(xpath = "//input[@placeholder='Поиск по сайту']")
    private WebElement searchLine;

    @FindBy(xpath = "//a[@class='ui-link cart-link']")
    private WebElement shoppingCart;

    @FindBy(xpath = "//span[@class='cart-link__price']")
    private WebElement shoppingCartPrice;

    protected PageManager pageManager = PageManager.getPageManager();

    protected JavascriptExecutor js = (JavascriptExecutor) getWebDriver();

    protected WebDriverWait wait = new WebDriverWait(getWebDriver(), 10, 1000);

    private final PropertyManager properties = PropertyManager.getPropertyManager();


    public BasePage() {
        PageFactory.initElements(getWebDriver(), this);
    }

    public SearchPage searchItem(String item) {
        wait.until(ExpectedConditions.visibilityOf(searchLine));
        fillInputField(searchLine, item);
        searchLine.sendKeys(Keys.ENTER);
        return pageManager.getSearchPage();
    }

    public OrderPage goToOrderPage() {
        shoppingCart.click();
        return pageManager.getOrderPage();
    }

    public ItemPage searchItemAndGoToItemPage(String itemName) {
        fillInputField(searchLine, itemName);
        searchLine.sendKeys(Keys.ENTER);
        return pageManager.getItemPage();
    }

    public BasePage checkListItemsPrice() {
        Assertions.assertEquals(Item.getTotalPrice(), parsePrice(shoppingCartPrice));
        return this;
    }

    protected int parsePrice(WebElement element) {
        return Integer.parseInt(element.getText().replaceAll("[^\\w]", ""));
    }


    protected void scrollToElementJs(WebElement element) {
        js.executeScript("arguments[0].scrollIntoView(true);", element);
    }

    protected WebElement elementToBeClickable(WebElement element) {
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    protected void elementIsChanged(WebElement element, String text) {
        wait.until(ExpectedConditions.not(ExpectedConditions.textToBePresentInElement(element, text)));
    }

    protected WebElement elementIsVisible(WebElement element) {
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    protected void fillInputField(WebElement field, String value) {
        scrollToElementJs(field);
        field.click();
        field.sendKeys(value);
    }

    public void scrollWithOffset(WebElement element, int x, int y) {
        String code = "window.scroll(" + (element.getLocation().x + x) + ","
                + (element.getLocation().y + y) + ");";
        ((JavascriptExecutor) DriverManager.getWebDriver()).executeScript(code, element, x, y);
    }

    public boolean isElementExist(By by) {
        boolean flag = false;
        try {
            getWebDriver().manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
            getWebDriver().findElement(by);
            flag = true;
        } catch (NoSuchElementException ignore) {
        } finally {
            getWebDriver().manage().timeouts().implicitlyWait(Integer.parseInt(properties.getProperty(IMPLICITLY_WAIT)), TimeUnit.SECONDS);
        }
        return flag;
    }


}
