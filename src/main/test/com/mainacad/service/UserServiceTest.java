package com.mainacad.service;

import com.mainacad.dao.UserDAO;
import com.mainacad.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {
  private static List<User> users = new ArrayList<>();
  private static final String USER_LOGIN = "user_login";
  private static final String USER_PASSWORD = "test_pass";

  @BeforeEach
  void setUp() {
    User user = new User(USER_LOGIN, USER_PASSWORD, "test_name", "test_surname");
    user = UserDAO.create(user);
    users.add(user);
  }

  @AfterEach
  void tearDown() {
    users.forEach(user -> UserDAO.delete(user.getId()));
    users.clear();
  }

  @Test
  void getAuthUser() {
    User checkedUser = UserService.findByLoginAndPassword(USER_LOGIN, USER_PASSWORD);
    assertNotNull(checkedUser);
    assertEquals(users.get(0).getId(), checkedUser.getId());

    checkedUser = UserService.findByLoginAndPassword(USER_LOGIN + "_", USER_PASSWORD);
    if (checkedUser != null) {
      assertNotEquals(users.get(0).getId(), checkedUser.getId());
    }

    checkedUser = UserService.findByLoginAndPassword(USER_LOGIN, USER_PASSWORD + "_");
    if (checkedUser != null) {
      assertNotEquals(users.get(0).getId(), checkedUser.getId());
    }
  }
}