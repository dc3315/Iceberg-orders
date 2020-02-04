public class IcebergOrder extends LimitOrder {

    private int peakSize;
    private int peak;

    public IcebergOrder(boolean buy, int id, short price, int volume,
                        int peakSize) {
        super(buy, id, price, volume);
        this.peakSize = peakSize;
        this.peak = peakSize;
        this.volume -= peakSize;
    }

    // Only ever returns peak volume.
    @Override
    public int getVolume() {
        return peak;
    }


    @Override
    public void decrementVolume(int v) {
        assert (v <= peak); // trading system can never see more than the peak
        // so decrementing by more than peak should never happen.
        peak -= v;
        if (peak == 0) {
            // iceberg floats up when there is no peak left
            floatUp();
        }
    }

    // Float iceberg up
    private void floatUp() {
        peak = Math.min(peakSize, volume);
        volume -= peak;
    }
}
