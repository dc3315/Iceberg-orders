public class LimitOrder {

    boolean isBuy;
    long tsc;
    int id;
    short price;
    int volume;

    public LimitOrder(boolean isBuy, int id, short price, int volume) {
        this.isBuy = isBuy;
        this.tsc = System.nanoTime();
        this.id = id;
        this.price = price;
        this.volume = volume;
    }

    public void decrementVolume() {

    }

    public void fill() {

    }

    public void refreshPeak() {
    }

    public void decrementPeak(int v) {
        volume -= v;
    }

    public int getFullVolume() {
        return volume;
    }

    public int getVolume() {
        return volume;
    }


}
