package com.mainacad.dao;

import com.mainacad.model.Cart;
import com.mainacad.model.Item;
import com.mainacad.model.Order;
import com.mainacad.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class OrderDAOTest {
  private static List<Item> items = new ArrayList<>();
  private static List<Cart> carts = new ArrayList<>();
  private static List<User> users = new ArrayList<>();
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

    // create test order
    Order order = new Order(item.getId(), 2, cart.getId());
    order = OrderDAO.create(order);
    orders.add(order);
  }

  @AfterEach
  void tearDown() {
    for (Order order: orders) {
      OrderDAO.delete(order.getId());
    }
    orders.clear();

    for (Cart cart: carts) {
      CartDAO.delete(cart.getId());
    }
    carts.clear();

    for (Item item: items) {
      ItemDAO.delete(item.getId());
    }
    items.clear();

    for (User user: users) {
      UserDAO.delete(user.getId());
    }
    users.clear();
  }

  @Test
  void testCreate() {
    Order order = new Order(items.get(0).getId(), 3, carts.get(0).getId());
    order = OrderDAO.create(order);
    orders.add(order);

    assertNotNull(order, "Creation method return null object");
    assertNotNull(order.getId(), "Object id is null. Creation method must update id field of Order object.");
  }

  @Test
  void testFind() {
    // checking findById
    Order checkedOrder = OrderDAO.findById(orders.get(0).getId());
    assertNotNull(checkedOrder, "Order not found, but it should");
    assertEquals(orders.get(0).getId(), checkedOrder.getId(), "Wrong order found");
  }

  @Test
  void testUpdate() {
    Order order = orders.get(0);
    order.setAmount(3);
    Order checkedOrder = OrderDAO.update(order);
    assertNotNull(order, "Update method return null object");
    assertEquals(3, checkedOrder.getAmount());

    checkedOrder = OrderDAO.findById(checkedOrder.getId());
    assertEquals(3, checkedOrder.getAmount());
  }

  @Test
  void testFindByCart() {
    Cart cart = carts.get(0);
    Order order = orders.get(0);

    List<Order> checkedOrdersList = OrderDAO.findByCart(cart.getId());
    assertNotNull(checkedOrdersList, "findByUser method return null object");

    Optional<Order> optionalCheckedOrder = checkedOrdersList.stream()
            .peek(orderElement -> assertEquals(cart.getId(), orderElement.getCartId())) //check that found ordеr with correct cart
            .filter(element -> element.getId().equals(order.getId()))
            .findAny();

    assertNotNull(optionalCheckedOrder.orElse(null), "Test order not found, but it should");
  }

  @Test
  void testFindClosedOrdersByUserAndPeriod() {
    User user = users.get(0);
    Cart cart = carts.get(0);
    Order order = orders.get(0);

    Long from = cart.getCreationTime() - 1000;
    Long to = cart.getCreationTime() + 1000;

    List<Order> checkedOrdersList = OrderDAO.findClosedOrdersByUserIdAndPeriod(user.getId(), from, to);
    assertNotNull(checkedOrdersList, "findClosedOrdersByUserAndPeriod method return null object");

    Optional<Order> optionalCheckedOrder = checkedOrdersList.stream()
            .peek(orderElement -> {
              Cart checkedCart = CartDAO.findById(orderElement.getCartId());
              assertEquals(user.getId(), checkedCart.getUserId()); // check that found ordеr with correct user
              assertEquals(true, checkedCart.getClosed());// check that found closed ordеr
            })
            .filter(element -> element.getId().equals(order.getId()))
            .findAny();

    assertNull(optionalCheckedOrder.orElse(null), "Test order found, but it shouldn't"); // we haven't closed orders

    cart.setClosed(true);
    CartDAO.update(cart);

    checkedOrdersList = OrderDAO.findClosedOrdersByUserIdAndPeriod(user.getId(), from, to);

    optionalCheckedOrder = checkedOrdersList.stream()
            .peek(orderElement -> {
              Cart checkedCart = CartDAO.findById(orderElement.getCartId());
              assertEquals(user.getId(), checkedCart.getUserId()); // check that found ordеr with correct user
              assertEquals(true, checkedCart.getClosed());// check that found closed ordеr
            })
            .filter(element -> element.getId().equals(order.getId()))
            .findAny();
    assertNotNull(optionalCheckedOrder.orElse(null), "Test order not found, but it should"); // we haven`t closed orders

    from = cart.getCreationTime() + 500;

    checkedOrdersList = OrderDAO.findClosedOrdersByUserIdAndPeriod(user.getId(), from, to);

    optionalCheckedOrder = checkedOrdersList.stream()
            .peek(orderElement -> {
              Cart checkedCart = CartDAO.findById(orderElement.getCartId());
              assertEquals(user.getId(), checkedCart.getUserId()); // check that found ordеr with correct user
              assertEquals(true, checkedCart.getClosed());// check that found closed ordеr
            })
            .filter(element -> element.getId().equals(order.getId()))
            .findAny();

    assertNull(optionalCheckedOrder.orElse(null), "Test order found, but it shouldn't"); // we haven't closed orders within that period
  }

  @Test
  void testDelete() {
    Order order = orders.get(0);
    OrderDAO.delete(order.getId());

    Order checkedOrder = OrderDAO.findById(order.getId());
    assertNull(checkedOrder, "Order not deleted from DB");
  }

  @Test
  void testGetSumOfAllOrdersByUserIdAndPeriod() {
    User user = users.get(0);
    Cart cart = carts.get(0);
    Order order = orders.get(0);

    Long from = cart.getCreationTime() - 1000;
    Long to = cart.getCreationTime() + 1000;

    Integer checkedSum = OrderDAO.getSumOfAllOrdersByUserIdAndPeriod(user.getId(), from, to);
    assertNotNull(checkedSum);
    assertEquals(0, checkedSum);

    cart.setClosed(true);
    cart = CartDAO.update(cart);

    checkedSum = OrderDAO.getSumOfAllOrdersByUserIdAndPeriod(user.getId(), from, to);
    assertEquals(40000, checkedSum);

    Order newOrder = new Order(order.getItemId(), 3, order.getCartId());
    newOrder = OrderDAO.create(newOrder);
    orders.add(newOrder);

    checkedSum = OrderDAO.getSumOfAllOrdersByUserIdAndPeriod(user.getId(), from, to);
    assertNotNull(checkedSum);
    assertEquals(100000, checkedSum);
  }
}