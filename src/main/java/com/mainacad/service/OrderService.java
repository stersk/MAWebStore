package com.mainacad.service;

import com.mainacad.dao.OrderDAO;
import com.mainacad.model.Cart;
import com.mainacad.model.Item;
import com.mainacad.model.Order;
import com.mainacad.model.User;

import java.util.List;

public class OrderService {

    public static Order createOrderByItemAndUser(Item item, Integer amount, User user){
        Order order = new Order();
        order.setItemId(item.getId());
        order.setAmount(amount);
        // get or create open cart
        Cart cart = CartService.findOpenCartByUser(user.getId());
        if (cart == null) {
            cart = CartService.createCartForUser(user.getId());
        }
        order.setCartId(cart.getId());
        return OrderDAO.create(order);
    }

    public static List<Order> getOrdersByCatr(Cart cart){
        return OrderDAO.findByCart(cart.getId());
    }

    public List<Order> findClosedOrdersByUserAndPeriod(User user, Long from, Long to){
        return OrderDAO.findClosedOrdersByUserIdAndPeriod(user.getId(), from, to);
    }
}
