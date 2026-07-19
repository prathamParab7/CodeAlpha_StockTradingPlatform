import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class StockTradingGUI extends JFrame {
    private User user;
    private Portfolio portfolio;
    private StockMarket market;
    private List<Transaction> transactions;
    private FileManager fileManager;

    // UI Components
    private JLabel lblBalance;
    private JLabel lblUserName;
    private JTable tblMarket;
    private JTable tblPortfolio;
    private JTable tblTransactions;

    // Performance labels
    private JLabel lblTotalInvestment;
    private JLabel lblCurrentValue;
    private JLabel lblProfitLoss;
    private JLabel lblPercentage;

    public StockTradingGUI(User user, Portfolio portfolio, StockMarket market, List<Transaction> transactions, FileManager fileManager) {
        this.user = user;
        this.portfolio = portfolio;
        this.market = market;
        this.transactions = transactions;
        this.fileManager = fileManager;

        setTitle("Stock Trading Platform");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);

        // Save on window close
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exitAndSave();
            }
        });

        initComponents();
        refreshAll();
    }

    private void initComponents() {
        // Layout: BorderLayout
        setLayout(new BorderLayout());

        // Header Panel (User Info)
        JPanel headerPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        headerPanel.setBackground(new Color(240, 240, 240));

        lblUserName = new JLabel("User: " + user.getUserName() + " (" + user.getUserId() + ")");
        lblUserName.setFont(new Font("Arial", Font.BOLD, 14));

        lblBalance = new JLabel("Available Balance: ₹" + String.format("%.2f", user.getAvailableBalance()), SwingConstants.RIGHT);
        lblBalance.setFont(new Font("Arial", Font.BOLD, 14));

        headerPanel.add(lblUserName);
        headerPanel.add(new JLabel()); // spacer
        headerPanel.add(lblBalance);
        add(headerPanel, BorderLayout.NORTH);

        // Tabbed Pane
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addChangeListener(e -> refreshAll());

        // Tab 1: Market Data
        tabbedPane.addTab("Market Data", createMarketPanel());

        // Tab 2: Portfolio
        tabbedPane.addTab("View Portfolio", createPortfolioPanel());

        // Tab 3: Performance
        tabbedPane.addTab("Portfolio Performance", createPerformancePanel());

        // Tab 4: Transaction History
        tabbedPane.addTab("Transaction History", createTransactionHistoryPanel());

        add(tabbedPane, BorderLayout.CENTER);

        // Status bar or footer with Save button
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSave = new JButton("Save Portfolio");
        btnSave.addActionListener(e -> {
            fileManager.savePortfolio(user, portfolio);
            fileManager.saveTransactions(transactions);
            JOptionPane.showMessageDialog(this, "Portfolio and Transaction history saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        });
        JButton btnExit = new JButton("Exit");
        btnExit.addActionListener(e -> exitAndSave());

        footerPanel.add(btnSave);
        footerPanel.add(btnExit);
        add(footerPanel, BorderLayout.SOUTH);
    }

    private JPanel createMarketPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] columnNames = {"Symbol", "Company Name", "Current Price"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblMarket = new JTable(model);
        tblMarket.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(tblMarket);
        panel.add(scrollPane, BorderLayout.CENTER);

        JButton btnBuy = new JButton("Buy Selected Stock");
        btnBuy.setFont(new Font("Arial", Font.BOLD, 12));
        btnBuy.addActionListener(e -> handleBuyAction());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(btnBuy);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createPortfolioPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] columnNames = {"Symbol", "Company", "Quantity Owned", "Avg Buy Price", "Current Price", "Current Value"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblPortfolio = new JTable(model);
        tblPortfolio.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(tblPortfolio);
        panel.add(scrollPane, BorderLayout.CENTER);

        JButton btnSell = new JButton("Sell Selected Stock");
        btnSell.setFont(new Font("Arial", Font.BOLD, 12));
        btnSell.addActionListener(e -> handleSellAction());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(btnSell);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createPerformancePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel lbl1 = new JLabel("Total Investment:");
        lbl1.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(lbl1, gbc);

        lblTotalInvestment = new JLabel("₹0.00");
        lblTotalInvestment.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 1; gbc.gridy = 0;
        panel.add(lblTotalInvestment, gbc);

        JLabel lbl2 = new JLabel("Current Portfolio Value:");
        lbl2.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(lbl2, gbc);

        lblCurrentValue = new JLabel("₹0.00");
        lblCurrentValue.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 1; gbc.gridy = 1;
        panel.add(lblCurrentValue, gbc);

        JLabel lbl3 = new JLabel("Total Profit / Loss:");
        lbl3.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(lbl3, gbc);

        lblProfitLoss = new JLabel("₹0.00");
        lblProfitLoss.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 1; gbc.gridy = 2;
        panel.add(lblProfitLoss, gbc);

        JLabel lbl4 = new JLabel("Profit / Loss %:");
        lbl4.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(lbl4, gbc);

        lblPercentage = new JLabel("0.00%");
        lblPercentage.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 1; gbc.gridy = 3;
        panel.add(lblPercentage, gbc);

        return panel;
    }

    private JPanel createTransactionHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] columnNames = {"Transaction ID", "Stock Symbol", "Type", "Quantity", "Price", "Date & Time"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblTransactions = new JTable(model);
        tblTransactions.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(tblTransactions);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void handleBuyAction() {
        int selectedRow = tblMarket.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a stock from the list to buy.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String symbol = (String) tblMarket.getValueAt(selectedRow, 0);
        Stock stock = market.getStock(symbol);

        String qtyStr = JOptionPane.showInputDialog(this, "Enter quantity to buy for " + symbol + " (Price: ₹" + stock.getCurrentPrice() + "):");
        if (qtyStr == null) return; // cancel

        try {
            int quantity = Integer.parseInt(qtyStr.trim());
            if (quantity <= 0) {
                JOptionPane.showMessageDialog(this, "Quantity must be greater than zero.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double totalCost = stock.getCurrentPrice() * quantity;
            if (user.getAvailableBalance() < totalCost) {
                JOptionPane.showMessageDialog(this, String.format("Insufficient balance.\nRequired: ₹%.2f\nAvailable: ₹%.2f", totalCost, user.getAvailableBalance()), "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Execute Buy
            user.setAvailableBalance(user.getAvailableBalance() - totalCost);
            portfolio.addStock(stock, quantity, stock.getCurrentPrice());

            String transactionId = "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            String dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            transactions.add(new Transaction(transactionId, symbol, "BUY", quantity, stock.getCurrentPrice(), dateTime));

            JOptionPane.showMessageDialog(this, "Purchase Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
            refreshAll();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid quantity value. Please enter a valid integer.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleSellAction() {
        int selectedRow = tblPortfolio.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a stock from your portfolio to sell.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String symbol = (String) tblPortfolio.getValueAt(selectedRow, 0);
        Portfolio.Holding holding = portfolio.findHolding(symbol);
        if (holding == null) return;

        Stock stock = holding.getStock();

        String qtyStr = JOptionPane.showInputDialog(this, "Enter quantity to sell for " + symbol + " (Owned: " + holding.getQuantity() + ", Current Price: ₹" + stock.getCurrentPrice() + "):");
        if (qtyStr == null) return; // cancel

        try {
            int quantity = Integer.parseInt(qtyStr.trim());
            if (quantity <= 0) {
                JOptionPane.showMessageDialog(this, "Quantity must be greater than zero.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (holding.getQuantity() < quantity) {
                JOptionPane.showMessageDialog(this, "Insufficient quantity in portfolio to execute sale.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Execute Sell
            portfolio.removeStock(stock, quantity);
            double saleRevenue = stock.getCurrentPrice() * quantity;
            user.setAvailableBalance(user.getAvailableBalance() + saleRevenue);

            String transactionId = "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            String dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            transactions.add(new Transaction(transactionId, symbol, "SELL", quantity, stock.getCurrentPrice(), dateTime));

            JOptionPane.showMessageDialog(this, "Sale Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
            refreshAll();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid quantity value. Please enter a valid integer.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshAll() {
        // Refresh header balance
        if (lblBalance != null) {
            lblBalance.setText("Available Balance: ₹" + String.format("%.2f", user.getAvailableBalance()));
        }

        // Refresh Stock Market Data
        if (tblMarket != null) {
            DefaultTableModel marketModel = (DefaultTableModel) tblMarket.getModel();
            marketModel.setRowCount(0);
            for (Stock s : market.getStocks()) {
                marketModel.addRow(new Object[]{s.getStockSymbol(), s.getCompanyName(), "₹" + String.format("%.2f", s.getCurrentPrice())});
            }
        }

        // Refresh Portfolio Data
        if (tblPortfolio != null) {
            DefaultTableModel portfolioModel = (DefaultTableModel) tblPortfolio.getModel();
            portfolioModel.setRowCount(0);
            for (Portfolio.Holding h : portfolio.getHoldings()) {
                double currentValue = h.getQuantity() * h.getStock().getCurrentPrice();
                portfolioModel.addRow(new Object[]{
                        h.getStock().getStockSymbol(),
                        h.getStock().getCompanyName(),
                        h.getQuantity(),
                        "₹" + String.format("%.2f", h.getAverageBuyPrice()),
                        "₹" + String.format("%.2f", h.getStock().getCurrentPrice()),
                        "₹" + String.format("%.2f", currentValue)
                });
            }
        }

        // Refresh Performance Data
        if (lblTotalInvestment != null && lblCurrentValue != null && lblProfitLoss != null && lblPercentage != null) {
            double totalInv = portfolio.calculateTotalInvestment();
            double currVal = portfolio.calculateCurrentPortfolioValue();
            double pL = portfolio.calculateProfitLoss();
            double percent = (totalInv > 0) ? (pL / totalInv) * 100.0 : 0.0;

            lblTotalInvestment.setText("₹" + String.format("%.2f", totalInv));
            lblCurrentValue.setText("₹" + String.format("%.2f", currVal));
            lblProfitLoss.setText("₹" + String.format("%.2f", pL));
            if (pL >= 0) {
                lblProfitLoss.setForeground(new Color(0, 128, 0));
                lblPercentage.setForeground(new Color(0, 128, 0));
            } else {
                lblProfitLoss.setForeground(Color.RED);
                lblPercentage.setForeground(Color.RED);
            }
            lblPercentage.setText(String.format("%.2f%%", percent));
        }

        // Refresh Transaction History
        if (tblTransactions != null) {
            DefaultTableModel transactionModel = (DefaultTableModel) tblTransactions.getModel();
            transactionModel.setRowCount(0);
            for (Transaction t : transactions) {
                transactionModel.addRow(new Object[]{
                        t.getTransactionId(),
                        t.getStockSymbol(),
                        t.getType(),
                        t.getQuantity(),
                        "₹" + String.format("%.2f", t.getPrice()),
                        t.getDateTime()
                });
            }
        }
    }

    private void exitAndSave() {
        fileManager.savePortfolio(user, portfolio);
        fileManager.saveTransactions(transactions);
        JOptionPane.showMessageDialog(this, "Portfolio and Transaction history auto-saved. Goodbye!", "Exit", JOptionPane.INFORMATION_MESSAGE);
        dispose();
        System.exit(0);
    }
}
