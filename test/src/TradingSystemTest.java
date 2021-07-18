import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.runners.Parameterized;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.util.stream.Stream;

public class TradingSystemTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    static Stream<Arguments> inputs() {
        return Stream.of(
                Arguments.of("input/ExampleTestCase.txt", "expected/ExampleTestCase.txt"),
                Arguments.of("input/PassiveExecutionTestCaseB.txt", "expected/PassiveExecutionTestCaseB.txt"),
                Arguments.of("input/PassiveExecutionTestCaseC.txt", "expected/PassiveExecutionTestCaseC.txt"),
                Arguments.of("input/AggressiveEntryTestCase.txt", "expected/AggressiveEntryTestCase.txt"),
                Arguments.of("input/PassiveExecutionTestCaseA.txt", "expected/PassiveExecutionTestCaseA.txt"));
    }

    @ParameterizedTest
    @MethodSource("inputs")
    public void testExampleTestCase(String inputData, String expectedData) throws IOException {
        System.setOut(new PrintStream(outContent));
        Main.main(new String[]{inputData});
        String expectedOutput =
                new String(Files.readAllBytes(new File(expectedData).toPath()));
        String actualOutput = outContent.toString();
        Assertions.assertEquals(expectedOutput, actualOutput);
        System.setOut(originalOut);
    }
}