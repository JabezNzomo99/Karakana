package com.jabezmagomere.karakanaics.Model;

public class Product {
    private String ProductId, ProductName, Category, Manfacturer, SerialNumber, Price, PhotoURL, Description;

    public String getPhotoURL() {
        return PhotoURL;
    }

    public void setPhotoURL(String photoURL) {
        PhotoURL = photoURL;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getProductId() {
        return ProductId;
    }

    public void setProductId(String productId) {
        ProductId = productId;
    }

    public Product(String productId, String productName, String category, String manfacturer, String serialNumber, String price, String photoURL, String description) {
        ProductId=productId;
        ProductName = productName;
        Category = category;
        Manfacturer = manfacturer;
        SerialNumber = serialNumber;
        Price = price;
        PhotoURL = photoURL;
        Description = description;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getManfacturer() {
        return Manfacturer;
    }

    public void setManfacturer(String manfacturer) {
        Manfacturer = manfacturer;
    }

    public String getSerialNumber() {
        return SerialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        SerialNumber = serialNumber;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }
}
