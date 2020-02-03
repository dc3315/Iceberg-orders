public class IcebergOrder extends LimitOrder {

    private int peakSize;
    private int peak;

    public IcebergOrder(boolean buy, int id, short price, int volume,
                        int peakSize) {
        super(buy, id, price, volume);
        this.peakSize = peakSize;
        peak = peakSize;
        this.volume -= peakSize; // hidden volume
    }


    // Only ever returns peak volume.
    @Override
    public int getVolume() {
        return peak;
    }

    @Override
    public void decrement(int v) {

        refreshPeak();
    }

    // peak floats up.
    @Override
    public void refreshPeak() {
        peak = Math.min(peakSize, volume);
        volume -= peak;
    }
}
