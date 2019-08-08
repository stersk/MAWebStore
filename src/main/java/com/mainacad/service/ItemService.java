package com.mainacad.service;


import com.mainacad.dao.ItemDAO;
import com.mainacad.model.Item;

public class ItemService {

    public static Item create(Item item){
        return ItemDAO.create(item);
    }

    public static Item findById(Integer id){
        return ItemDAO.findById(id);
    }
}
