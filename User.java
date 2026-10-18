import java.util.*;

public class User {
    public static final double STARTING_CASH = 100000.0;

    private final String username;
    private double cash = STARTING_CASH;
    private double realizedPnl = 0;
    private final Map<String, Holding> holdings = new LinkedHashMap<>();
    private final List<Transaction> transactions = new ArrayList<>();
    private final List<Snapshot> snapshots = new ArrayList<>();

    public User(String username) { this.username = username; }

    public String getUsername() { return username; }
    public double getCash() { return cash; }
    public void setCash(double cash) { this.cash = cash; }
    public double getRealizedPnl() { return realizedPnl; }
    public void setRealizedPnl(double v) { this.realizedPnl = v; }
    public Map<String, Holding> getHoldings() { return holdings; }
    public List<Transaction> getTransactions() { return transactions; }
    public List<Snapshot> getSnapshots() { return snapshots; }
}
