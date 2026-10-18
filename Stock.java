public class Stock {
    private final String symbol;
    private final String name;
    private double price;
    private double previousPrice;

    public Stock(String symbol, String name, double price) {
        this(symbol, name, price, price);
    }

    public Stock(String symbol, String name, double price, double previousPrice) {
        this.symbol = symbol;
        this.name = name;
        this.price = price;
        this.previousPrice = previousPrice;
    }

    public String getSymbol() { return symbol; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public double getPreviousPrice() { return previousPrice; }

    public void updatePrice(double newPrice) {
        this.previousPrice = this.price;
        this.price = newPrice;
    }

    public double getChangePercent() {
        return previousPrice == 0 ? 0 : (price - previousPrice) / previousPrice * 100;
    }
}
