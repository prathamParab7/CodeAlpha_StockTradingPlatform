public class User {
    private String userId;
    private String userName;
    private double availableBalance;

    public User(String userId, String userName) {
        this.userId = userId;
        this.userName = userName;
        this.availableBalance = 100000.0; // Default balance: ₹100,000
    }

    public User(String userId, String userName, double availableBalance) {
        this.userId = userId;
        this.userName = userName;
        this.availableBalance = availableBalance;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public double getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(double availableBalance) {
        this.availableBalance = availableBalance;
    }
}
