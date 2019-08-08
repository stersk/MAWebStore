package com.mainacad.service;

import com.mainacad.dao.CartDAO;
import com.mainacad.model.Cart;

import java.util.Date;

public class CartService {

    public static Cart createCartForUser(Integer userId){
        Cart cart = new Cart();
        cart.setCreationTime(new Date().getTime());
        cart.setClosed(Boolean.FALSE);
        cart.setUserId(userId);
        return CartDAO.create(cart);
    }

    public static Cart findOpenCartByUser(Integer userId){
        return CartDAO.findOpenCartByUser(userId);
    }
}
