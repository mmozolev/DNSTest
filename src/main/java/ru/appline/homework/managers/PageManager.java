package ru.appline.homework.managers;

import ru.appline.homework.pages.ItemPage;
import ru.appline.homework.pages.OrderPage;
import ru.appline.homework.pages.SearchPage;
import ru.appline.homework.pages.StartPage;

public class PageManager {

    private static PageManager pageManager;

    private static StartPage startPage;

    private static SearchPage searchPage;

    private static ItemPage itemPage;

    private static OrderPage orderPage;

    private PageManager() {

    }

    public static PageManager getPageManager() {
        if (pageManager == null) {
            pageManager = new PageManager();
        }
        return pageManager;
    }

    public StartPage getStartPage() {
        if (startPage == null) {
            startPage = new StartPage();
        }
        return startPage;
    }

    public SearchPage getSearchPage() {
        if (searchPage == null) {
            searchPage = new SearchPage();
        }
        return searchPage;
    }

    public ItemPage getItemPage() {
        if (itemPage == null) {
            itemPage = new ItemPage();
        }
        return itemPage;
    }

    public OrderPage getOrderPage() {
        if (orderPage == null) {
            orderPage = new OrderPage();
        }
        return orderPage;
    }
}
