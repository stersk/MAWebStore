package com.mainacad.controller;

import com.mainacad.model.Item;
import com.mainacad.service.ItemService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class OrderController extends HttpServlet{
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    req.setCharacterEncoding("UTF-8");
    resp.setCharacterEncoding("UTF-8");

    List<Item> items = ItemService.getAllItems();
    req.setAttribute("items", items);

    RequestDispatcher dispatcher = req.getRequestDispatcher("/jsp/items.jsp");
    dispatcher.forward(req, resp);
  }
}
