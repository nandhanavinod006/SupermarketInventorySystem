package dao;

public class StockTransaction {

    private int transactionId;
    private int productId;
    private String transactionType;
    private int quantity;

    public StockTransaction(int productId, String transactionType, int quantity) {
        this.productId = productId;
        this.transactionType = transactionType;
        this.quantity = quantity;
    }

    public int getProductId() {
        return productId;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public int getQuantity() {
        return quantity;
    }
}
    

