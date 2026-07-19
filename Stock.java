public class Stock {
    private String stockSymbol;
    private String companyName;
    private double currentPrice;

    public Stock(String stockSymbol, String companyName, double currentPrice) {
        this.stockSymbol = stockSymbol;
        this.companyName = companyName;
        this.currentPrice = currentPrice;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public void setStockSymbol(String stockSymbol) {
        this.stockSymbol = stockSymbol;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }

    @Override
    public String toString() {
        return String.format("%-12s %-25s ₹%.2f", stockSymbol, companyName, currentPrice);
    }
}
