package ru.appline.homework.tests;

import org.junit.jupiter.api.Test;
import ru.appline.homework.baseTest.BaseTest;
import ru.appline.homework.items.Item;

public class DNSTest extends BaseTest {

    @Test
    void test() {
        app.getStartPage()
                .searchItem("playstation")
                .goToItemPage("PlayStation")
                .changeGuarantee("2")
                .addItemToShoppingCart()
                .searchItemAndGoToItemPage("Detroit")
                .addItemToShoppingCart()
                .checkListItemsPrice()
                .goToOrderPage()
                .checkGaranty("PlayStation")
                .checkItemPrice("PlayStation")
                .checkItemPrice("Detroit")
                .checkOrderPrice()
                .deleteItem("Detroit")
                .addItem("PlayStation", 2)
                .returnItemToOrder();
    }
}
