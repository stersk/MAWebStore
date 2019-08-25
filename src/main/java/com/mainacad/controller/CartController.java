package com.mainacad.controller;

import com.mainacad.dao.OrderDAO;
import com.mainacad.model.Item;
import com.mainacad.model.Order;
import com.mainacad.model.User;
import com.mainacad.service.CartService;
import com.mainacad.service.ItemService;
import com.mainacad.service.OrderService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
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

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    req.setCharacterEncoding("UTF-8");
    resp.setCharacterEncoding("UTF-8");

    String action = req.getParameter("action");
    Boolean success = false;

    if(action.equals("removeFromOpenCart")) {
      Integer orderId = Integer.parseInt(req.getParameter("orderId"));
      OrderDAO.delete(orderId);

      success = true;

    } else if (action.equals("updateItemAmountInOrder")) {
      Integer orderId = Integer.parseInt(req.getParameter("orderId"));
      Integer amount = Integer.parseInt(req.getParameter("amount"));

      if (amount.equals(0)) {
        OrderService.deleteOrder(orderId);
        success = true;

      } else {
        if (OrderService.updateItemAmountInOrder(orderId, amount) != null) {
          success = true;
        };
      }

    } else {
      super.doPost(req, resp);
    }

    if (success) {
      PrintWriter respWriter = resp.getWriter();
      resp.setContentType("application/json");
      resp.setCharacterEncoding("UTF-8");

      respWriter.print("{}");
      respWriter.flush();
    }
  }
}
