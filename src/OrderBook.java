import java.util.Iterator;
import java.util.PriorityQueue;

public class OrderBook {

    private PriorityQueue<Order> buyOrders;
    private PriorityQueue<Order> sellOrders;

    public OrderBook() {
        buyOrders = new PriorityQueue<>((o1, o2) -> {
            int priceComparison = Integer.compare(o2.price(), o1.price());
            return (priceComparison == 0) ? Long.compare(o1.tsc(), o2.tsc())
                    : priceComparison;
        });

        sellOrders = new PriorityQueue<>((o1, o2) -> {
            int priceComparison = Integer.compare(o1.price(), o2.price());
            return (priceComparison == 0) ? Long.compare(o1.tsc(), o2.tsc())
                    : priceComparison;
        });
    }

    public void submit(Order order) {
        if (order.isBuy()) {
            buyOrders.add(order);
        } else {
            sellOrders.add(order);
        }
        match();
    }

    /**
     * Check top of priority queues until we can produce anymore trades.
     */
    private void match() {
        Order buyRoot;
        Order sellRoot;
        while (!buyOrders.isEmpty() && !sellOrders.isEmpty() &&
                (buyRoot = buyOrders.peek()).price() >= (sellRoot =
                        sellOrders.peek()).price()) {

            // Determine volume to trade.
            int buyVolume = buyRoot.getVolume();
            int sellVolume = sellRoot.getVolume();
            int c = Integer.compare(buyVolume, sellVolume);
            int tradeVolume = Math.min(buyVolume, sellVolume);

            buyRoot.decrementVolume(tradeVolume);
            sellRoot.decrementVolume(tradeVolume);

            // Track trading partners.
            buyRoot.bookkeepTradePartner(sellRoot.id(), tradeVolume);
            sellRoot.bookkeepTradePartner(buyRoot.id(), tradeVolume);

            // Eliminate filled orders or refresh icebergs.
            checkOrderVolume(buyRoot, sellRoot, c);
            checkOrderVolume(sellRoot, buyRoot, c);
        }
        // Display trade results.
        render();
    }

    private void checkOrderVolume(Order order, Order partner, int c) {
        // Invariant: order cannot be null since we peeked it from
        // a non-empty queue.
        PriorityQueue<Order> orders = (order.isBuy()) ? buyOrders :
                sellOrders;
        if (order.getVolume() != 0) {
            if (c <= 0) {
                // Iceberg was exhausted and floated back up.
                // Refresh iceberg timestamp.
                Order root = orders.poll();
                root.setTime(System.nanoTime());
                orders.add(root);
            }
            // There remains volume to be traded in the peak or in the limit
            // order.
        } else {
            // Order was exhausted, log immediate execution and remove from
            // partnerlist in remaining trade. This means only one log per
            // <Buyer,Seller> pair trade.
            partner.removePartnerFromLog(order.id());
            orders.poll().logFill();
        }
    }

    /**
     * Logging.
     */
    public void render() {
        String header = "+-----------------------------------------------------------------+\n" +
                "| BUY                            | SELL                           |\n" +
                "| Id       | Volume      | Price | Price | Volume      | Id       |\n" +
                "+----------+-------------+-------+-------+-------------+----------+\n";
        String footer =
                "+----------+-------------+-------+-------+-------------+----------+\n";
        System.out.print(header);

        Iterator<Order> buyOrdersIt = buyOrders.iterator();
        Iterator<Order> sellOrdersIt = sellOrders.iterator();
        while (buyOrdersIt.hasNext() || sellOrdersIt.hasNext()) {
            Order b = (buyOrdersIt.hasNext()) ? buyOrdersIt.next() : null;
            Order s = (sellOrdersIt.hasNext()) ? sellOrdersIt.next() :
                    null;
            renderOrderPair(b, s);
        }
        System.out.println(footer);
    }

    private void renderOrderPair(Order b, Order s) {
        assert (b != null || s != null);
        if (b != null) {
            System.out.printf("|%10d|%,13d|%,7d|", b.id(), b.getVolume(),
                    b.price());
        } else {
            System.out.printf("|%10c|%13c|%7c|", ' ', ' ', ' ');
        }

        if (s != null) {
            System.out.printf("%,7d|%,13d|%10d|", s.price(), s.getVolume(),
                    s.id());
        } else {
            System.out.printf("%7c|%13c|%10c|", ' ', ' ', ' ');
        }
        System.out.println();
    }

}
