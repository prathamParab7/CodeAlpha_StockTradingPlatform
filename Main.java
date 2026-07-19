import java.util.List;

public class Main {
    public static void main(String[] args) {
        StockMarket market = new StockMarket();
        User user = new User("U101", "Default User");
        Portfolio portfolio = new Portfolio();
        FileManager fileManager = new FileManager();

        // Automatically load when program starts
        fileManager.loadPortfolio(user, portfolio, market);
        List<Transaction> transactions = fileManager.loadTransactions();

        // Launch GUI directly
        javax.swing.SwingUtilities.invokeLater(() -> {
            new StockTradingGUI(user, portfolio, market, transactions, fileManager).setVisible(true);
        });
    }
}
