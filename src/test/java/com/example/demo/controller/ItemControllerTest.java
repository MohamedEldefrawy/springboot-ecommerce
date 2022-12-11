package com.example.demo.controller;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {
    private ItemController itemController;
    private final ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setup() {
        this.itemController = new ItemController();
        TestUtils.injectObjects(this.itemController, "itemRepository", this.itemRepository);
    }

    @Test
    public void testGetAllItems() {
        List<Item> dummyItems = createItems();
        when(this.itemRepository.findAll()).thenReturn(dummyItems);
        ResponseEntity<List<Item>> itemsResponseEntity = this.itemController.getItems();
        assertNotNull(itemsResponseEntity);
        List<Item> items = itemsResponseEntity.getBody();
        assertNotNull(items);
        Assertions.assertEquals(items.size(), 2);
    }

    @Test
    public void testGetItemById() {
        Item dummyItem = createItem();
        when(this.itemRepository.findById(1L)).thenReturn(Optional.of(dummyItem));
        ResponseEntity<Item> itemResponseEntity = this.itemController.getItemById(dummyItem.getId());
        assertNotNull(itemResponseEntity);
        assertEquals(200, itemResponseEntity.getStatusCodeValue());
        Item selectedItem = itemResponseEntity.getBody();
        assertEquals(dummyItem.getName(), selectedItem.getName());
        assertEquals(dummyItem.getDescription(), selectedItem.getDescription());
        assertEquals(dummyItem.getPrice(), selectedItem.getPrice());
        assertEquals(dummyItem.getId(), selectedItem.getId());
    }

    @Test
    public void testGetItemByName() {
        List<Item> dummyItems = createItems();
        when(this.itemRepository.findByName(dummyItems.get(0).getName())).thenReturn(dummyItems);
        ResponseEntity<List<Item>> itemResponseEntity = this.itemController.getItemsByName(dummyItems.get(0).getName());
        assertNotNull(itemResponseEntity);
        assertEquals(200, itemResponseEntity.getStatusCodeValue());
        List<Item> selectedItems = itemResponseEntity.getBody();
        assertNotNull(selectedItems);
        assertEquals(2, selectedItems.size());
    }

    private List<Item> createItems() {
        Item item1 = new Item();
        item1.setId(1L);
        item1.setName("Hola");
        item1.setDescription("hola description");
        item1.setPrice(new BigDecimal(200));


        Item item2 = new Item();
        item2.setId(2L);
        item2.setName("Hola");
        item2.setDescription("hola2 description");
        item2.setPrice(new BigDecimal(200));

        return Lists.newArrayList(item1, item2);
    }

    private Item createItem() {
        Item item1 = new Item();
        item1.setId(1L);
        item1.setName("Hola");
        item1.setDescription("hola description");
        item1.setPrice(new BigDecimal(200));

        return item1;
    }
}
