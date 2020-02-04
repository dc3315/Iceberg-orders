public class IcebergOrder extends LimitOrder {

    int peakSize;
    int peak;

    public IcebergOrder(boolean buy, int id, short price, int volume,
                        int peakSize) {
        super(buy, id, price, volume);
        this.peakSize = peakSize;
        this.peak = peakSize;
        this.volume -= peakSize; // hidden volume
    }


    // Only ever returns peak volume.
    @Override
    public int getVolume() {
        return peak;
    }

    public void fill() { }


    public void decrementVolume(int v) {
        assert (v <= peak); // trading system can never see more than the peak
        // so cannot decrement by more than peak.
        peak -= v;
        if (peak == 0) {
            // iceberg always floats up when there is no peak left
            refreshPeak();
        }
    }

    // Float iceberg up
    private void refreshPeak() {
        peak = Math.min(peakSize, volume);
        volume -= peak;
    }
}
