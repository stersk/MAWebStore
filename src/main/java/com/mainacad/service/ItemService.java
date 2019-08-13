package com.mainacad.service;


import com.mainacad.dao.ItemDAO;
import com.mainacad.model.Item;

import java.util.List;

public class ItemService {

    public static Item create(Item item){
        return ItemDAO.create(item);
    }

    public static Item findById(Integer id){
        return ItemDAO.findById(id);
    }

    public static Item findByCode(String code) {
        List<Item> items = ItemDAO.findByItemCode(code);
        if (items.isEmpty()) {
            return null;
        } else {
            return items.get(0);
        }
    }

    public static List<Item> getAllItems() {
        return ItemDAO.findAll();
    }

    public static List<Item> getItemsWithPriceBetween(Integer from, Integer to){
        return ItemDAO.findByItemPriceBetween(from, to);
    }

    public static Item setItemPrice(Item item, Integer price){
       item.setPrice(price);

       return ItemDAO.update(item);
    }
}
