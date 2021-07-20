package order;

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
        match(order);
    }

    // Check top of priority queues until we cannot produce anymore trades.
    private void match(Order order) {
        Order bestBuyOffer;
        Order bestSellOffer;
        while (!buyOrders.isEmpty() && !sellOrders.isEmpty()
                && (bestBuyOffer = buyOrders.peek()).getPrice() >= (bestSellOffer = sellOrders.peek()).getPrice()) {
            // Determine volume to trade.
            int buyVolume = bestBuyOffer.getVolume();
            int sellVolume = bestSellOffer.getVolume();
            int c = Integer.compare(buyVolume, sellVolume);
            int tradeVolume = Math.min(buyVolume, sellVolume);

            bestBuyOffer.decrementVolume(tradeVolume);
            bestSellOffer.decrementVolume(tradeVolume);

            // Track trading partners.
            bestBuyOffer.trackTradePartner(bestSellOffer.getId(), tradeVolume);
            bestSellOffer.trackTradePartner(bestBuyOffer.getId(), tradeVolume);

            // Eliminate filled orders or refresh icebergs.
            checkOrderVolume(bestBuyOffer, bestSellOffer, c);
            checkOrderVolume(bestSellOffer, bestBuyOffer, c);
        }
        order.refresh();
        System.out.println(this); // typically you would use a logger here, but for this exercise this was omitted.
    }

    private void checkOrderVolume(Order order, Order partner, int c) {
        PriorityQueue<Order> orders = (order.isBuy()) ? buyOrders : sellOrders;
        if (order.getVolume() != 0) {
            if (c <= 0) {
                // Iceberg was exhausted and floated back up.
                // Refresh iceberg timestamp.
                Order root = orders.poll();
                root.setTime(System.nanoTime());
                orders.add(root);
            }
            // There remains volume to be traded in the peak or in the limit order.
        } else {
            // order.Order was exhausted, log immediate execution and remove from
            // partnerlist in remaining trade. This means only one log per
            // <Buyer,Seller> pair trade.
            partner.untrackTradePartner(order.getId());
            System.out.print(orders.poll());
        }
    }

    @Override
    public String toString() {
        StringBuilder orderBookLog = new StringBuilder();
        String header = "+-----------------------------------------------------------------+\n" +
                "| BUY                            | SELL                           |\n" +
                "| Id       | Volume      | Price | Price | Volume      | Id       |\n" +
                "+----------+-------------+-------+-------+-------------+----------+\n";
        String footer =
                "+----------+-------------+-------+-------+-------------+----------+\n";
        orderBookLog.append(header);

        Iterator<Order> buyOrdersIt = buyOrders.iterator();
        Iterator<Order> sellOrdersIt = sellOrders.iterator();
        while (buyOrdersIt.hasNext() || sellOrdersIt.hasNext()) {
            Order b = (buyOrdersIt.hasNext()) ? buyOrdersIt.next() : null;
            Order s = (sellOrdersIt.hasNext()) ? sellOrdersIt.next() : null;
            orderBookLog.append(renderOrderPair(b, s));
        }
        orderBookLog.append(footer);
        return orderBookLog.toString();
    }

    private String renderOrderPair(Order b, Order s) {
        String orderPair = "";
        assert (b != null || s != null);
        if (b != null) {
            orderPair += String.format("|%10d|%,13d|%,7d|", b.getId(), b.getVolume(), b.getPrice());
        } else {
            orderPair += String.format("|%10c|%13c|%7c|", ' ', ' ', ' ');
        }

        if (s != null) {
            orderPair += String.format("%,7d|%,13d|%10d|", s.getPrice(), s.getVolume(), s.getId());
        } else {
            orderPair += String.format("%7c|%13c|%10c|", ' ', ' ', ' ');
        }
        orderPair += "\n";
        return orderPair;
    }

    public void executeTrades(BufferedReader tradesDataStream) throws IOException {
        String line;
        while ((line = tradesDataStream.readLine()) != null) {
            if (line.trim().length() != 0 && line.indexOf('#') == -1) {
                submit(Order.fromLine(line));
            }
        }
    }
}
