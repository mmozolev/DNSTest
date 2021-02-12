package ru.appline.homework.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class SearchPage extends BasePage {

    @FindBy(xpath = "//div[@class='n-catalog-product__main']")
    List<WebElement> listOfItem;

    /**
     * Метод осуществляет переход на страницу товара
     *
     * @param itemName - имя товара
     * @return ItemPage - страница товара
     */
    public ItemPage goToItemPage(String itemName) {
        WebElement name = null;
        for (WebElement element : listOfItem) {
            if (name != null) break;
            try {
                name = element.findElement(By.xpath(String.format(".//a[contains(text(), '%s')]", itemName)));
            } catch (NoSuchElementException ignore) {
            }
        }
        name.click();
        return pageManager.getItemPage();
    }
}
