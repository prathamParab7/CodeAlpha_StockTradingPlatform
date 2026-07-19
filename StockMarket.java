import java.util.ArrayList;

public class StockMarket {
    private ArrayList<Stock> stocks;

    public StockMarket() {
        stocks = new ArrayList<>();
        // Initialize predefined stocks with realistic sample prices
        stocks.add(new Stock("TCS", "Tata Consultancy Services", 3500.0));
        stocks.add(new Stock("INFY", "Infosys Limited", 1650.0));
        stocks.add(new Stock("RELIANCE", "Reliance Industries", 2850.0));
        stocks.add(new Stock("HDFCBANK", "HDFC Bank Limited", 1750.0));
        stocks.add(new Stock("ICICIBANK", "ICICI Bank Limited", 950.0));
        stocks.add(new Stock("SBIN", "State Bank of India", 610.0));
        stocks.add(new Stock("WIPRO", "Wipro Limited", 420.0));
        stocks.add(new Stock("TATAMOTORS", "Tata Motors Limited", 630.0));
    }

    public ArrayList<Stock> getStocks() {
        return stocks;
    }

    public void displayMarket() {
        System.out.println("----------------------------------------------------------------------");
        System.out.printf("%-15s %-35s %-15s\n", "Symbol", "Company", "Current Price");
        System.out.println("----------------------------------------------------------------------");
        for (Stock stock : stocks) {
            System.out.printf("%-15s %-35s ₹%-14.2f\n", stock.getStockSymbol(), stock.getCompanyName(), stock.getCurrentPrice());
        }
        System.out.println("----------------------------------------------------------------------");
    }

    public Stock searchStock(String symbol) {
        for (Stock stock : stocks) {
            if (stock.getStockSymbol().equalsIgnoreCase(symbol)) {
                return stock;
            }
        }
        return null;
    }

    public Stock getStock(String symbol) {
        return searchStock(symbol);
    }
}
