public class IcebergOrder extends LimitOrder {

    private int peakSize;

    public IcebergOrder(boolean buy, int id, short price, int quantity,
                        int peakSize) {
        super(buy, id, price, quantity);
        this.peakSize = peakSize;
    }
}
