package simulation.items;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the LiquidItem class. Tests the fluid ounce limit, the
 * reason message, and toString.
 * 
 * @author fisherjc
 * @version Oct 20, 2026
 */
public class LiquidItemTest {

    private LiquidItem allowed;
    private LiquidItem prohibited;

    /**
     * Sets up fresh LiquidItem instances before each test executes.
     */
    @BeforeEach
    public void setUp() {
        allowed = new LiquidItem("Shampoo", 2.0);
        prohibited = new LiquidItem("Water Bottle", 16.9);
    }

    // ~ Accessor Tests ........................................................

    /**
     * Tests that the constructor stores the name.
     */
    @Test
    public void testGetName() {
        assertEquals("Shampoo", allowed.getName());
    }

    // ~ isProhibited Tests ....................................................

    /**
     * Tests liquids under and over the limit.
     */
    @Test
    public void testIsProhibited() {
        assertFalse(allowed.isProhibited());
        assertTrue(prohibited.isProhibited());
    }

    /**
     * Tests liquids at and just past the limit.
     */
    @Test
    public void testIsProhibited_Boundary() {
        assertFalse(new LiquidItem("Lotion", LiquidItem.MAX_FLUID_OZ).isProhibited());
        assertTrue(new LiquidItem("Lotion", 3.5).isProhibited());
        assertFalse(new LiquidItem("Empty Bottle", 0.0).isProhibited());
    }

    // ~ getReason Tests .......................................................

    /**
     * Tests the reason message for allowed and prohibited liquids.
     */
    @Test
    public void testGetReason() {
        assertEquals("This liquid is allowed", allowed.getReason());
        assertEquals("This liquid is over the allowed fluid ounces amount: "
                + "16.9 > 3.4", prohibited.getReason());
    }

    // ~ toString Tests ........................................................

    /**
     * Tests that toString includes the name and fluid ounces.
     */
    @Test
    public void testToString() {
        assertEquals("Shampoo[2.0oz]", allowed.toString());
        assertEquals("Water Bottle[16.9oz]", prohibited.toString());
    }
}
