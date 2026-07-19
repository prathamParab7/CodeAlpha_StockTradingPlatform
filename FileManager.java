import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    private static final String PORTFOLIO_FILE = "portfolio.txt";
    private static final String TRANSACTIONS_FILE = "transactions.txt";

    public void savePortfolio(User user, Portfolio portfolio) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PORTFOLIO_FILE))) {
            // Save User details
            writer.write("USER:" + user.getUserId() + "," + user.getUserName() + "," + user.getAvailableBalance());
            writer.newLine();

            // Save Holdings
            for (Portfolio.Holding h : portfolio.getHoldings()) {
                writer.write("HOLDING:" + h.getStock().getStockSymbol() + "," + h.getQuantity() + "," + h.getAverageBuyPrice());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving portfolio data: " + e.getMessage());
        }
    }

    public void loadPortfolio(User user, Portfolio portfolio, StockMarket market) {
        File file = new File(PORTFOLIO_FILE);
        if (!file.exists()) {
            return;
        }
        portfolio.getHoldings().clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("USER:")) {
                    String[] parts = line.substring(5).split(",");
                    if (parts.length >= 3) {
                        user.setUserId(parts[0]);
                        user.setUserName(parts[1]);
                        user.setAvailableBalance(Double.parseDouble(parts[2]));
                    }
                } else if (line.startsWith("HOLDING:")) {
                    String[] parts = line.substring(8).split(",");
                    if (parts.length >= 3) {
                        String symbol = parts[0];
                        int quantity = Integer.parseInt(parts[1]);
                        double averageBuyPrice = Double.parseDouble(parts[2]);

                        Stock stock = market.getStock(symbol);
                        if (stock == null) {
                            stock = new Stock(symbol, symbol, averageBuyPrice);
                        }
                        portfolio.getHoldings().add(new Portfolio.Holding(stock, quantity, averageBuyPrice));
                    }
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error loading portfolio data: " + e.getMessage());
        }
    }

    public void saveTransactions(List<Transaction> transactions) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TRANSACTIONS_FILE))) {
            for (Transaction t : transactions) {
                writer.write(t.getTransactionId() + "," +
                             t.getStockSymbol() + "," +
                             t.getType() + "," +
                             t.getQuantity() + "," +
                             t.getPrice() + "," +
                             t.getDateTime());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving transaction history: " + e.getMessage());
        }
    }

    public List<Transaction> loadTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        File file = new File(TRANSACTIONS_FILE);
        if (!file.exists()) {
            return transactions;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 6) {
                    transactions.add(new Transaction(
                            parts[0],
                            parts[1],
                            parts[2],
                            Integer.parseInt(parts[3]),
                            Double.parseDouble(parts[4]),
                            parts[5]
                    ));
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error loading transaction history: " + e.getMessage());
        }
        return transactions;
    }
}
