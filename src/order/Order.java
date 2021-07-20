package order;

public interface Order {

    static Order fromLine(String line) {
        String[] tokens = line.split(",");
        boolean isBuy = tokens[0].charAt(0) == 'B';
        int id = Integer.parseInt(tokens[1]);
        short price = Short.parseShort(tokens[2]);
        int volume = Integer.parseInt(tokens[3]);
        if (tokens.length > 4) {
            int peakSize = Integer.parseInt(tokens[4]);
            return new IcebergOrder(isBuy, id, price, volume, peakSize);
        } else {
            return new LimitOrder(isBuy, id, price, volume);
        }
    }

    int getVolume();

    void decrementVolume(int v);

    int getPrice();

    long getTimeStamp();

    int getId();

    void trackTradePartner(int id, int v);

    void untrackTradePartner(int partnerId);

    boolean isBuy();

    void setTime(long timestamp);

    default void refresh() {}
}
