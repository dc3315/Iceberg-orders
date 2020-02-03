import java.util.Iterator;
import java.util.PriorityQueue;

public class OrderBook {

    private PriorityQueue<LimitOrder> buyOrders;
    private PriorityQueue<LimitOrder> sellOrders;

    public OrderBook() {
        buyOrders = new PriorityQueue<>((o1, o2) -> {
            int priceComparison = Integer.compare(o2.price, o1.price);
            return (priceComparison == 0) ? Long.compare(o1.tsc, o2.tsc)
                    : priceComparison;
        });

        sellOrders = new PriorityQueue<>((o1, o2) -> {
            int priceComparison = Integer.compare(o1.price, o2.price);
            return (priceComparison == 0) ? Long.compare(o1.tsc, o2.tsc)
                    : priceComparison;
        });
    }


    public void render() {
        String header = "+-----------------------------------------------------------------+\n" +
                "| BUY                            | SELL                           |\n" +
                "| Id       | Volume      | Price | Price | Volume      | Id       |\n" +
                "+----------+-------------+-------+-------+-------------+----------+\n";
        String footer =
                "+----------+-------------+-------+-------+-------------+----------+\n";
        System.out.print(header);

        Iterator<LimitOrder> buyOrdersIt = buyOrders.iterator();
        Iterator<LimitOrder> sellOrdersIt = sellOrders.iterator();
        while (buyOrdersIt.hasNext() || sellOrdersIt.hasNext()) {
            LimitOrder b = (buyOrdersIt.hasNext()) ? buyOrdersIt.next() : null;
            LimitOrder s = (sellOrdersIt.hasNext()) ? sellOrdersIt.next() :
                    null;
            renderOrderPair(b, s);
        }
        System.out.println(footer);
    }

    private void renderOrderPair(LimitOrder b, LimitOrder s) {
        assert (b != null || s != null);
        if (b != null) {
            System.out.printf("|%10d|%,13d|%,7d|", b.id, b.volume, b.price);
        } else {
            System.out.printf("|%10c|%13c|%7c|", ' ', ' ', ' ');
        }

        if (s != null) {
            System.out.printf("%,7d|%,13d|%10d|", s.price, s.volume, s.id);
        } else {
            System.out.printf("%7c|%13c|%10c|", ' ', ' ', ' ');
        }
        System.out.println();
    }

    public void submit(LimitOrder order) {
        if (order.isBuy) {
            buyOrders.add(order);
        } else {
            sellOrders.add(order);
        }
    }


    public void match(LimitOrder order) {
        while (!buyOrders.isEmpty() && !sellOrders.isEmpty() &&
                buyOrders.peek().price >= sellOrders.peek().price) {
            LimitOrder buyOrder = buyOrders.poll();
            LimitOrder sellOrder = sellOrders.poll();

            int buyOrderPeakVolume = buyOrder.getVolume();
            int sellOrderPeakVolume = sellOrder.getVolume();

            int minVolume = Math.min(buyOrderPeakVolume,
                    sellOrderPeakVolume);

            // decrement minvolume

            if (buyOrder.getFullVolume() > 0) {

            }

        }
    }

}
