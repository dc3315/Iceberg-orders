public interface Order {

    int getVolume();

    void decrementVolume(int v);

    int price();

    long tsc();

    int id();

    void bookkeepTradePartner(int id, int v);

    void removePartnerFromLog(int partnerId);

    boolean isBuy();

    void setTime(long tsc);

    void logFill();

}
