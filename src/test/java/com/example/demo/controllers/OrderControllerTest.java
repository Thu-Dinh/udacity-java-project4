package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {

    private OrderController orderController;

    private UserRepository userRepository = mock(UserRepository.class);

    private OrderRepository orderRepository = mock(OrderRepository.class);

    @Before
    public void setUp() {
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "userRepository", userRepository);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);
    }

    @Test
    public void getOrdersForUser_Success() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setUsername("aaa");
        when(userRepository.findByUsername("aaa")).thenReturn(user);

        List<UserOrder> userOrderList = new ArrayList<>();
        UserOrder userOrder = new UserOrder();
        userOrder.setUser(user);

        Item item = new Item();
        item.setId(1l);
        userOrder.setItems(Arrays.asList(item));
        userOrderList.add(userOrder);
        when(orderRepository.findByUser(user)).thenReturn(userOrderList);

        final ResponseEntity<List<UserOrder>> res = orderController.getOrdersForUser("aaa");
        assertNotNull(res);
        assertEquals(200, res.getStatusCodeValue());

        List<UserOrder> list = res.getBody();
        assertEquals(1, list.size());

    }

    @Test
    public void getOrdersForUser_NotFound() throws Exception {
        when(userRepository.findByUsername("aaa")).thenReturn(null);
        final ResponseEntity<List<UserOrder>> res = orderController.getOrdersForUser("aaa");
        assertNotNull(res);
        assertEquals(404, res.getStatusCodeValue());
    }

    @Test
    public void submit_success() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setUsername("aaa");

        Cart cart = new Cart();
        cart.setId(1L);
        Item item = new Item();
        item.setId(1l);
        cart.setItems(Arrays.asList(item));
        user.setCart(cart);
        when(userRepository.findByUsername("aaa")).thenReturn(user);

        final ResponseEntity<UserOrder> res = orderController.submit("aaa");
        assertNotNull(res);
        assertEquals(200, res.getStatusCodeValue());

        UserOrder userOrder = res.getBody();
        assertEquals(1, userOrder.getItems().size());

    }

    @Test
    public void submit_notfound() throws Exception {
        when(userRepository.findByUsername("aaa")).thenReturn(null);
        final ResponseEntity<UserOrder> res = orderController.submit("aaa");
        assertNotNull(res);
        assertEquals(404, res.getStatusCodeValue());
    }
}
