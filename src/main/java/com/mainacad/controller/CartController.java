package com.mainacad.controller;

import com.mainacad.model.Item;
import com.mainacad.model.Order;
import com.mainacad.model.User;
import com.mainacad.service.CartService;
import com.mainacad.service.ItemService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CartController extends HttpServlet{
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    req.setCharacterEncoding("UTF-8");
    resp.setCharacterEncoding("UTF-8");

    List<Order> orders = new ArrayList<>();
    List<Item> items = new ArrayList<>();
    User currentUser = null;

    Object userAttributeValue = req.getSession(false).getAttribute("user");
    if (userAttributeValue != null) {
      currentUser = (User) userAttributeValue;
      orders = CartService.getOrdersFromOpenCartByUser(currentUser.getId());
      items = orders
              .stream()
              .parallel()
              .map(order -> ItemService.findById(order.getItemId()))
              .collect(Collectors.toList());

    }

    req.setAttribute("user", currentUser);
    req.setAttribute("orders", orders);
    req.setAttribute("items", items);

    RequestDispatcher dispatcher = req.getRequestDispatcher("/jsp/cart.jsp");
    dispatcher.forward(req, resp);
  }
}
