import java.time.LocalDateTime;

public class TradingService {
    private final Market market;
    private final DataStore store;

    public TradingService(Market market, DataStore store) {
        this.market = market;
        this.store = store;
    }

    private Stock requireStock(String symbol) {
        Stock s = market.get(symbol.trim());
        if (s == null) throw new IllegalArgumentException("Unknown stock symbol: " + symbol);
        return s;
    }

    public void buy(User u, String symbol, int qty) {
        if (qty <= 0) throw new IllegalArgumentException("Quantity must be positive");
        Stock s = requireStock(symbol);
        double cost = s.getPrice() * qty;
        if (cost > u.getCash())
            throw new IllegalStateException(String.format("Insufficient funds: need Rs.%.2f, have Rs.%.2f", cost, u.getCash()));
        u.setCash(u.getCash() - cost);
        u.getHoldings().computeIfAbsent(s.getSymbol(), k -> new Holding(k, 0, 0)).addShares(qty, s.getPrice());
        u.getTransactions().add(new Transaction(Transaction.Type.BUY, s.getSymbol(), qty, s.getPrice(), LocalDateTime.now()));
        snapshot(u);
        store.saveUser(u);
    }

    /** Returns the realized profit/loss of this sale. */
    public double sell(User u, String symbol, int qty) {
        if (qty <= 0) throw new IllegalArgumentException("Quantity must be positive");
        Stock s = requireStock(symbol);
        Holding h = u.getHoldings().get(s.getSymbol());
        if (h == null || h.getQuantity() < qty)
            throw new IllegalStateException("You don't own enough shares of " + s.getSymbol());
        double pnl = (s.getPrice() - h.getAvgCost()) * qty;
        u.setCash(u.getCash() + s.getPrice() * qty);
        u.setRealizedPnl(u.getRealizedPnl() + pnl);
        h.removeShares(qty);
        if (h.getQuantity() == 0) u.getHoldings().remove(s.getSymbol());
        u.getTransactions().add(new Transaction(Transaction.Type.SELL, s.getSymbol(), qty, s.getPrice(), LocalDateTime.now()));
        snapshot(u);
        store.saveUser(u);
        return pnl;
    }

    public double holdingsValue(User u) {
        double total = 0;
        for (Holding h : u.getHoldings().values())
            total += h.getQuantity() * market.get(h.getSymbol()).getPrice();
        return total;
    }

    public double totalValue(User u) { return u.getCash() + holdingsValue(u); }

    public void snapshot(User u) {
        u.getSnapshots().add(new Snapshot(LocalDateTime.now(), totalValue(u)));
    }

    /** Simulates time passing: prices move and the portfolio value is recorded. */
    public void advanceMarket(User u) {
        market.tick();
        store.saveMarket(market);
        snapshot(u);
        store.saveUser(u);
    }
}
