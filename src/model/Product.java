package model;

import java.time.LocalDate;

public class Product {
    private int productId;
    private String productName;
    private String category;
    private String brand;
    private int quantity;
    private double unitPrice;
    private LocalDate expiryDate;
    private int minimumStock;

    public Product() {}

    public Product(int productId, String productName, String category, String brand,
                   int quantity, double unitPrice, LocalDate expiryDate, int minimumStock) {
        this.productId = productId;
        this.productName = productName;
        this.category = category;
        this.brand = brand;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.expiryDate = expiryDate;
        this.minimumStock = minimumStock;
    }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public double getUnitPrice() { return unitPrice; }
    public void setUnitPrice(double unitPrice) { this.unitPrice = unitPrice; }
    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }
    public int getMinimumStock() { return minimumStock; }
    public void setMinimumStock(int minimumStock) { this.minimumStock = minimumStock; }
}
