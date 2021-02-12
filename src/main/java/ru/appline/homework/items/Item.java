package ru.appline.homework.items;

import lombok.Data;

import java.util.ArrayList;

@Data
public class Item {

    private String name;
    private int price;
    private int guaranty;
    private int quantity;
    private int guarantyPrice;

    public static ArrayList<Item> items = new ArrayList<>();

    public static int getTotalPrice() {
        int result = 0;
        for (Item item : items) {
            result += (item.price * item.quantity);
        }
        return result;
    }

}
