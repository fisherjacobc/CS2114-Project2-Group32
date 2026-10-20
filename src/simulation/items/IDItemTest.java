package simulation.items;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import simulation.Passenger;

/**
 * Unit tests for the IDItem class. Tests real and fake IDs, the reason
 * message, and toString.
 * 
 * @author fisherjc
 * @version Oct 20, 2026
 */
public class IDItemTest {

    // Enough repetitions to cover every random first letter
    private static final int TRIALS = 200;

    private Passenger passenger;
    private IDItem realID;
    private IDItem fakeID;

    /**
     * Sets up a passenger and fresh IDItem instances before each test
     * executes.
     */
    @BeforeEach
    public void setUp() {
        passenger = new Passenger("Clark Kent", 303, false, false, null);
        realID = new IDItem("Driver License", passenger, true);
        fakeID = new IDItem("Driver License", passenger, false);
    }

    // ~ Accessor Tests ........................................................

    /**
     * Tests that the constructor stores the name.
     */
    @Test
    public void testGetName() {
        assertEquals("Driver License", realID.getName());
    }

    // ~ isProhibited Tests ....................................................

    /**
     * Tests that a real ID is always allowed.
     */
    @Test
    public void testIsProhibited_RealID() {
        for (int i = 0; i < TRIALS; i++) {
            IDItem id = new IDItem("Passport", passenger, true);
            assertFalse(id.isProhibited(), id.toString());
        }
    }

    /**
     * Tests that a fake ID is always prohibited.
     */
    @Test
    public void testIsProhibited_FakeID() {
        for (int i = 0; i < TRIALS; i++) {
            IDItem id = new IDItem("Passport", passenger, false);
            assertTrue(id.isProhibited(), id.toString());
        }
    }

    // ~ getReason Tests .......................................................

    /**
     * Tests the reason message for real and fake IDs.
     */
    @Test
    public void testGetReason() {
        assertEquals("This is a real and allowed ID", realID.getReason());
        assertEquals("This is a fake ID", fakeID.getReason());
    }

    // ~ toString Tests ........................................................

    /**
     * Tests that toString includes the name and an ID number derived from
     * the passenger's name.
     */
    @Test
    public void testToString() {
        String expectedSuffix = passenger.getName().hashCode() + "]";
        String str = realID.toString();

        assertTrue(str.startsWith("Driver License["));
        assertTrue(str.endsWith(expectedSuffix));
    }
}
