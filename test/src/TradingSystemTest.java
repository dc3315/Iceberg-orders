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

     // NB: Should really use a loop to unroll set of <Input,ExpectedOutput,
     // ActualOutput> tests. In this case since there are few we leave as is.

    @org.junit.Test
    public void testExampleTestCase() throws IOException {
        Main.main(new String[]{"test/input/ExampleTestCase.txt"});
        String expectedOutput =
                new String(Files.readAllBytes(new File("test/expected/ExampleTestCase.txt").toPath()));
        String actualOutput = outContent.toString();
        assertEquals(expectedOutput, actualOutput);
    }

    @org.junit.Test
    public void testPassiveExecutionTestCaseA() throws IOException {
        Main.main(new String[]{"test/input/PassiveExecutionTestCaseA.txt"});
        String expectedOutput =
                new String(Files.readAllBytes(new File("test/expected/PassiveExecutionTestCaseA.txt").toPath()));
        String actualOutput = outContent.toString();
        assertEquals(expectedOutput, actualOutput);
    }

    @org.junit.Test
    public void testPassiveExecutionTestCaseB() throws IOException {
        Main.main(new String[]{"test/input/PassiveExecutionTestCaseB.txt"});
        String expectedOutput =
                new String(Files.readAllBytes(new File("test/expected/PassiveExecutionTestCaseB.txt").toPath()));
        String actualOutput = outContent.toString();
        assertEquals(expectedOutput, actualOutput);
    }

    @org.junit.Test
    public void testPassiveExecutionTestCaseC() throws IOException {
        Main.main(new String[]{"test/input/PassiveExecutionTestCaseC.txt"});
        String expectedOutput =
                new String(Files.readAllBytes(new File("test/expected" +
                        "/PassiveExecutionTestCaseC.txt").toPath()));
        String actualOutput = outContent.toString();
        assertEquals(expectedOutput, actualOutput);
    }

    @org.junit.Test
    public void testAggressiveEntryTestCase() throws IOException {
        Main.main(new String[]{"test/input/AggressiveEntryTestCase.txt"});
        String expectedOutput =
                new String(Files.readAllBytes(new File("test/expected" +
                        "/AggressiveEntryTestCase.txt").toPath()));
        String actualOutput = outContent.toString();
        assertEquals(expectedOutput, actualOutput);
    }
}