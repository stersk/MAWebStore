package com.mainacad.dao;

import com.mainacad.model.Item;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ItemDAOTest {

  private static List<Item> items = new ArrayList<>();
  private static String TEST_ITEM_CODE = "qwerty12345";
  private static Integer TEST_NEW_PRICE = 1450000;

  @BeforeEach
  void setUp() {
    Item item = new Item(TEST_ITEM_CODE, "Kellys Spider 40 (2014)", 1400000);
    item = ItemDAO.create(item);
    items.add(item);
  }

  @AfterEach
  void tearDown() {
    for (Item item: items) {
      if (item.getId() != null) {
        ItemDAO.delete(item.getId());
      }
    }

    items.clear();
  }

  @Test
  void testCreate() {
    Item item = new Item(TEST_ITEM_CODE, "Kellys Spider 40 (2014)", 1400000);
    assertNull(item.getId());
    Item createdItem = ItemDAO.create(item);
    items.add(createdItem);
    assertNotNull(createdItem);
    assertNotNull(createdItem.getId());

    // checking findById
    Item checkedItem = ItemDAO.findById(createdItem.getId());
    assertNotNull(checkedItem);
    assertNotNull(checkedItem.getId());
  }

  @Test
  void testFind() {
    Item checkedItem = ItemDAO.findById(items.get(0).getId());
    assertNotNull(checkedItem);
    assertNotNull(checkedItem.getId());
    assertEquals(items.get(0).getId(), checkedItem.getId(), "Wrong item found");
  }

  @Test
  void testUpdate() {
    Item checkedItem = items.get(0);

    checkedItem.setPrice(TEST_NEW_PRICE);
    checkedItem = ItemDAO.update(checkedItem);

    assertNotNull(checkedItem);
    assertEquals(TEST_NEW_PRICE, checkedItem.getPrice());

    Item checkedItemFromDB = ItemDAO.findById(checkedItem.getId());
    assertNotNull(checkedItemFromDB);
    assertEquals(TEST_NEW_PRICE, checkedItemFromDB.getPrice());
  }

  @Test
  void testFindByItemCode() {
    Item createdItem = items.get(0);

    List<Item> checkedItems = ItemDAO.findByItemCode(TEST_ITEM_CODE);
    assertNotNull(checkedItems);

    Item checkedItem = findItemInCollection(checkedItems, createdItem);
    assertNotNull(checkedItem, "Item not found by code");
  }

  @Test
  void testFindItemByPriceBetween() {
    Item createdItem = items.get(0);

    List<Item> checkedItems = ItemDAO.findByItemPriceBetween(createdItem.getPrice() - 100, createdItem.getPrice() + 100);
    assertNotNull(checkedItems);

    Item checkedItem = findItemInCollection(checkedItems, createdItem);
    assertNotNull(checkedItem, "Item not found by price, but should");

    checkedItems = ItemDAO.findByItemPriceBetween(createdItem.getPrice() + 100, createdItem.getPrice() + 200);
    assertNotNull(checkedItem);

    checkedItem = findItemInCollection(checkedItems, createdItem);
    assertNull(checkedItem, "Item found by price, but should not");
  }

  @Test
  void testFindAll() {
    List<Item> checkedItems = ItemDAO.findAll();

    assertNotNull(checkedItems);
    assertEquals(true, checkedItems.size() > 0);
    for (com.mainacad.model.Item Item: checkedItems) {
      assertNotNull(Item, "All_Items collection has null objects");
    }
  }

  @Test
  void testDelete() {
    Item checkedItem = items.get(0);
    ItemDAO.delete(checkedItem.getId());

    Item deletedItem = ItemDAO.findById(checkedItem.getId());
    assertNull(deletedItem);
  }

  private Item findItemInCollection(List<Item> list, Item item){
    Optional<Item> optionalItem = list.stream()
                          .filter(element -> element.getId().equals(item.getId()))
                          .findAny();

    return optionalItem.orElse(null);
  }
}