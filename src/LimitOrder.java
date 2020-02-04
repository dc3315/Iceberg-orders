import java.util.HashMap;

public class LimitOrder {

    boolean isBuy;
    long tsc;
    int id;
    short price;
    int volume;
    HashMap<Integer, Integer> tradePartners;

    public LimitOrder(boolean isBuy, int id, short price, int volume) {
        this.isBuy = isBuy;
        this.tsc = System.nanoTime();
        this.id = id;
        this.price = price;
        this.volume = volume;
        this.tradePartners = new HashMap<>();
    }

    public void addTradePartner(int id, int v) {
        int newVolume = tradePartners.getOrDefault(id, 0) + v;
        tradePartners.put(id, newVolume);
    }

    public void fill() {
        for (int partnerId : tradePartners.keySet()) {
            System.out.printf("%d,%d,%d,%d\n", (isBuy) ? this.id : partnerId,
                    (isBuy) ? partnerId : this.id,
                    this.price, tradePartners.get(partnerId));
        }
    }

    public void decrementVolume(int v) {
        assert (v <= volume);
        volume -= v;
    }


    public int getVolume() {
        return volume;
    }


    public void setTime(long nanoTime) {
        this.tsc = nanoTime;
    }
}
