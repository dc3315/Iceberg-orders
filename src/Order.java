public interface Order {

    int getVolume();

    void decrementVolume(int v);

    int price();

    long tsc();

    int id();

    void trackTradePartner(int id, int v);

    void untrackTradePartner(int partnerId);

    boolean isBuy();

    void setTime(long tsc);

    void logFill();

    void refresh();
}
