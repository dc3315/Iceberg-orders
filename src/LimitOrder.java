import java.util.HashMap;

public class LimitOrder implements Order {

    private boolean isBuy;
    private long tsc;
    private int id;
    private short price;
    protected int volume;
    private HashMap<Integer, Integer> tradePartners;

    public LimitOrder(boolean isBuy, int id, short price, int volume) {
        this.isBuy = isBuy;
        this.tsc = System.nanoTime();
        this.id = id;
        this.price = price;
        this.volume = volume;
        this.tradePartners = new HashMap<>();
    }

    public void bookkeepTradePartner(int id, int v) {
        int newVolume = tradePartners.getOrDefault(id, 0) + v;
        tradePartners.put(id, newVolume);
    }

    public void logFill() {
        for (int partnerId : tradePartners.keySet()) {
            System.out.printf("%d,%d,%d,%d\n", (isBuy) ? id : partnerId,
                    (isBuy) ? partnerId : id,
                    this.price, tradePartners.get(partnerId));
        }
    }

    @Override
    public void removePartnerFromLog(int partnerId) {
        tradePartners.remove(partnerId);
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

    public int price() {
        return price;
    }

    public long tsc() {
        return tsc;
    }

    public int id() {
        return id;
    }

    public boolean isBuy() {
        return isBuy;
    }
}
