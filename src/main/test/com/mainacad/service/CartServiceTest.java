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

class CartServiceTest {
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

    Order order = new Order(item.getId(), 3, cart.getId());
    order = OrderDAO.create(order);
    orders.add(order);
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
  void testCreateCartForUser() {
    Cart checkedCart = CartService.createCartForUser(users.get(0).getId());
    carts.add(checkedCart);

    assertNotNull(checkedCart);
    assertEquals(false, checkedCart.getClosed());
    assertEquals(users.get(0).getId(), checkedCart.getUserId());
  }

  @Test
  void testGetCartSum() {
    Integer checkedSum = CartService.getCartSum(carts.get(0));
    assertNotNull(checkedSum);
    assertEquals(60000, checkedSum);
  }
}