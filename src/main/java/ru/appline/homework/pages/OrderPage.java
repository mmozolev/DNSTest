package ru.appline.homework.pages;

import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import ru.appline.homework.items.Item;

import java.util.List;

public class OrderPage extends BasePage {

    @FindBy(xpath = "//div[@class='cart-items__product']")
    private List<WebElement> orderList;

    @FindBy(xpath = "//div[@class='total-amount__label']//span[@class='price__current']")
    private WebElement shoppingCartPrice;

    @FindBy(xpath = "//div[@class='group-tabs']//span[@class='restore-last-removed']")
    private WebElement returnButton;

    @FindBy(xpath = "//span[@class='cart-link__badge']")
    private WebElement quantityItemInCart;

    /**
     * Метод проверяет гарантию у товара с именем itemName
     *
     * @param itemName - Имя товара
     * @return this - остаемя на странице
     */
    public OrderPage checkGaranty(String itemName) {

        WebElement element = findItemInOrder(itemName);
        int itemGaranty = 0;
        for (Item item : Item.items) {
            if (getElementName(element).equals(item.getName()))
                itemGaranty = item.getGuaranty() * 12;
            break;
        }
        Assertions.assertEquals(getElementGuarantee(element), itemGaranty, "Гарантия не соответствует выбранной");
        return this;

    }

    /**
     * Метод проверяет цену товара с именем itemName
     *
     * @param itemName - имя товара
     * @return this - остаемся на странице
     */
    public OrderPage checkItemPrice(String itemName) {
        WebElement element = findItemInOrder(itemName);
        for (Item item : Item.items) {
            if (getElementName(element).equals(item.getName())) {
                Assertions.assertEquals(item.getPrice() * item.getQuantity(),
                        getElementPrice(element) + getElementCount(element) * getElementGuaranteePrice(element),
                        "Не соответствие цены");
                break;
            }
        }
        return this;
    }

    /**
     * Метод проверяет итоговую сумму корзины
     *
     * @return this - остаемся на странице
     */
    public OrderPage checkOrderPrice() {
        int totalPrice = 0;
        for (WebElement element : orderList) {
            totalPrice += getElementPrice(element) + getElementCount(element) * getElementGuaranteePrice(element);
        }
        Assertions.assertEquals(totalPrice, Integer.parseInt(shoppingCartPrice.getText().replaceAll("[^\\w]", "")), "Не совпадает цена корзины");
        Assertions.assertEquals(Item.getTotalPrice(), totalPrice, "Не совпадает цена корзины");
        return this;
    }

    /**
     * Метод удаляет товар с именем itemName из корзины
     *
     * @param itemName - имя товара
     * @return this - остаемся на странице
     */
    public OrderPage deleteItem(String itemName) {
        WebElement element = findItemInOrder(itemName);
        WebElement button = getDeleteButton(element);

        String stringPriceBefore = getNotParsingElementPrice(shoppingCartPrice);
        int priceBefore = Integer.parseInt(stringPriceBefore.replaceAll("[^\\w]", ""));
        int itemPrice = 0;
        int elementCount = getElementCount(element);

        for (Item item : Item.items) {
            if (getElementName(element).equals(item.getName())) {
                item.setQuantity(0);
                itemPrice = item.getPrice();
                break;
            }
        }

        button.click();
        elementIsChanged(shoppingCartPrice, stringPriceBefore);
        Assertions.assertEquals(priceBefore, getCartPrice() + itemPrice * elementCount, "Цена не совпадает");
        Assertions.assertThrows(NoSuchElementException.class, () -> findItemInOrder(itemName));
        return this;
    }

