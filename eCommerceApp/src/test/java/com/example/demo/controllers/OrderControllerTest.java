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

import java.math.BigDecimal;
import java.util.ArrayList;
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

        Item item = new Item();
        item.setId(1L);
        item.setName("testItem");
        item.setPrice(BigDecimal.valueOf(10.11));
        item.setDescription("testDescription");
        List<Item> items = new ArrayList<Item>();
        items.add(item);

        User user = new User();
        user.setId(1);
        user.setUsername("test");
        user.setPassword("testPassword");

        Cart cart = new Cart();
        cart.setId(1L);
        cart.setUser(user);
        cart.setItems(items);
        cart.setTotal(BigDecimal.valueOf(10.11));

        user.setCart(cart);
        when(userRepository.findByUsername("test")).thenReturn(user);
    }

    @Test
    public void submit_order_happy_path() {
        final ResponseEntity<UserOrder> response = orderController.submit("test");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        UserOrder userOrder = response.getBody();
        assertNotNull(userOrder);
        assertEquals(BigDecimal.valueOf(10.11),userOrder.getTotal());
        assertEquals(1, userOrder.getItems().size());
    }

    @Test
    public void submit_order_user_not_found() {
        ResponseEntity<UserOrder> response = orderController.submit("testUser");
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

}
