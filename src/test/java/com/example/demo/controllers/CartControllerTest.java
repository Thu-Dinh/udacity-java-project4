package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {

    private CartController cartController;

    private UserRepository userRepository = mock(UserRepository.class);

    private CartRepository cartRepository = mock(CartRepository.class);

    private ItemRepository itemRepository = mock(ItemRepository.class);

    private ModifyCartRequest request;

    @Before
    public void setUp() {
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "userRepository", userRepository);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);

        request = new ModifyCartRequest();
        request.setUsername("aaa");
        request.setItemId(1l);
        request.setQuantity(12);
    }

    @Test
    public void addTocart_success() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setUsername("aaa");
        Cart cart = new Cart();
        cart.setId(1l);
        Item item = new Item();
        item.setId(1l);
        item.setName("item1");
        item.setPrice(new BigDecimal(20));
        List<Item> itemList = new ArrayList<>();
        itemList.add(item);
        cart.setItems(itemList);
        cart.setTotal(new BigDecimal(15));
        user.setCart(cart);
        when(userRepository.findByUsername("aaa")).thenReturn(user);

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        ResponseEntity<Cart> res = cartController.addTocart(request);
        assertNotNull(res);
        assertEquals(200, res.getStatusCodeValue());
        assertEquals(Optional.of(1l).get(), res.getBody().getId());
    }

    @Test
    public void addTocart_NotFoundUser() throws Exception {
        when(userRepository.findByUsername("aaa")).thenReturn(null);
        final ResponseEntity<Cart> res = cartController.addTocart(request);
        assertNotNull(res);
        assertEquals(404, res.getStatusCodeValue());
    }

    @Test
    public void addTocart_NotFoundItem() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setUsername("aaa");
        Cart cart = new Cart();
        cart.setId(1l);
        Item item = new Item();
        item.setId(1l);
        item.setName("item1");
        cart.setItems(Arrays.asList(item));
        user.setCart(cart);
        when(userRepository.findByUsername("aaa")).thenReturn(user);

        when(itemRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Cart> res = cartController.addTocart(request);
        assertNotNull(res);
        assertEquals(404, res.getStatusCodeValue());
    }

    @Test
    public void removeFromcart_success() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setUsername("aaa");
        Cart cart = new Cart();
        cart.setId(1l);
        Item item = new Item();
        item.setId(1l);
        item.setName("item1");
        item.setPrice(new BigDecimal(20));
        List<Item> itemList = new ArrayList<>();
        itemList.add(item);
        cart.setItems(itemList);
        cart.setTotal(new BigDecimal(15));
        user.setCart(cart);
        when(userRepository.findByUsername("aaa")).thenReturn(user);

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        ResponseEntity<Cart> res = cartController.removeFromcart(request);
        assertNotNull(res);
        assertEquals(200, res.getStatusCodeValue());
        assertEquals(Optional.of(1l).get(), res.getBody().getId());
    }

    @Test
    public void removeFromcart_NotFoundUser() throws Exception {
        when(userRepository.findByUsername("aaa")).thenReturn(null);
        final ResponseEntity<Cart> res = cartController.removeFromcart(request);
        assertNotNull(res);
        assertEquals(404, res.getStatusCodeValue());
    }

    @Test
    public void removeFromcart_NotFoundItem() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setUsername("aaa");
        Cart cart = new Cart();
        cart.setId(1l);
        Item item = new Item();
        item.setId(1l);
        item.setName("item1");
        cart.setItems(Arrays.asList(item));
        user.setCart(cart);
        when(userRepository.findByUsername("aaa")).thenReturn(user);

        when(itemRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Cart> res = cartController.removeFromcart(request);
        assertNotNull(res);
        assertEquals(404, res.getStatusCodeValue());
    }

}
