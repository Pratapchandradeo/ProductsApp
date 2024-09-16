package com.masai.ProductsApp.service;

import com.masai.ProductsApp.model.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class InventoryService {

    private static final Logger logger = LoggerFactory.getLogger(InventoryService.class);
    private final RestTemplate restTemplate;

    public InventoryService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Item> getProductsByCategory(String category) {
        String url = "https://fakestoreapi.com/products/category/" + category;
        try {
            Item[] products = restTemplate.getForObject(url, Item[].class);
            return Arrays.asList(products);
        } catch (HttpClientErrorException e) {
            logger.error("Error fetching products: " + e.getMessage());
            throw new RuntimeException("Error fetching products for category: " + category, e);
        } catch (ResourceAccessException e) {
            logger.error("Network error: " + e.getMessage());
            throw new RuntimeException("Network error while fetching products", e);
        }
    }

    public Item addProduct(Item product) {
        System.out.println(product);
        String url = "https://fakestoreapi.com/products";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Item> requestEntity = new HttpEntity<>(product, headers);

        try {
            ResponseEntity<Item> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Item.class);
            logger.info("Received response: {}", response.getBody());
            return response.getBody();
        } catch (HttpClientErrorException e) {
            logger.error("Failed to create product: " + e.getMessage());
            throw new RuntimeException("Failed to create product: " + e.getMessage(), e);
        } catch (ResourceAccessException e) {
            logger.error("Network error while creating product: " + e.getMessage());
            throw new RuntimeException("Network error while creating product", e);
        }
    }
}
