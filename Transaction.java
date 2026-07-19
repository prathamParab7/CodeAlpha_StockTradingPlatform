import java.util.List;

public class Transaction {
    private String transactionId;
    private String stockSymbol;
    private String type; // BUY or SELL
    private int quantity;
    private double price;
    private String dateTime;

    public Transaction(String transactionId, String stockSymbol, String type, int quantity, double price, String dateTime) {
        this.transactionId = transactionId;
        this.stockSymbol = stockSymbol;
        this.type = type;
        this.quantity = quantity;
        this.price = price;
        this.dateTime = dateTime;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public String getType() {
        return type;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public String getDateTime() {
        return dateTime;
    }

    public static void displayTransactionHistory(List<Transaction> transactions) {
        System.out.println("------------------------------------------------------------------------------------------------------");
        System.out.printf("%-15s %-12s %-10s %-10s %-12s %-25s\n", "Transaction ID", "Stock Symbol", "Type", "Quantity", "Price", "Date & Time");
        System.out.println("------------------------------------------------------------------------------------------------------");
        if (transactions.isEmpty()) {
            System.out.println("No transaction history found.");
        } else {
            for (Transaction t : transactions) {
                System.out.printf("%-15s %-12s %-10s %-10d ₹%-11.2f %-25s\n",
                        t.getTransactionId(),
                        t.getStockSymbol(),
                        t.getType(),
                        t.getQuantity(),
                        t.getPrice(),
                        t.getDateTime());
            }
        }
        System.out.println("------------------------------------------------------------------------------------------------------");
    }
}
