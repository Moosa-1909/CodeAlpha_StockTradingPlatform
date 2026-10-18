import java.util.*;

/** Holds all tradable stocks and simulates price movement. */
public class Market {
    private final Map<String, Stock> stocks = new LinkedHashMap<>();
    private final Random random = new Random();

    public Market() {
        add(new Stock("INFY", "Infosys", 1500.00));
        add(new Stock("TCS", "Tata Consultancy", 3800.00));
        add(new Stock("RELIANCE", "Reliance Industries", 2900.00));
        add(new Stock("HDFCBANK", "HDFC Bank", 1650.00));
        add(new Stock("WIPRO", "Wipro", 480.00));
        add(new Stock("ITC", "ITC Limited", 450.00));
        add(new Stock("SBIN", "State Bank of India", 800.00));
        add(new Stock("TATAMOTORS", "Tata Motors", 950.00));
    }

    public void add(Stock s) { stocks.put(s.getSymbol(), s); }

    public Stock get(String symbol) { return stocks.get(symbol.toUpperCase()); }

    public Collection<Stock> all() { return stocks.values(); }

    /** Random-walk price move of roughly +/-2% per step. */
    public void tick() {
        for (Stock s : stocks.values()) {
            double change = random.nextGaussian() * 0.02;
            double next = Math.max(1.0, s.getPrice() * (1 + change));
            s.updatePrice(Math.round(next * 100.0) / 100.0);
        }
    }
}
