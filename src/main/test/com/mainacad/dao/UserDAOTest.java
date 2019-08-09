package com.mainacad.dao;

import com.mainacad.model.User;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserDAOTest {
  private static List<User> users = new ArrayList<>();
  private static String TEST_USER_LOGIN = "test_user";

  @BeforeEach
  void setUp() {
    User user = new User(TEST_USER_LOGIN, "test_pass", "test_name", "test_surname");
    user = UserDAO.create(user);
    users.add(user);
  }

  @AfterEach
  void tearDown() {
    for (User user: users) {
      if (user.getId() != null) {
        UserDAO.delete(user.getId());
      }
    }

    users.clear();
  }

  @Test
  void testCreate() {
    User user = new User(TEST_USER_LOGIN + "2", "test_pass", "test_name", "test_surname");

    // test create
    assertNull(user.getId());
    User createdUser = UserDAO.create(user);
    assertNotNull(createdUser);
    assertNotNull(createdUser.getId());

    users.add(createdUser);

    // test findById
    User checkedUser = UserDAO.findById(createdUser.getId());
    assertNotNull(checkedUser);
    assertNotNull(checkedUser.getId());
  }

  @Test
  void testFindById() {
    User checkedUser = UserDAO.findById(users.get(0).getId());
    assertNotNull(checkedUser);
    assertNotNull(checkedUser.getId());
    assertEquals(users.get(0).getId(), checkedUser.getId(), "Wrong user found");
  }

  @Test
  void testUpdate() {
    User checkedUser = users.get(0);
    checkedUser.setPassword("new_password");
    checkedUser = UserDAO.update(checkedUser);

    User checkedUserFromDB = UserDAO.findById(checkedUser.getId());
    assertNotNull(checkedUserFromDB);
    assertEquals("new_password", checkedUserFromDB.getPassword());
  }

  @Test
  void testFindUserByLogin() {
    User createdUser = users.get(0);
    User checkedUsers = UserDAO.findByLogin(TEST_USER_LOGIN);
    assertNotNull(checkedUsers);
    assertEquals(createdUser.getId(), checkedUsers.getId(), "User not found by login");
  }

  @Test
  void testFindAll() {
    List<User> checkedUsers = UserDAO.findAll();

    assertNotNull(checkedUsers);
    assertEquals(true, checkedUsers.size() > 0);
    for (User user: checkedUsers) {
      assertNotNull(user, "All_Users collection has null objects");
    }
  }

  @Test
  void testDelete() {
    User checkedUser = users.get(0);
    UserDAO.delete(checkedUser.getId());

    User deletedUser = UserDAO.findById(checkedUser.getId());
    assertNull(deletedUser, "User delete failed");
  }
}