package com.mainacad.service;

import com.mainacad.dao.ItemDAO;
import com.mainacad.model.Item;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ItemServiceTest {
  private static List<Item> items = new ArrayList<>();

  @BeforeEach
  void setUp() {
    // crete test item
    Item item = new Item("test_item", "Test item", 20000);
    item = ItemDAO.create(item);
    items.add(item);
  }

  @AfterEach
  void tearDown() {
    items.forEach(item -> ItemDAO.delete(item.getId()));
    items.clear();
  }

  @Test
  void testSetItemPrice() {
    ItemService.setItemPrice(items.get(0), 20001);
    Item item = ItemDAO.findById(items.get(0).getId());

    assertNotNull(item);
    assertEquals(20001, item.getPrice());
  }
}