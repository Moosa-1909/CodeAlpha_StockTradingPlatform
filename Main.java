import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner sc = new Scanner(System.in);
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd-MMM HH:mm:ss");
    private static Market market;
    private static TradingService service;
    private static User user;

    public static void main(String[] args) {
        DataStore store = new DataStore();
        market = new Market();
        store.loadMarket(market);
        service = new TradingService(market, store);

        System.out.println("=== Stock Trading Platform (simulated) ===");
        String name = ask("Enter username: ").replaceAll("[^A-Za-z0-9_]", "");
        if (name.isEmpty()) { System.out.println("Invalid username."); return; }
        if (store.userExists(name)) {
            user = store.loadUser(name);
            System.out.println("Welcome back, " + name + "!");
        } else {
            user = new User(name);
            service.snapshot(user);
            store.saveUser(user);
            System.out.printf("New account created with Rs.%.2f virtual cash.%n", User.STARTING_CASH);
        }

        while (true) {
            System.out.println("\n1. Market data\n2. Buy\n3. Sell\n4. My portfolio\n5. Performance history"
                    + "\n6. Transaction history\n7. Advance market (simulate time)\n8. Exit");
            switch (ask("Choose: ")) {
                case "1": showMarket(); break;
                case "2": run(Main::buy); break;
                case "3": run(Main::sell); break;
                case "4": showPortfolio(); break;
                case "5": showPerformance(); break;
                case "6": showTransactions(); break;
                case "7": service.advanceMarket(user); showMarket(); break;
                case "8": System.out.println("Goodbye!"); return;
                default: System.out.println("Invalid choice.");
            }
        }
    }

    private static void run(Runnable r) {
        try { r.run(); } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static String ask(String p) { System.out.print(p); return sc.nextLine().trim(); }

    private static void showMarket() {
        System.out.printf("%n%-12s %-22s %10s %9s%n", "SYMBOL", "NAME", "PRICE", "CHANGE");
        for (Stock s : market.all())
            System.out.printf("%-12s %-22s %10.2f %+8.2f%%%n", s.getSymbol(), s.getName(), s.getPrice(), s.getChangePercent());
    }

    private static void buy() {
        showMarket();
        String sym = ask("Symbol: ");
        int qty = Integer.parseInt(ask("Quantity: "));
        service.buy(user, sym, qty);
        System.out.printf("Bought %d x %s. Cash left: Rs.%.2f%n", qty, sym.toUpperCase(), user.getCash());
    }

    private static void sell() {
        String sym = ask("Symbol: ");
        int qty = Integer.parseInt(ask("Quantity: "));
        double pnl = service.sell(user, sym, qty);
        System.out.printf("Sold %d x %s. Realized P/L: %+.2f | Cash: Rs.%.2f%n", qty, sym.toUpperCase(), pnl, user.getCash());
    }

    private static void showPortfolio() {
        System.out.printf("%n%-12s %5s %10s %10s %12s %11s %8s%n", "SYMBOL", "QTY", "AVG COST", "PRICE", "VALUE", "P/L", "P/L %");
        double unrealized = 0;
        for (Holding h : user.getHoldings().values()) {
            double price = market.get(h.getSymbol()).getPrice();
            double value = price * h.getQuantity();
            double pl = (price - h.getAvgCost()) * h.getQuantity();
            unrealized += pl;
            System.out.printf("%-12s %5d %10.2f %10.2f %12.2f %+11.2f %+7.2f%%%n", h.getSymbol(), h.getQuantity(),
                    h.getAvgCost(), price, value, pl, (price / h.getAvgCost() - 1) * 100);
        }
        if (user.getHoldings().isEmpty()) System.out.println("(no holdings)");
        double total = service.totalValue(user);
        System.out.println("-----------------------------------------------------------------------------");
        System.out.printf("Cash: Rs.%.2f | Holdings: Rs.%.2f | Total: Rs.%.2f%n", user.getCash(), service.holdingsValue(user), total);
        System.out.printf("Unrealized P/L: %+.2f | Realized P/L: %+.2f | Overall return: %+.2f%%%n",
                unrealized, user.getRealizedPnl(), (total / User.STARTING_CASH - 1) * 100);
    }

    private static void showPerformance() {
        List<Snapshot> snaps = user.getSnapshots();
        System.out.printf("%n%-16s %14s %12s%n", "TIME", "PORTFOLIO", "CHANGE");
        double prev = User.STARTING_CASH;
        for (Snapshot s : snaps) {
            System.out.printf("%-16s %14.2f %+12.2f%n", s.getTime().format(FMT), s.getValue(), s.getValue() - prev);
            prev = s.getValue();
        }
    }

    private static void showTransactions() {
        if (user.getTransactions().isEmpty()) { System.out.println("No transactions yet."); return; }
        System.out.printf("%n%-16s %-5s %-12s %5s %10s %12s%n", "TIME", "TYPE", "SYMBOL", "QTY", "PRICE", "TOTAL");
        for (Transaction t : user.getTransactions())
            System.out.printf("%-16s %-5s %-12s %5d %10.2f %12.2f%n", t.getTime().format(FMT), t.getType(),
                    t.getSymbol(), t.getQuantity(), t.getPrice(), t.getTotal());
    }
}
