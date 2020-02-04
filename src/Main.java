import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class Main {

    public static final int NUM_LIMIT_ORDER_FIELDS = 4;

    public static void main(String[] args) throws IOException {

        OrderBook book = new OrderBook();

        BufferedReader systemIn =
                new BufferedReader(new InputStreamReader(System.in, StandardCharsets.US_ASCII));

        String line;
        while ((line = systemIn.readLine()) != null) {
            String[] tokens = line.split(",");
            boolean isBuy = tokens[0].charAt(0) == 'B';
            int id = Integer.parseInt(tokens[1]);

            short price = Short.parseShort(tokens[2]);
            int volume = Integer.parseInt(tokens[3]);
            if (tokens.length > NUM_LIMIT_ORDER_FIELDS) {
                // iceberg order
                int peakSize = Integer.parseInt(tokens[4]);
                book.submit(new IcebergOrder(isBuy, id, price, volume,
                        peakSize));
            } else {
                book.submit(new LimitOrder(isBuy, id, price, volume));
            }
        }
    }
}
