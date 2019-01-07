package com.jabezmagomere.karakanaics.Model;

public class Purchase {
    private String ProductName, Amount, Status, PurchaseDate;

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getPurchaseDate() {
        return PurchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        PurchaseDate = purchaseDate;
    }

    public Purchase(String productName, String amount, String status, String purchaseDate) {
        ProductName = productName;
        Amount = amount;
        Status = status;
        PurchaseDate = purchaseDate;
    }
}
