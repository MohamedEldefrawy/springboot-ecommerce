package com.example.demo.controller;

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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {
    private final UserRepository userRepository = mock(UserRepository.class);
    private final CartRepository cartRepository = mock(CartRepository.class);
    private final ItemRepository itemRepository = mock(ItemRepository.class);

    private CartController cartController;

    @Before
    public void setup() {
        this.cartController = new CartController();
        TestUtils.injectObjects(this.cartController, "userRepository", this.userRepository);
        TestUtils.injectObjects(this.cartController, "cartRepository", this.cartRepository);
        TestUtils.injectObjects(this.cartController, "itemRepository", this.itemRepository);
    }

    @Test
    public void addToCartTest() {
        User dummyUser = createNewUser();
        Item dummyItem = createItem();
        when(this.itemRepository.findById(1L)).thenReturn(Optional.of(dummyItem));
        when(this.userRepository.findById(1L)).thenReturn(Optional.of(dummyUser));
        when(this.userRepository.findByUsername(dummyUser.getUsername())).thenReturn(dummyUser);
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername(dummyUser.getUsername());
        modifyCartRequest.setItemId(dummyItem.getId());
        modifyCartRequest.setQuantity(1);

        ResponseEntity<Cart> responseEntity = this.cartController.addTocart(modifyCartRequest);
        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    public void removeFromCartTest() {
        User dummyUser = createNewUser();
        Item dummyItem = createItem();
        when(this.itemRepository.findById(1L)).thenReturn(Optional.of(dummyItem));
        when(this.userRepository.findById(1L)).thenReturn(Optional.of(dummyUser));
        when(this.userRepository.findByUsername(dummyUser.getUsername())).thenReturn(dummyUser);
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername(dummyUser.getUsername());
        modifyCartRequest.setItemId(dummyItem.getId());
        modifyCartRequest.setQuantity(1);
        ResponseEntity<Cart> responseEntity = this.cartController.removeFromcart(modifyCartRequest);
        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());

    }

    private Item createItem() {
        Item item1 = new Item();
        item1.setId(1L);
        item1.setName("Hola");
        item1.setDescription("hola description");
        item1.setPrice(new BigDecimal(200));

        return item1;
    }

    private User createNewUser() {
        User user = new User();
        user.setUsername("Mohamed");
        user.setId(1L);
        user.setPassword("Mohamed");
        user.setCart(new Cart());
        return user;
    }
}
