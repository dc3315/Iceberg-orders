public class LimitOrder {

    boolean buy;
    long tsc;
    int id;
    short price;
    int quantity;

    public LimitOrder(boolean buy, int id, short price, int quantity) {
        this.buy = buy;
        this.tsc = System.nanoTime();
        this.id = id;
        this.price = price;
        this.quantity = quantity;
    }

}
