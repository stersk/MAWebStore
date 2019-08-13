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

    public static List<Item> findAll(){
        return ItemDAO.findAll();
    }
}
