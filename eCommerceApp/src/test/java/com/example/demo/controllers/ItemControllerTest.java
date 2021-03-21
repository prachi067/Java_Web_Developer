package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {

    private ItemController itemController;

    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp() {
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);

        Item item = new Item();
        item.setId(1L);
        item.setName("testItem");
        item.setPrice(BigDecimal.valueOf(10.11));
        item.setDescription("testDescription");
        List<Item> items = new ArrayList<Item>();
        items.add(item);

        when(itemRepository.findAll()).thenReturn(items);
        when(itemRepository.findById(1L)).thenReturn(java.util.Optional.of(item));
        when(itemRepository.findByName("testItem")).thenReturn(Collections.singletonList(item));

    }

    @Test
    public void get_Items_happy_path() {
        final ResponseEntity<List<Item>> response = itemController.getItems();
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        List<Item> items = response.getBody();
        assertNotNull(items);
        assertEquals(1, items.size());
    }

    @Test
    public void get_Items_By_id_happy_path() {
        final ResponseEntity<Item> response = itemController.getItemById(1L);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Item item = response.getBody();
        assertNotNull(item);
        assertEquals("testItem",item.getName());
    }

    @Test
    public void get_Items_By_Name_happy_path() {
        final ResponseEntity<List<Item>> response = itemController.getItemsByName("testItem");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        List<Item> items = response.getBody();
        assertNotNull(items);
        assertEquals(1, items.size());
    }
}
