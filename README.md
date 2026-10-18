# 📈 Stock Trading Platform

A console-based stock trading simulator built in **Java** using object-oriented design. View live-style market data, buy and sell shares with virtual cash, and track how your portfolio performs over time. Everything is saved to disk so your account continues between runs.

> Built as **Task 2** of the **CodeAlpha Java Programming Internship**.

---

## ✨ Features

| Feature | Details |
|---|---|
| **Market data display** | 8 stocks with price and % change since the last market update |
| **Buy / sell operations** | Trade at current market price with full validation |
| **Portfolio tracking** | Quantity, average cost, current value and profit/loss per holding |
| **Performance over time** | Portfolio value is snapshotted after every trade and market update |
| **Transaction history** | Complete log of every buy and sell with timestamps |
| **Market simulation** | "Advance market" moves prices using a random walk (about ±2% per step) |
| **Multi-user** | Each username gets its own account with ₹1,00,000 virtual cash |
| **Persistence** | Users and market prices are stored under `data/` and reloaded on startup |

### Profit and loss

- **Unrealized P/L:** `(current price − average cost) × quantity` for shares you still hold
- **Realized P/L:** profit or loss locked in by selling, accumulated across all sales
- **Average cost:** recalculated on every buy, so buying the same stock at different prices is handled correctly

---

## 🧱 Project Structure

```
.
├── Main.java            # Console menu and display formatting
├── TradingService.java  # Business logic: buy, sell, valuation, snapshots
├── Market.java          # Stocks and price simulation
├── Stock.java           # Stock model (price, previous price, % change)
├── User.java            # Account: cash, holdings, transactions, history
├── Holding.java         # Shares owned and average cost
├── Transaction.java     # Buy/sell record
├── Snapshot.java        # Portfolio value at a point in time
├── DataStore.java       # File I/O persistence layer
└── README.md
```

### Design overview

- **OOP separation:** models (`Stock`, `Holding`, `Transaction`, `Snapshot`, `User`), logic (`TradingService`, `Market`), storage (`DataStore`) and UI (`Main`) are independent classes.
- **Validation:** unknown symbols, non-positive quantities, insufficient funds and overselling all produce clear error messages without crashing.
- **Plain-text persistence:** each user is stored as a readable file (`data/<username>.txt`) and market prices in `data/market.csv`.
- **No external dependencies:** only the Java standard library.

---

## 🚀 Getting Started

### Requirements

- Java **11 or higher** (JDK)

### Run from the command line

```bash
git clone https://github.com/Moosa-1909/CodeAlpha_StockTradingPlatform.git
cd CodeAlpha_StockTradingPlatform
javac *.java
java Main
```

### Run in IntelliJ IDEA

1. Open the project folder.
2. Open `Main.java`.
3. Click the ▶ **Run** icon next to `public static void main`.

The `data/` folder is created automatically on first run.

---

## 🖥️ Sample Session

```
=== Stock Trading Platform (simulated) ===
Enter username: moosa
New account created with Rs.100000.00 virtual cash.

Choose: 2
Symbol: INFY
Quantity: 10
Bought 10 x INFY. Cash left: Rs.85000.00

Choose: 7        (advance market)

SYMBOL       NAME                        PRICE    CHANGE
INFY         Infosys                   1488.62    -0.76%
TCS          Tata Consultancy          3770.91    -0.77%
...

Choose: 3
Symbol: INFY
Quantity: 4
Sold 4 x INFY. Realized P/L: -45.52 | Cash: Rs.90954.48

Choose: 4
SYMBOL         QTY   AVG COST      PRICE        VALUE         P/L    P/L %
INFY             6    1500.00    1488.62      8931.72      -68.28   -0.76%
-----------------------------------------------------------------------------
Cash: Rs.90954.48 | Holdings: Rs.8931.72 | Total: Rs.99886.20
Unrealized P/L: -68.28 | Realized P/L: -45.52 | Overall return: -0.11%
```

Error handling examples:

```
Error: Insufficient funds: need Rs.48026000.00, have Rs.90954.48
Error: You don't own enough shares of TCS
```

---

## ⚠️ Disclaimer

All prices are **simulated** and all money is **virtual**. This project is for learning purposes only and is not financial advice.

## 🔮 Possible Improvements

- Fetch real market data from a public API
- Replace file storage with a database (JDBC)
- Add limit orders and brokerage fees
- Portfolio performance chart (JavaFX or exported image)
- Unit tests for P/L and average-cost calculations

---

## 👤 Author

**Moosa**
B.E. Computer Science, Muffakham Jah College of Engineering and Technology
GitHub: [@Moosa-1909](https://github.com/Moosa-1909)

*Created as part of the CodeAlpha Internship program.*
