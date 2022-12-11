package com.example.demo.controller;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {

    private final UserRepository userRepository = mock(UserRepository.class);
    private final OrderRepository orderRepository = mock(OrderRepository.class);
    private OrderController orderController;

    @Before
    public void setup() {
        this.orderController = new OrderController();
        TestUtils.injectObjects(this.orderController, "userRepository", this.userRepository);
        TestUtils.injectObjects(this.orderController, "orderRepository", this.orderRepository);
    }

    @Test
    public void testSubmit() {
        User dummyuser = createNewUser();
        List<Item> dummyItems = createItems();
        Cart cart = new Cart();
        cart.setItems(dummyItems);
        dummyuser.setCart(cart);
        when(this.userRepository.findByUsername(dummyuser.getUsername())).thenReturn(dummyuser);

        ResponseEntity<UserOrder> orderResponseEntity = this.orderController.submit(dummyuser.getUsername());
        assertNotNull(orderResponseEntity);
        assertEquals(200, orderResponseEntity.getStatusCodeValue());
        assertEquals(2, Objects.requireNonNull(orderResponseEntity.getBody()).getItems().size());
    }

    @Test
    public void testGetOrdersForUser() {
        UserOrder dummyUserOrder = createUserOrder();
        when(this.userRepository.findByUsername(dummyUserOrder.getUser().getUsername())).thenReturn(dummyUserOrder.getUser());
        when(this.orderRepository.findByUser(dummyUserOrder.getUser())).thenReturn(Lists.newArrayList(dummyUserOrder));
        ResponseEntity<List<UserOrder>> responseEntity = this.orderController.getOrdersForUser(dummyUserOrder.getUser().getUsername());
        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals(1, Objects.requireNonNull(responseEntity.getBody()).size());
    }

    private UserOrder createUserOrder() {
        UserOrder userOrder = new UserOrder();
        userOrder.setItems(createItems());
        userOrder.setUser(createNewUser());
        userOrder.setId(1L);
        userOrder.setTotal(new BigDecimal(200));
        return userOrder;
    }

    private User createNewUser() {
        User user = new User();
        user.setUsername("Mohamed");
        user.setId(1);
        user.setPassword("Mohamed");
        user.setCart(new Cart());
        return user;
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
}
