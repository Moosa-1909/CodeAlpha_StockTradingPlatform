import java.time.LocalDateTime;

/** Portfolio value at a point in time, used for performance tracking. */
public class Snapshot {
    private final LocalDateTime time;
    private final double value;

    public Snapshot(LocalDateTime time, double value) {
        this.time = time;
        this.value = value;
    }

    public LocalDateTime getTime() { return time; }
    public double getValue() { return value; }
}
