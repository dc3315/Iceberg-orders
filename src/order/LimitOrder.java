package order;

import java.util.HashMap;

class LimitOrder implements Order {

    private final boolean isBuy;
    private long timeStamp;
    private final int id;
    private final short price;
    int volume;
    private final HashMap<Integer, Integer> tradePartners = new HashMap<>();

    LimitOrder(boolean isBuy, int id, short price, int volume) {
        this.isBuy = isBuy;
        this.timeStamp = System.nanoTime();
        this.id = id;
        this.price = price;
        this.volume = volume;
    }

    @Override
    public void trackTradePartner(int id, int v) {
        int newVolume = tradePartners.getOrDefault(id, 0) + v;
        tradePartners.put(id, newVolume);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        tradePartners.keySet()
                .forEach(partnerId -> {
                    sb.append(String.format("%d,%d,%d,%d",
                            (isBuy) ? id : partnerId,
                            (isBuy) ? partnerId : id,
                            this.price, tradePartners.get(partnerId)));
                    sb.append(System.getProperty("line.separator"));
                });
        return sb.toString();
    }

    @Override
    public void untrackTradePartner(int partnerId) {
        tradePartners.remove(partnerId);
    }

    @Override
    public void decrementVolume(int v) {
        assert (v <= volume);
        volume -= v;
    }

    @Override
    public int getVolume() {
        return volume;
    }

    @Override
    public void setTime(long timestamp) {
        this.timeStamp = timestamp;
    }

    @Override
    public int getPrice() {
        return price;
    }

    @Override
    public long getTimeStamp() {
        return timeStamp;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public boolean isBuy() {
        return isBuy;
    }
}
