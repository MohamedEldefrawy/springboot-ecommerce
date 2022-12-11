package com.example.demo.controller;

import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/item")
public class ItemController {

    @Autowired
    private ItemRepository itemRepository;
    private final Logger logger = LoggerFactory.getLogger(ItemController.class);

    @GetMapping
    public ResponseEntity<List<Item>> getItems() {
        List<Item> items = itemRepository.findAll();
        if (items.size() == 0)
            logger.warn("No Items found");
        return ResponseEntity.ok(itemRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Item> getItemById(@PathVariable Long id) {
        Optional<Item> itemOptional = itemRepository.findById(id);
        if (!itemOptional.isPresent())
            logger.warn("No item found with id:" + itemOptional);
        return ResponseEntity.of(itemOptional);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<List<Item>> getItemsByName(@PathVariable String name) {
        List<Item> items = itemRepository.findByName(name);
        if (items == null || items.isEmpty()) {
            logger.warn("No Item found with name: " + name);
            return ResponseEntity.notFound().build();
        } else
            return ResponseEntity.ok(items);
    }

}
