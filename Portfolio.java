import java.util.ArrayList;
import java.util.List;

public class Portfolio {
    public static class Holding {
        private Stock stock;
        private int quantity;
        private double averageBuyPrice;

        public Holding(Stock stock, int quantity, double averageBuyPrice) {
            this.stock = stock;
            this.quantity = quantity;
            this.averageBuyPrice = averageBuyPrice;
        }

        public Stock getStock() {
            return stock;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public double getAverageBuyPrice() {
            return averageBuyPrice;
        }

        public void setAverageBuyPrice(double averageBuyPrice) {
            this.averageBuyPrice = averageBuyPrice;
        }
    }

    private List<Holding> holdings;

    public Portfolio() {
        this.holdings = new ArrayList<>();
    }

    public List<Holding> getHoldings() {
        return holdings;
    }

    public void addStock(Stock stock, int quantity, double price) {
        Holding existing = findHolding(stock.getStockSymbol());
        if (existing != null) {
            double totalCost = (existing.getQuantity() * existing.getAverageBuyPrice()) + (quantity * price);
            int newQuantity = existing.getQuantity() + quantity;
            existing.setQuantity(newQuantity);
            existing.setAverageBuyPrice(totalCost / newQuantity);
        } else {
            holdings.add(new Holding(stock, quantity, price));
        }
    }

    public boolean removeStock(Stock stock, int quantity) {
        Holding existing = findHolding(stock.getStockSymbol());
        if (existing == null || existing.getQuantity() < quantity) {
            return false;
        }
        int newQuantity = existing.getQuantity() - quantity;
        if (newQuantity == 0) {
            holdings.remove(existing);
        } else {
            existing.setQuantity(newQuantity);
        }
        return true;
    }

    public Holding findHolding(String symbol) {
        for (Holding h : holdings) {
            if (h.getStock().getStockSymbol().equalsIgnoreCase(symbol)) {
                return h;
            }
        }
        return null;
    }

    public double calculateTotalInvestment() {
        double total = 0;
        for (Holding h : holdings) {
            total += h.getQuantity() * h.getAverageBuyPrice();
        }
        return total;
    }

    public double calculateCurrentPortfolioValue() {
        double total = 0;
        for (Holding h : holdings) {
            total += h.getQuantity() * h.getStock().getCurrentPrice();
        }
        return total;
    }

    public double calculateProfitLoss() {
        return calculateCurrentPortfolioValue() - calculateTotalInvestment();
    }

    public void displayHoldings(User user) {
        System.out.println("----------------------------------------------------------------------------------");
        System.out.printf("%-10s %-20s %-10s %-12s %-15s %-12s\n", "Symbol", "Company", "Quantity", "Buy Price", "Current Price", "Current Value");
        System.out.println("----------------------------------------------------------------------------------");
        if (holdings.isEmpty()) {
            System.out.println("No holdings found.");
        } else {
            for (Holding h : holdings) {
                double currentVal = h.getQuantity() * h.getStock().getCurrentPrice();
                System.out.printf("%-10s %-20s %-10d ₹%-11.2f ₹%-14.2f ₹%-11.2f\n",
                        h.getStock().getStockSymbol(),
                        h.getStock().getCompanyName(),
                        h.getQuantity(),
                        h.getAverageBuyPrice(),
                        h.getStock().getCurrentPrice(),
                        currentVal);
            }
        }
        System.out.println("----------------------------------------------------------------------------------");
        System.out.printf("Total Investment:        ₹%.2f\n", calculateTotalInvestment());
        System.out.printf("Current Portfolio Value: ₹%.2f\n", calculateCurrentPortfolioValue());
        System.out.printf("Available Balance:       ₹%.2f\n", user.getAvailableBalance());
        System.out.println("----------------------------------------------------------------------------------");
    }
}
