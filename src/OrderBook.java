import java.io.BufferedReader;
import java.io.IOException;
import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;

public class OrderBook {
    private final PriorityQueue<Order> buyOrders = new PriorityQueue<>(
            Comparator.comparing(Order::getPrice).reversed().thenComparing(Order::getTimeStamp));
    private final PriorityQueue<Order> sellOrders = new PriorityQueue<>(
            Comparator.comparing(Order::getPrice).thenComparing(Order::getTimeStamp));

    public void submit(Order order) {
        if (order.isBuy()) {
            buyOrders.add(order);
        } else {
            sellOrders.add(order);
        }
        match(order instanceof IcebergOrder, order);
        // bit of a pity for instanceof usage. Would be curious about neater
        // solution.
    }

    /**
     * Check top of priority queues until we cannot produce anymore trades.
     */
    private void match(boolean aggressiveEntry, Order order) {
        Order buyRoot;
        Order sellRoot;
        int i = 0;
        while (!buyOrders.isEmpty() && !sellOrders.isEmpty() &&
                (buyRoot = buyOrders.peek()).getPrice() >= (sellRoot =
                        sellOrders.peek()).getPrice()) {
            if (aggressiveEntry && i == 0) {
                // if the order reached top of one of priority queues
                // it succeeded in aggressively entering.
                // When matching is complete we need to float the iceberg up
                // before rendering according to documentation.
                aggressiveEntry = (buyRoot == order || sellRoot == order);
            }
            // Determine volume to trade.
            int buyVolume = buyRoot.getVolume();
            int sellVolume = sellRoot.getVolume();
            int c = Integer.compare(buyVolume, sellVolume);
            int tradeVolume = Math.min(buyVolume, sellVolume);

            buyRoot.decrementVolume(tradeVolume);
            sellRoot.decrementVolume(tradeVolume);

            // Track trading partners.
            buyRoot.trackTradePartner(sellRoot.getId(), tradeVolume);
            sellRoot.trackTradePartner(buyRoot.getId(), tradeVolume);

            // Eliminate filled orders or refresh icebergs.
            checkOrderVolume(buyRoot, sellRoot, c);
            checkOrderVolume(sellRoot, buyRoot, c);
            i++;
        }

        if (aggressiveEntry) {
            // The aggressive order matched and needs to be floated up.
            // If the order was already filled there will be a no-op.
            order.refresh();
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
            partner.untrackTradePartner(order.getId());
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
            System.out.printf("|%10d|%,13d|%,7d|", b.getId(), b.getVolume(),
                    b.getPrice());
        } else {
            System.out.printf("|%10c|%13c|%7c|", ' ', ' ', ' ');
        }

        if (s != null) {
            System.out.printf("%,7d|%,13d|%10d|", s.getPrice(), s.getVolume(),
                    s.getId());
        } else {
            System.out.printf("%7c|%13c|%10c|", ' ', ' ', ' ');
        }
        System.out.println();
    }

    public void executeTrades(BufferedReader tradesDataStream) throws IOException {
        String line;
        while ((line = tradesDataStream.readLine()) != null) {
            submit(Order.fromLine(line));
        }
    }
}
