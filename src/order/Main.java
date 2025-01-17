package order;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class Main {

    public static void main(String[] args) throws IOException {
        // For testing purposes
        if (args.length > 0) {
            System.setIn(new FileInputStream(args[0]));
        }
        BufferedReader systemIn = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.US_ASCII));
        new OrderBook().executeTrades(systemIn);
    }
}