    /**
     * Метод увеличивает количество товара с именем itemName на количество count
     *
     * @param itemName - имя товара
     * @param count    - число, на которое нужно увеличить
     * @return this - остаемся на странице
     */
    public OrderPage addItem(String itemName, int count) {
        WebElement element = findItemInOrder(itemName);
        WebElement button = getAddButton(element);

        String stringPriceBefore = getNotParsingCartPrice();
        int priceBefore = Integer.parseInt(stringPriceBefore.replaceAll("[^\\w]", ""));
        int itemPrice = 0;
        int elementCount = getElementCount(element);

        if (count <= 0) return pageManager.getOrderPage();

        for (Item item : Item.items) {
            if (getElementName(element).equals(item.getName())) {
                item.setQuantity(item.getQuantity() + count);
                itemPrice = item.getPrice();
                break;
            }
        }

        scrollWithOffset(element, 0, -400);
        for (int i = 0; i < count; i++) {
            elementCount += 1;
            button.click();
            wait.until(ExpectedConditions.textToBePresentInElementValue(element.findElement(By
                    .xpath(".//input[@class='count-buttons__input']")), String.valueOf(elementCount)));
        }


        Assertions.assertEquals(priceBefore, getCartPrice() - (count * itemPrice),
                "Не совпадает цена после добавления");

        return this;
    }

    /**
     * Метод возвращающий удаленный товар в корзину
     *
     * @return this - остаемся на странице
     */
    public OrderPage returnItemToOrder() {
        int cartPrice = getCartPrice();
        elementToBeClickable(returnButton).click();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException ignore) {
        }


        for (WebElement element : orderList) {
            for (Item item : Item.items) {
                if (getElementName(element).equals(item.getName())) {
                    if (getElementCount(element) != item.getQuantity()) {
                        item.setQuantity(getElementCount(element));
                        Assertions.assertEquals(getCartPrice(), cartPrice + item.getPrice() * item.getQuantity(),
                                "Не совпадает цена после возврата");
                    }
                }
            }
        }
        return this;
    }


    /**
     * Метод увиличивающий число товара с именем itemName на 1 единицу
     *
     * @param name - имя товара
     * @return this - остаемся на странцие
     */
    public OrderPage addItem(String name) {
        return addItem(name, 1);
    }

    /**
     * Метод для поиска определенного товара с именем itemName в корзине
     *
     * @param itemName - имя товара
     * @return возвращает необходимый товар
     */
    private WebElement findItemInOrder(String itemName) {
        for (WebElement element : orderList) {
            if (element.findElement(By.xpath(".//a[@class='cart-items__product-name-link']")).getText().contains(itemName)) {
                return element;
            }
        }
        throw new NoSuchElementException("");
    }

    private String getElementName(WebElement element) {
        return element.findElement(By.xpath(".//a[@class='cart-items__product-name-link']")).getText();
    }

    private int getElementPrice(WebElement element) {
        return Integer.parseInt(element.findElement(By.xpath(".//span[@class='price__current']"))
                .getText().replaceAll("[^\\w]", ""));
    }

    private int getCartPrice() {
        return Integer.parseInt(shoppingCartPrice.getText().replaceAll("[^\\w]", ""));
    }

    private String getNotParsingCartPrice() {
        return shoppingCartPrice.getText();
    }

    private String getNotParsingElementPrice(WebElement element) {
        return element.getText();
    }

    private int getElementCount(WebElement element) {
        return Integer.parseInt(element.findElement(By.xpath(".//input[@class='count-buttons__input']"))
                .getAttribute("value"));
    }

    private int getElementGuarantee(WebElement element) {
        try {
            return Integer.parseInt(element.findElement(By
                    .xpath(".//span[@class='base-ui-radio-button__icon base-ui-radio-button__icon_checked']"))
                    .getText().replaceAll("[^\\w]", ""));
        } catch (NoSuchElementException e) {
            return 0;
        }
    }

    private WebElement getAddButton(WebElement element) {
        return element.findElement(By.xpath(".//button[@data-commerce-action='CART_ADD']"));
    }

    private WebElement getDeleteButton(WebElement element) {
        return element.findElement(By.xpath(".//button[.='Удалить']"));
    }

    private int getElementGuaranteePrice(WebElement element) {
        wait.until(ExpectedConditions.visibilityOf(element));
        int i;
        try {
            i = Integer.parseInt(element.findElement(By
                    .xpath(".//span[@class='base-ui-radio-button__icon base-ui-radio-button__icon_checked']/../" +
                            "..//span[@class='additional-warranties-row__price']"))
                    .getText().replaceAll("[^\\w]", ""));
        } catch (NoSuchElementException e) {
            i = 0;
        }
        return i;
    }


}
