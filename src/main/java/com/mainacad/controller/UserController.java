package com.mainacad.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mainacad.dao.UserDAO;
import com.mainacad.model.User;
import com.mainacad.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class UserController extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    req.setCharacterEncoding("UTF-8");
    resp.setCharacterEncoding("UTF-8");

    String action = req.getParameter("action");

    if (action.equals("logout")) {
      HttpSession session = req.getSession();
      session.setAttribute("user", null);

      resp.sendRedirect(req.getContextPath() + "/");
    } else if (action.equals("userExist")) {
      User user = UserService.findByLogin(req.getParameter("login"));

      Map<String, Boolean> map = new HashMap<>();
      map.put("userExist", user != null);

      ObjectMapper mapper = new ObjectMapper();
      String jsonResult = mapper.writerWithDefaultPrettyPrinter()
              .writeValueAsString(map);

      PrintWriter respWriter = resp.getWriter();
      resp.setContentType("application/json");
      resp.setCharacterEncoding("UTF-8");

      respWriter.print(jsonResult);
      respWriter.flush();

    } else {
      super.doGet(req, resp);
    }
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    req.setCharacterEncoding("UTF-8");
    resp.setCharacterEncoding("UTF-8");

    String action = req.getParameter("action");

    HttpSession session = req.getSession();
    if (action.equals("login")) {

      String login = req.getParameter("login");
      String password = req.getParameter("password");

      User user = UserService.findByLoginAndPassword(login, password);
      if (user != null) {
        session.setAttribute("user", user);
        session.setAttribute("wrongAuth", false);

        resp.sendRedirect(req.getContextPath() + "/items");
      } else {
        session.setAttribute("wrongAuth", true);

        resp.sendRedirect(req.getContextPath() + "/");
      }
    } else if (action.equals("register")) {
      String login = req.getParameter("login");
      String password = req.getParameter("password");
      String firstName = req.getParameter("fname");
      String lastName = req.getParameter("lname");
      User user = new User(login, password, firstName, lastName);

      User savedUser = UserDAO.create(user);

      if (savedUser != null) {
        session.setAttribute("user", user);

        req.setAttribute("userCreated", true);
        resp.sendRedirect(req.getContextPath() + "/items");
      }
    } else if (action.equals("logout")) {
      session.setAttribute("user", null);

      resp.sendRedirect(req.getContextPath() + "/");
    }
  }
}
