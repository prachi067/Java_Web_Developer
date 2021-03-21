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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {

    private CartController cartController;

    private UserRepository userRepository = mock(UserRepository.class);

    private CartRepository cartRepository = mock(CartRepository.class);

    private ItemRepository itemRepository = mock(ItemRepository.class);


    @Before
    public void setUp() {
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "userRepository", userRepository);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);

        Item item = new Item();
        item.setId(1L);
        item.setName("testItem");
        item.setPrice(BigDecimal.valueOf(10.11));
        item.setDescription("testDescription");

        User user = new User();
        user.setId(1);
        user.setUsername("test");
        user.setPassword("testPassword");

        Cart cart = new Cart();
        user.setCart(cart);

        when(userRepository.findByUsername("test")).thenReturn(user);
        when(itemRepository.findById(1L)).thenReturn(java.util.Optional.of(item));
    }

    @Test
    public void add_to_cart_happy_path() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(1L);
        modifyCartRequest.setQuantity(2);
        modifyCartRequest.setUsername("test");

        final ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Cart cart = response.getBody();
        assertNotNull(cart);
        assertEquals(2, cart.getItems().size());
        assertEquals("testItem", cart.getItems().get(0).getName());
        assertEquals(BigDecimal.valueOf(10.11).multiply(BigDecimal.valueOf(2)), cart.getTotal());
    }


    @Test
    public void remove_from_cart_happy_path() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(1L);
        modifyCartRequest.setQuantity(2);
        modifyCartRequest.setUsername("test");

        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(1L);
        modifyCartRequest.setQuantity(1);
        modifyCartRequest.setUsername("test");

        response = cartController.removeFromcart(modifyCartRequest);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Cart cart = response.getBody();
        assertNotNull(cart);
        assertEquals(1, cart.getItems().size());
        assertEquals("testItem", cart.getItems().get(0).getName());
        assertEquals(BigDecimal.valueOf(10.11), cart.getTotal());

    }
}
