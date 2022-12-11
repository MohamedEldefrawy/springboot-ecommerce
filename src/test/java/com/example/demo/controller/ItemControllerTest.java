package com.example.demo.controller;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.mockito.Mockito.mock;

public class ItemControllerTest {
    private ItemController itemController;
    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setup() {
        this.itemController = new ItemController();
        TestUtils.injectObjects(this.itemController, "itemRepository", this.itemRepository);
    }

    @Test
    public void testGetAllItems() {
        ResponseEntity<List<Item>> itemsResponseEntity = this.itemController.getItems();
        Assertions.assertNotNull(itemsResponseEntity);
        List<Item> items = itemsResponseEntity.getBody();
        Assertions.assertNotNull(items);
        Assertions.assertEquals(items.size(), 0);
    }
}
