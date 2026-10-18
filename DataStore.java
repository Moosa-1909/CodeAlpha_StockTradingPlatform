import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.*;

/** File I/O persistence for users (portfolio, trades, history) and market prices. */
public class DataStore {
    private final Path dir = Paths.get("data");

    public DataStore() {
        try { Files.createDirectories(dir); } catch (IOException e) {
            System.out.println("Warning: could not create data folder: " + e.getMessage());
        }
    }

    private Path userFile(String username) { return dir.resolve(username + ".txt"); }

    public boolean userExists(String username) { return Files.exists(userFile(username)); }

    public void saveUser(User u) {
        List<String> lines = new ArrayList<>();
        lines.add("CASH," + u.getCash());
        lines.add("REALIZED," + u.getRealizedPnl());
        for (Holding h : u.getHoldings().values())
            lines.add("HOLDING," + h.getSymbol() + "," + h.getQuantity() + "," + h.getAvgCost());
        for (Transaction t : u.getTransactions()) lines.add(t.toCsv());
        for (Snapshot s : u.getSnapshots()) lines.add("SNAP," + s.getTime() + "," + s.getValue());
        write(userFile(u.getUsername()), lines);
    }

    public User loadUser(String username) {
        User u = new User(username);
        try {
            for (String line : Files.readAllLines(userFile(username))) {
                if (line.isBlank()) continue;
                String[] p = line.split(",");
                switch (p[0]) {
                    case "CASH": u.setCash(Double.parseDouble(p[1])); break;
                    case "REALIZED": u.setRealizedPnl(Double.parseDouble(p[1])); break;
                    case "HOLDING":
                        u.getHoldings().put(p[1], new Holding(p[1], Integer.parseInt(p[2]), Double.parseDouble(p[3])));
                        break;
                    case "TXN": u.getTransactions().add(Transaction.fromParts(p)); break;
                    case "SNAP": u.getSnapshots().add(new Snapshot(LocalDateTime.parse(p[1]), Double.parseDouble(p[2]))); break;
                    default: break;
                }
            }
        } catch (IOException | RuntimeException e) {
            System.out.println("Warning: problem reading user file: " + e.getMessage());
        }
        return u;
    }

    public void saveMarket(Market m) {
        List<String> lines = new ArrayList<>();
        for (Stock s : m.all())
            lines.add(String.join(",", s.getSymbol(), s.getName(), String.valueOf(s.getPrice()),
                    String.valueOf(s.getPreviousPrice())));
        write(dir.resolve("market.csv"), lines);
    }

    /** Restores saved prices so the market continues where it left off. */
    public void loadMarket(Market m) {
        Path f = dir.resolve("market.csv");
        if (!Files.exists(f)) return;
        try {
            for (String line : Files.readAllLines(f)) {
                if (line.isBlank()) continue;
                String[] p = line.split(",");
                m.add(new Stock(p[0], p[1], Double.parseDouble(p[2]), Double.parseDouble(p[3])));
            }
        } catch (IOException | RuntimeException e) {
            System.out.println("Warning: problem reading market file: " + e.getMessage());
        }
    }

    private void write(Path file, List<String> lines) {
        try { Files.write(file, lines); } catch (IOException e) {
            System.out.println("Error: could not save data: " + e.getMessage());
        }
    }
}
