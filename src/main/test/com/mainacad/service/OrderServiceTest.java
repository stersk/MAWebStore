package com.mainacad.service;

import com.mainacad.dao.CartDAO;
import com.mainacad.dao.ItemDAO;
import com.mainacad.dao.OrderDAO;
import com.mainacad.dao.UserDAO;
import com.mainacad.model.Cart;
import com.mainacad.model.Item;
import com.mainacad.model.Order;
import com.mainacad.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class OrderServiceTest {
  private static List<User> users = new ArrayList<>();
  private static List<Item> items = new ArrayList<>();
  private static List<Cart> carts = new ArrayList<>();
  private static List<Order> orders = new ArrayList<>();


  @BeforeEach
  void setUp() {
    // crete test item
    Item item = new Item("test_item", "Test item", 20000);
    item = ItemDAO.create(item);
    items.add(item);

    // create test user
    User user = new User("user_login", "test_pass", "test_name", "test_surname");
    user = UserDAO.create(user);
    users.add(user);

    // create test cart
    Cart cart = new Cart(1565024867119L, false, user.getId());
    cart = CartDAO.create(cart);
    carts.add(cart);
  }

  @AfterEach
  void tearDown() {
    orders.forEach(order -> OrderDAO.delete(order.getId()));
    orders.clear();

    carts.forEach(cart -> CartDAO.delete(cart.getId()));
    carts.clear();

    items.forEach(item -> ItemDAO.delete(item.getId()));
    items.clear();

    users.forEach(user -> UserDAO.delete(user.getId()));
    users.clear();
  }

  @Test
  void testCreateOrderByItemAndUser() {
    Order checkedOrder = OrderService.createOrderByItemAndUser(items.get(0), 2, users.get(0));
    orders.add(checkedOrder);

    assertNotNull(checkedOrder);
    assertNotNull(checkedOrder.getId());

    assertEquals(items.get(0).getId(), checkedOrder.getItemId());
    assertEquals(2, checkedOrder.getAmount());

    Cart checkedCart = CartDAO.findById(checkedOrder.getCartId());
    assertNotNull(checkedCart);
    assertEquals(carts.get(0).getId(), checkedCart.getId());
    assertEquals(users.get(0).getId(), checkedCart.getUserId());
    assertEquals(false, checkedCart.getClosed());
  }

  @Test
  void testAddItemToOrder() {
    Order createdOrder = OrderService.addItemToOrder(items.get(0),users.get(0));
    orders.add(createdOrder);

    assertNotNull(createdOrder);

    Order checkedOrder = OrderService.findById(createdOrder.getId());
    assertEquals(1, checkedOrder.getAmount());

    createdOrder = OrderService.addItemToOrder(items.get(0),users.get(0));

    checkedOrder = OrderService.findById(createdOrder.getId());
    assertEquals(2, checkedOrder.getAmount());
  }
}