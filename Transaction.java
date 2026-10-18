import java.time.LocalDateTime;

public class Transaction {
    public enum Type { BUY, SELL }

    private final Type type;
    private final String symbol;
    private final int quantity;
    private final double price;
    private final LocalDateTime time;

    public Transaction(Type type, String symbol, int quantity, double price, LocalDateTime time) {
        this.type = type;
        this.symbol = symbol;
        this.quantity = quantity;
        this.price = price;
        this.time = time;
    }

    public Type getType() { return type; }
    public String getSymbol() { return symbol; }
    public int getQuantity() { return quantity; }
    public double getPrice() { return price; }
    public LocalDateTime getTime() { return time; }
    public double getTotal() { return price * quantity; }

    public String toCsv() {
        return String.join(",", "TXN", type.name(), symbol, String.valueOf(quantity),
                String.valueOf(price), time.toString());
    }

    public static Transaction fromParts(String[] p) {
        return new Transaction(Type.valueOf(p[1]), p[2], Integer.parseInt(p[3]),
                Double.parseDouble(p[4]), LocalDateTime.parse(p[5]));
    }
}
