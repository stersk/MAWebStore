package com.mainacad.service;

import com.mainacad.dao.CartDAO;
import com.mainacad.dao.OrderDAO;
import com.mainacad.model.Cart;
import com.mainacad.model.Order;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CartService {

    public static Cart createCartForUser(Integer userId){
        Cart cart = new Cart();
        cart.setCreationTime(new Date().getTime());
        cart.setClosed(Boolean.FALSE);
        cart.setUserId(userId);
        return CartDAO.create(cart);
    }

    public static Cart findById(Integer id) {
        return CartDAO.findById(id);
    }

    public static Cart findOpenCartByUser(Integer userId){
        return CartDAO.findOpenCartByUser(userId);
    }

    public static Cart close(Integer catrId){
        return CartDAO.close(catrId);
    }

    public static Integer getCartSum(Cart cart){
        return CartDAO.getCartSum(cart.getId());
    }

//    TODO Tests
    public static List<Order> getOrdersFromOpenCartByUser(Integer userId) {
        List<Order> orders = new ArrayList<>();

        Cart openCart = CartDAO.findOpenCartByUser(userId);
        if (openCart != null) {
            orders = OrderDAO.findByCart(openCart.getId());
        }

        return orders;
    }
}
