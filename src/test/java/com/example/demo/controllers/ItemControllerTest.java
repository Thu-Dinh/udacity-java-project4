package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
    }

    @Test
    public void getItemsTest() throws Exception {
        Item item = new Item();
        item.setId(1L);
        when(itemRepository.findAll()).thenReturn(Arrays.asList(item));

        ResponseEntity<List<Item>> res = itemController.getItems();
        assertNotNull(res);
        assertEquals(200, res.getStatusCodeValue());
        assertEquals(1, res.getBody().size());

    }

    @Test
    public void getItemByIdTest() throws Exception {
        Item item = new Item();
        item.setId(1L);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        ResponseEntity<Item> res = itemController.getItemById(1L);
        assertNotNull(res);
        assertEquals(200, res.getStatusCodeValue());
        Item itemR = res.getBody();
        assertEquals(Optional.of(1L).get(), itemR.getId());
    }

    @Test
    public void getItemsByName_success() throws Exception {
        Item item = new Item();
        item.setId(1L);
        item.setName("itemTest");
        when(itemRepository.findByName("itemTest")).thenReturn(Arrays.asList(item));

        ResponseEntity<List<Item>> res = itemController.getItemsByName("itemTest");
        assertNotNull(res);
        assertEquals(200, res.getStatusCodeValue());
        assertEquals(1, res.getBody().size());

    }

    @Test
    public void getItemsByName_notfound() throws Exception {
        when(itemRepository.findByName("itemTest")).thenReturn(new ArrayList<>());
        ResponseEntity<List<Item>> res = itemController.getItemsByName("itemTest");
        assertNotNull(res);
        assertEquals(404, res.getStatusCodeValue());
    }
}
