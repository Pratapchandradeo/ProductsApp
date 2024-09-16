package com.masai.ProductsApp.controller;

import com.masai.ProductsApp.model.Item;
import com.masai.ProductsApp.service.InventoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/products")
public class InventoryController {

    private final InventoryService productService;

    public InventoryController(InventoryService productService) {
        this.productService = productService;
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Item>> getProductsByCategory(@PathVariable String category) {
        List<Item> products = productService.getProductsByCategory(category);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addProduct(@RequestBody Item product) {
        Item createdProduct = productService.addProduct(product);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }
}
