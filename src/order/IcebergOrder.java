package order;

class IcebergOrder extends LimitOrder {

    private final int peakSize;
    private int peak;

    IcebergOrder(boolean buy, int id, short price, int volume, int peakSize) {
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
    public void refresh() {
        floatUp();
    }

    @Override
    public void decrementVolume(int v) {
        assert (v <= peak); // typically you would use something like Preconditions.checkState() here.
        // trading system can never see more than the peak
        // so decrementing by more than peak should never happen.
        peak -= v;
        if (peak == 0) {
            // automatically float iceberg up when no peak is left.
            floatUp();
        }
    }

    private void floatUp() {
        int toFloatUp = Math.min(volume, peakSize - peak);
        peak += toFloatUp;
        volume -= toFloatUp;
    }
}
