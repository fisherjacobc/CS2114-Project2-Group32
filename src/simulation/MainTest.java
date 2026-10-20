package simulation;

import static org.junit.jupiter.api.Assertions.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the Main class. Tests the main menu loop using
 * simulated console input.
 * 
 * @author fisherjc
 * @version Oct 13, 2026
 */
public class MainTest {

    private InputStream originalIn;
    private PrintStream originalOut;
    private ByteArrayOutputStream output;

    /**
     * Redirects System.out before each test executes.
     */
    @BeforeEach
    public void setUp() {
        originalIn = System.in;
        originalOut = System.out;
        output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
    }

    /**
     * Restores System.in and System.out after each test executes.
     */
    @AfterEach
    public void tearDown() {
        System.setIn(originalIn);
        System.setOut(originalOut);
    }

    /**
     * Feeds the given text to System.in.
     * 
     * @param input the simulated console input
     */
    private void provideInput(String input) {
        System.setIn(new ByteArrayInputStream(input.getBytes()));
    }

    // ~ Menu Tests ............................................................

    /**
     * Tests that choosing D ends the simulation immediately.
     */
    @Test
    public void testMain_EndSimulation() {
        provideInput("D\n");
        Main.main(new String[]{});

        assertTrue(output.toString().contains("CS International Airport"));
    }

    /**
     * Tests that invalid input prints an error and the menu repeats.
     */
    @Test
    public void testMain_InvalidInput() {
        provideInput("Z\n\nd\n");
        Main.main(new String[]{});

        assertTrue(output.toString().contains("Invalid Input!"));
    }

    /**
     * Tests adding random passengers before ending the simulation.
     */
    @Test
    public void testMain_AddRandomPassenger() {
        provideInput("A\n\n a \n\nD\n");
        Main.main(new String[]{});

        assertFalse(output.toString().contains("Invalid Input!"));
    }
}
