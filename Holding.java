public class Holding {
    private final String symbol;
    private int quantity;
    private double avgCost;

    public Holding(String symbol, int quantity, double avgCost) {
        this.symbol = symbol;
        this.quantity = quantity;
        this.avgCost = avgCost;
    }

    public String getSymbol() { return symbol; }
    public int getQuantity() { return quantity; }
    public double getAvgCost() { return avgCost; }

    public void addShares(int qty, double price) {
        double totalCost = avgCost * quantity + price * qty;
        quantity += qty;
        avgCost = totalCost / quantity;
    }

    public void removeShares(int qty) { quantity -= qty; }
}
