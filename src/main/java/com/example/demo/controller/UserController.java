package com.example.demo.controller;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/id/{id}")
    public ResponseEntity<User> findById(@PathVariable Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            logger.info("User : " + user.getUsername());
        } else
            logger.warn("No user found with id :" + id);
        return ResponseEntity.of(userOptional);
    }

    @GetMapping("/{username}")
    public ResponseEntity<User> findByUserName(@PathVariable String username) {
        User user = userRepository.findByUsername(username);
        if (user != null)
            logger.info("User:" + user.getUsername());
        else
            logger.warn("No user found with username: " + username);
        return user == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(user);
    }

    @PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestBody CreateUserRequest createUserRequest) {
        User user = new User();
        user.setUsername(createUserRequest.getUsername());
        Cart cart = new Cart();
        cartRepository.save(cart);
        user.setCart(cart);
        if (createUserRequest.getPassword().length() <= 7 ||
                !createUserRequest.getPassword().equals(createUserRequest.getConfirmPassword())) {
            logger.warn("Password doesn't match specified criteria");
            logger.warn("Confirm password doesn't match password");
            return ResponseEntity.badRequest().build();
        }
        user.setPassword(this.bCryptPasswordEncoder.encode(createUserRequest.getPassword()));
        userRepository.save(user);
        return ResponseEntity.ok(user);
    }

}
