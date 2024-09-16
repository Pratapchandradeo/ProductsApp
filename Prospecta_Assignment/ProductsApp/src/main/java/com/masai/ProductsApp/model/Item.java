package com.masai.ProductsApp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {

    private long id;

    
    private String title;

    
    private Double price;

   
    private String description;

    
    private String category;

    private String image;

    private ItemRating rating;
}
