package com.mainacad.dao;

import com.mainacad.model.Cart;
import com.mainacad.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CartDAOTest {
  private static List<Cart> carts = new ArrayList<>();
  private static List<User> users = new ArrayList<>();
  private static final Long NEW_CREATION_TIME = 1565024869119L;

  @BeforeEach
  void setUp() {
    User user = new User("test_user_login", "test_pass", "test_name", "test_surname");
    user = UserDAO.create(user);
    users.add(user);

    Cart cart = new Cart(1565024867119L, false, user.getId());
    cart = CartDAO.create(cart);
    carts.add(cart);
  }

  @AfterEach
  void tearDown() {
    for (Cart cart: carts) {
      CartDAO.delete(cart.getId());
    }
    carts.clear();

    for (User user: users) {
      UserDAO.delete(user.getId());
    }
    users.clear();
  }

  @Test
  void testCreate() {
    Cart cart = new Cart(1565024867219L, false, users.get(0).getId());
    cart = CartDAO.create(cart);
    carts.add(cart);

    assertNotNull(cart, "Creation method return null object");
    assertNotNull(cart.getId(), "Object id is null. Creation method must update id field of Cart object.");

    // checking findById
    Cart checkedCart = CartDAO.findById(cart.getId());
    assertNotNull(checkedCart, "Cart not found, but it should");
  }

  @Test
  void testFind() {
    Cart checkedCart = CartDAO.findById(carts.get(0).getId());
    assertNotNull(checkedCart, "Cart not found, but it should");
    assertEquals(carts.get(0).getId(), checkedCart.getId(), "Wrong cart found");
  }

  @Test
  void testFindOpenCartByUser() {
    Cart cart = carts.get(0);

    Cart checkedCart = CartDAO.findOpenCartByUser(cart.getUserId());
    assertNotNull(checkedCart, "findOpenCartByUser method return null object");

    assertEquals(cart.getUserId(), checkedCart.getUserId());
    assertEquals(cart.getClosed(), false);

    checkedCart.setClosed(true);
    CartDAO.update(checkedCart);

    Cart checkedCartAfterUpdate = CartDAO.findOpenCartByUser(cart.getUserId());
    if (checkedCartAfterUpdate != null) {
      assertNotEquals(checkedCart.getId(), checkedCartAfterUpdate.getId());
    }
  }

  @Test
  void testFindByUser() {
    Cart cart = carts.get(0);

    List<Cart> checkedCartList = CartDAO.findByUser(cart.getUserId());
    assertNotNull(checkedCartList, "findByUser method return null object");

    Cart cartForCheck = findCartInCollection(checkedCartList, cart);
    assertNotNull(cartForCheck, "Test cart not found, but it should");
  }

  @Test
  void testUpdate() {
    Cart cart = carts.get(0);

    cart.setCreationTime(NEW_CREATION_TIME);
    Cart checkedCart = CartDAO.update(cart);
    assertNotNull(cart, "Update method return null object");
    assertEquals(NEW_CREATION_TIME, checkedCart.getCreationTime());

    checkedCart = CartDAO.findById(checkedCart.getId());
    assertEquals(NEW_CREATION_TIME, checkedCart.getCreationTime());
  }

  @Test
  void testDelete() {
    Cart cart = carts.get(0);
    CartDAO.delete(cart.getId());

    Cart checkedCart = CartDAO.findById(cart.getId());
    assertNull(checkedCart, "Cart not deleted from DB");
  }

  private Cart findCartInCollection(List<Cart> list, Cart cart){
    Optional<Cart> optionalCart = list.stream()
            .filter(element -> element.getId().equals(cart.getId()))
            .findAny();

    return optionalCart.orElse(null);
  }
}