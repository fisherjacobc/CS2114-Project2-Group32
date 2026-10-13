package simulation;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the Passenger class. Tests PreCheck and secondary
 * screening flags, carried items, and toString.
 * 
 * @author fisherjc
 * @version Oct 13, 2026
 */
public class PassengerTest {

    private Passenger passenger;
    private String[] items;

    /**
     * Sets up a fresh Passenger instance before each test executes.
     */
    @BeforeEach
    public void setUp() {
        items = new String[]{"Laptop", "Bag"};
        passenger = new Passenger("Diana Prince", 606, true, false, items);
    }

    // ~ Accessor & Mutator Tests ..............................................

    /**
     * Tests that the constructor stores every field.
     */
    @Test
    public void testConstructor() {
        assertEquals("Diana Prince", passenger.getName());
        assertEquals(606, passenger.getFlightNumber());
        assertTrue(passenger.isPreCheck());
        assertFalse(passenger.isSecondaryScreening());
        assertSame(items, passenger.getItems());
    }

    /**
     * Tests setPreCheck and setSecondaryScreening.
     */
    @Test
    public void testSetFlags() {
        passenger.setPreCheck(false);
        passenger.setSecondaryScreening(true);

        assertFalse(passenger.isPreCheck());
        assertTrue(passenger.isSecondaryScreening());
    }

    /**
     * Tests setItems with a new array and with null.
     */
    @Test
    public void testSetItems() {
        String[] newItems = {"Knife"};
        passenger.setItems(newItems);
        assertArrayEquals(newItems, passenger.getItems());

        passenger.setItems(null);
        assertNull(passenger.getItems());
    }

    // ~ toString Tests ........................................................

    /**
     * Tests that toString includes the Person details and both flags.
     */
    @Test
    public void testToString() {
        assertEquals("Name: Diana Prince, Flight #: 606 "
                + "[PreCheck: true, Secondary Screening: false]",
                passenger.toString());
    }
}
