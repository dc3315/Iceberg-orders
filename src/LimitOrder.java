public class LimitOrder {

    private long tsc;
    private int id;
    private short price;
    private int quantity;

    public LimitOrder(boolean buy, int id, short price, int quantity) {
        this.tsc = System.nanoTime();
        this.id = id;
        this.price = price;
        this.quantity = quantity;
    }

}
