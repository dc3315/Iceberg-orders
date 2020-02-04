import org.junit.After;
import org.junit.Before;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;

import static org.junit.Assert.assertEquals;

public class TradingSystemTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    @org.junit.Test
    public void test4_2_0() throws IOException {
        Main.main(new String[]{"test/input/4.2.0.txt"});
        String expectedOutput =
                new String(Files.readAllBytes(new File("test/expected/4.2.0.txt").toPath()));
        String actualOutput = outContent.toString();
        assertEquals(expectedOutput, actualOutput);
    }

    @org.junit.Test
    public void test4_2_3_2A() throws IOException {
        Main.main(new String[]{"test/input/4.2.3.2a.txt"});
        String expectedOutput =
                new String(Files.readAllBytes(new File("test/expected/4.2.3.2a.txt").toPath()));
        String actualOutput = outContent.toString();
        assertEquals(expectedOutput, actualOutput);
    }

    @org.junit.Test
    public void test4_2_3_2B() throws IOException {
        Main.main(new String[]{"test/input/4.2.3.2b.txt"});
        String expectedOutput =
                new String(Files.readAllBytes(new File("test/expected/4.2.3.2b.txt").toPath()));
        String actualOutput = outContent.toString();
        assertEquals(expectedOutput, actualOutput);
    }

    @org.junit.Test
    public void test4_2_3_2C() throws IOException {
        Main.main(new String[]{"test/input/4.2.3.2c.txt"});
        String expectedOutput =
                new String(Files.readAllBytes(new File("test/expected/4.2.3" +
                        ".2c.txt").toPath()));
        String actualOutput = outContent.toString();
        assertEquals(expectedOutput, actualOutput);
    }
}