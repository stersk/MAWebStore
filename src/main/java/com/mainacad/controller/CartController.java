package com.mainacad.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mainacad.model.Cart;
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
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

public class CartController extends HttpServlet{
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    req.setCharacterEncoding("UTF-8");
    resp.setCharacterEncoding("UTF-8");

    List<Order> orders = new ArrayList<>();
    List<Item> items = new ArrayList<>();
    Integer cartSum = 0;
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

      Cart openCart = CartService.findOpenCartByUser(currentUser.getId());
      if (openCart != null) {
        cartSum = CartService.getCartSum(openCart);
      }
    }

    req.setAttribute("user", currentUser);
    req.setAttribute("orders", orders);
    req.setAttribute("items", items);
    req.setAttribute("cartSum", cartSum);

    RequestDispatcher dispatcher = req.getRequestDispatcher("/jsp/cart.jsp");
    dispatcher.forward(req, resp);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    req.setCharacterEncoding("UTF-8");
    resp.setCharacterEncoding("UTF-8");

    User currentUser = null;
    String action = req.getParameter("action");

    Object userAttributeValue = req.getSession(false).getAttribute("user");
    if (userAttributeValue != null) {
      currentUser = (User) userAttributeValue;
    };

    String jsonRespond = null;

    if(action.equals("removeFromOpenCart")) {
      Integer orderId = Integer.parseInt(req.getParameter("orderId"));

      jsonRespond = deleteOrderAndReturnCartSumInJson(orderId);

    } else if (action.equals("updateItemAmountInOrder")) {
      Integer orderId = Integer.parseInt(req.getParameter("orderId"));
      Integer amount = Integer.parseInt(req.getParameter("amount"));

      if (amount.equals(0)) {
        jsonRespond = deleteOrderAndReturnCartSumInJson(orderId);

      } else {
        Order updatedOrder = OrderService.updateItemAmountInOrder(orderId, amount);
        if (updatedOrder != null) {
          Cart cart = CartService.findById(updatedOrder.getCartId());
          Integer cartSum = CartService.getCartSum(cart);
          Map<String, String> respondData = new HashMap<>();
          respondData.put("cartSum", cartSum.toString());

          ObjectMapper mapper = new ObjectMapper();
          jsonRespond = mapper.writeValueAsString(respondData);
        }
      }

    } else if (action.equals("confirm")) {
      if (currentUser != null) {
        Cart openedCart = CartService.findOpenCartByUser(currentUser.getId());

        Integer itemsCount = OrderService.getOrdersByCart(openedCart).size();
        Integer cartSum = CartService.getCartSum(openedCart);

        if (itemsCount > 0) {
          openedCart = CartService.close(openedCart.getId());

          req.setAttribute("user", currentUser);
          req.setAttribute("cart", openedCart);
          req.setAttribute("itemsCount", itemsCount);
          req.setAttribute("cartSum", cartSum);
          req.setAttribute("creationDate", new Date(openedCart.getCreationTime()));

          RequestDispatcher dispatcher = req.getRequestDispatcher("/jsp/cart-confirmed.jsp");
          dispatcher.forward(req, resp);
        } else {
          resp.sendRedirect(req.getContextPath() + "/items");
        }
      }

    } else if (action.equals("discard")) {
      if (currentUser != null) {
        Cart openedCart = CartService.findOpenCartByUser(currentUser.getId());
        CartService.deleteCart(openedCart);
      }

      HttpSession session = req.getSession();
      session.setAttribute("discartCompleted", true);

      resp.sendRedirect(req.getContextPath() + "/items");
    } else {
      super.doPost(req, resp);
    }

    if (jsonRespond != null) {
      PrintWriter respWriter = resp.getWriter();
      resp.setContentType("application/json");
      resp.setCharacterEncoding("UTF-8");

      respWriter.print(jsonRespond);
      respWriter.flush();
    }
  }

  private static String deleteOrderAndReturnCartSumInJson(Integer orderId) throws JsonProcessingException {
    String jsonResult = "{}";

    Order orderToDelete = OrderService.findById(orderId);
    Cart cart = CartService.findById(orderToDelete.getCartId());

    OrderService.deleteOrder(orderId);
    Integer cartSum = CartService.getCartSum(cart);
    Map<String, String> respondData = new HashMap<>();
    respondData.put("cartSum", cartSum.toString());

    ObjectMapper mapper = new ObjectMapper();
    jsonResult = mapper.writeValueAsString(respondData);

    return jsonResult;
  }
}
