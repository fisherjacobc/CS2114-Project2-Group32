package simulation.items;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the GenericItem class. Tests the prohibited flag, the
 * reason message, and the inherited name and toString behavior.
 * 
 * @author fisherjc
 * @version Oct 20, 2026
 */
public class GenericItemTest {

    private GenericItem allowed;
    private GenericItem prohibited;

    /**
     * Sets up fresh GenericItem instances before each test executes.
     */
    @BeforeEach
    public void setUp() {
        allowed = new GenericItem("Laptop", false);
        prohibited = new GenericItem("Fireworks", true);
    }

    // ~ Accessor Tests ........................................................

    /**
     * Tests that the constructor stores the name.
     */
    @Test
    public void testGetName() {
        assertEquals("Laptop", allowed.getName());
        assertEquals("Fireworks", prohibited.getName());
    }

    // ~ isProhibited Tests ....................................................

    /**
     * Tests that isProhibited reflects the constructor flag.
     */
    @Test
    public void testIsProhibited() {
        assertFalse(allowed.isProhibited());
        assertTrue(prohibited.isProhibited());
    }

    /**
     * Tests that a generic item is not flagged based on its name alone.
     */
    @Test
    public void testIsProhibited_IgnoresName() {
        assertFalse(new GenericItem("Metal Weapon", false).isProhibited());
    }

    // ~ getReason Tests .......................................................

    /**
     * Tests the reason message for allowed and prohibited items.
     */
    @Test
    public void testGetReason() {
        assertEquals("This item is allowed", allowed.getReason());
        assertEquals("This item is a prohibited item", prohibited.getReason());
    }

    // ~ toString Tests ........................................................

    /**
     * Tests that toString returns the item name.
     */
    @Test
    public void testToString() {
        assertEquals("Laptop", allowed.toString());
    }
}
