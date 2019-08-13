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

    public static Cart closeCartById(Integer cartId){
        Cart cart = CartDAO.findById(cartId);
        if (cart != null) {
            cart.setClosed(true);
            CartDAO.update(cart);

            return cart;
        }

        return null;
    }

    public static Integer getCartSum(Cart cart){
        return CartDAO.getCartSum(cart.getId());
    }
}
