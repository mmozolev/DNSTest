package ru.appline.homework.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;
import ru.appline.homework.items.Item;


import java.util.List;


public class ItemPage extends BasePage {

    @FindBy(xpath = "//h1")
    WebElement elementName;

    @FindBy(xpath = "//span[contains(@class,'product-card-price__current')]")
    WebElement elementPrice;

    @FindBy(xpath = "//select")
    WebElement guaranteeSelect;

    @FindBy(xpath = "//button[.='Купить']")
    WebElement addToShoppingCartButton;

    private int guarantee;

    public ItemPage changeGuarantee(String guaranteePeriod) {
        //String price = getPrice();
        try {
            Select select = new Select(guaranteeSelect);
            List<WebElement> options = select.getOptions();
            for (WebElement element : options) {
                if (element.getAttribute("index").equals(guaranteePeriod)) {
                    select.selectByIndex(Integer.parseInt(element.getAttribute("index")));
                    try {
                        guarantee = getAndParseGuarantee(element);
                    } catch (NumberFormatException ignore) {
                    }
                }
            }
        } catch (NoSuchElementException ignore) {
        }

        return pageManager.getItemPage();
    }

    public ItemPage addItemToShoppingCart() {
        Item item = new Item();
        item.setName(getItemName());
        item.setPrice(getItemPrice());
        item.setQuantity(1);
        item.setGuaranty(guarantee);
        Item.items.add(item);
        guarantee = 0;

        addToShoppingCartButton.click();
        isElementExist(By.xpath("//button[@class='button-ui buy-btn button-ui_brand button-ui_passive-done']"));
        return pageManager.getItemPage();
    }

    private int getItemPrice() {
        return Integer.parseInt(elementIsVisible(elementPrice).getText().replaceAll("[^\\w]", ""));
    }

    private String getItemName() {
        return elementName.getText();

    }

    private int getAndParseGuarantee(WebElement element) {
        return Integer.parseInt((element).getText().replaceAll("[^\\w]", ""));
    }

}
