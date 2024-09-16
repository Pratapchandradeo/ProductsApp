# 📦 ProductsApp API

## Overview

The ProductsApp API is a RESTful service built with Spring Boot that interacts with the [Fake Store API](https://fakestoreapi.com/) to manage product data. It provides endpoints to fetch products by category, add new products, and upload CSV files for processing.

## Features

- **Get Products by Category**: Fetch products by their category using a `GET` request.
- **Add New Product**: Add new products to the store using a `POST` request.
- **Upload and Process CSV**: Upload CSV files to evaluate and process data.

## Base URL

The API is accessible at `http://localhost:8088`.

## API Endpoints

### 1. **Get Products by Category**

- **Endpoint:** `GET /api/products/category/{category}`
- **Description:** Retrieves a list of products based on the specified category.
- **Example Request:**
    ```bash
    curl --location 'http://localhost:8088/api/products/category/jewelery'
    ```
- **Example Response:**
    ```json
    [
      {
        "id": 5,
        "title": "John Hardy Women's Legends Naga Gold & Silver Dragon Station Chain Bracelet",
        "price": 695,
        "description": "From our Legends Collection, the Naga was inspired by the mythical water dragon that protects the ocean's pearl. Wear facing inward to be bestowed with love and abundance, or outward for protection.",
        "category": "jewelery",
        "image": "https://fakestoreapi.com/img/71pWzhdJNwL._AC_UL640_QL65_ML3_.jpg",
        "rating": {
          "rate": 4.6,
          "count": 400
        }
      },
      {
        "id": 6,
        "title": "Solid Gold Petite Micropave ",
        "price": 168,
        "description": "Satisfaction Guaranteed. Return or exchange any order within 30 days. Designed and sold by Hafeez Center in the United States. Satisfaction Guaranteed. Return or exchange any order within 30 days.",
        "category": "jewelery",
        "image": "https://fakestoreapi.com/img/61sbMiUnoGL._AC_UL640_QL65_ML3_.jpg",
        "rating": {
          "rate": 3.9,
          "count": 70
        }
      
      }
    ]
    ```

### 2. **Add New Product**

- **Endpoint:** `POST /api/products/add`
- **Description:** Adds a new product to the store.
- **Request Body Example:**
    ```json
    {
      "id": 1,
      "title": "Fjallraven - Foldsack No. 1 Backpack, Fits 15 Laptops",
      "price": 109.95,
      "description": "Your perfect pack for everyday use and walks in the forest. Stash your laptop (up to 15 inches) in the padded sleeve, your everyday",
      "category": "men's clothing",
      "image": "https://fakestoreapi.com/img/81fPKd-2AYL._AC_SL1500_.jpg",
      "rating": {
        "rate": 3.9,
        "count": 120
      }
    }
    ```
- **Example Request:**
    ```bash
    curl --location 'http://localhost:8088/api/products/add' \
    --data '{
      "id": 1,
      "title": "Fjallraven - Foldsack No. 1 Backpack, Fits 15 Laptops",
      "price": 109.95,
      "description": "Your perfect pack for everyday use and walks in the forest. Stash your laptop (up to 15 inches) in the padded sleeve, your everyday",
      "category": "men'\''s clothing",
      "image": "https://fakestoreapi.com/img/81fPKd-2AYL._AC_SL1500_.jpg",
      "rating": {
        "rate": 3.9,
        "count": 120
      }
    }'
    ```
- **Example Response:**
    ```json
    {
      "id": 21,
      "title": "Fjallraven - Foldsack No. 1 Backpack, Fits 15 Laptops",
      "price": 109.95,
      "description": "Your perfect pack for everyday use and walks in the forest. Stash your laptop (up to 15 inches) in the padded sleeve, your everyday",
      "category": "men's clothing",
      "image": "https://fakestoreapi.com/img/81fPKd-2AYL._AC_SL1500_.jpg",
      "rating": null
    }
    ```

### 3. **Upload and Process CSV**

- **Endpoint:** `POST /api/csv/upload`
- **Description:** Uploads a CSV file, processes it, and returns the evaluated result.
- **Example Request:**
    ```bash
    curl --location 'http://localhost:8088/api/csv/upload' \
    --form 'file=@"/C:/Users/Administrator/Documents/Personal Doc/Masai Project/ProductsApp/Prospecta_Assignment/CSV_Assignment_Prospecta_/CSV_Assignment_Prospecta/target/test-classes/input3.csv"'
    ```
- **Example Response:**
    ```
    10,20,5,25
    15,35,70,75
    25,20,95,-55
    ```

## How to Test

1. **Using Postman:**
   - Send `GET` and `POST` requests using the provided endpoints.
   - Use the `CSV Upload` feature by attaching a `.csv` file in the body of the request.

## Conclusion

The ProductsApp API offers a seamless integration with the Fake Store API to manage product data and process CSV files efficiently. We encourage you to test the endpoints and provide feedback for further improvements.

Happy coding! 🚀
