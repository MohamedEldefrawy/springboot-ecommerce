package com.example.demo.controller;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {
    private UserController userController;
    private final UserRepository userRepository = mock(UserRepository.class);
    private final CartRepository cartRepository = mock(CartRepository.class);
    private final BCryptPasswordEncoder bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setup() {
        this.userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", this.userRepository);
        TestUtils.injectObjects(userController, "cartRepository", this.cartRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", this.bCryptPasswordEncoder);

    }

    @Test
    public void testCreateUserHappyPath() {
        when(bCryptPasswordEncoder.encode("testuser")).thenReturn("hashedPassword");
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("testuser");
        createUserRequest.setPassword("testuser");
        createUserRequest.setConfirmPassword("testuser");

        ResponseEntity<User> userResponseEntity = this.userController.createUser(createUserRequest);
        assertNotNull(userResponseEntity);
        assertEquals(200, userResponseEntity.getStatusCodeValue());

        User user = userResponseEntity.getBody();
        assertNotNull(user);
        assertEquals("testuser", user.getUsername());
        assertEquals("hashedPassword", user.getPassword());
    }

    @Test
    public void testFindUserById() {
        User dummyUser = createNewUser();
        when(this.userRepository.findById(1L)).thenReturn(Optional.of(dummyUser));
        ResponseEntity<User> userResponseEntity = this.userController.findById(1L);
        assertEquals(200, userResponseEntity.getStatusCodeValue());

        User selectedUser = userResponseEntity.getBody();
        assertNotNull(selectedUser);
        assertEquals(1, selectedUser.getId());
        assertEquals("Mohamed", selectedUser.getUsername());
        assertEquals("Mohamed", selectedUser.getPassword());
    }

    @Test
    public void testFindUserByName() {
        User dummyUser = createNewUser();
        when(this.userRepository.findByUsername(dummyUser.getUsername()))
                .thenReturn(dummyUser);
        ResponseEntity<User> userResponseEntity = this.userController.findByUserName(dummyUser.getUsername());
        assertNotNull(userResponseEntity);
        assertEquals(200, userResponseEntity.getStatusCodeValue());
        User selectedUser = userResponseEntity.getBody();
        assertNotNull(selectedUser);
        assertEquals(dummyUser.getUsername(), selectedUser.getUsername());
        assertEquals(dummyUser.getId(), selectedUser.getId());
        assertEquals(dummyUser.getPassword(), selectedUser.getPassword());
    }

    private User createNewUser() {
        User user = new User();
        user.setUsername("Mohamed");
        user.setId(1);
        user.setPassword("Mohamed");
        user.setCart(new Cart());
        return user;
    }


}
