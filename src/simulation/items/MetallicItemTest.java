package simulation.items;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the MetallicItem class. Tests the prohibited flag, the
 * keyword-based name checks, and the reason message.
 * 
 * @author fisherjc
 * @version Oct 20, 2026
 */
public class MetallicItemTest {

    private MetallicItem allowed;
    private MetallicItem prohibited;

    /**
     * Sets up fresh MetallicItem instances before each test executes.
     */
    @BeforeEach
    public void setUp() {
        allowed = new MetallicItem("Belt Buckle", false);
        prohibited = new MetallicItem("Knife", true);
    }

    // ~ Accessor Tests ........................................................

    /**
     * Tests that the constructor stores the name and that it is a
     * GenericItem.
     */
    @Test
    public void testConstructor() {
        assertEquals("Belt Buckle", allowed.getName());
        assertTrue(allowed instanceof GenericItem);
    }

    // ~ isProhibited Tests ....................................................

    /**
     * Tests that isProhibited reflects the constructor flag.
     */
    @Test
    public void testIsProhibited_Flag() {
        assertFalse(allowed.isProhibited());
        assertTrue(prohibited.isProhibited());
    }

    /**
     * Tests that each prohibited keyword in the name flags the item, and that
     * "metal" alone does not.
     */
    @Test
    public void testIsProhibited_Keywords() {
        assertFalse(new MetallicItem("Metal Pipe", false).isProhibited());
        assertTrue(new MetallicItem("Toy Weapon", false).isProhibited());
        assertTrue(new MetallicItem("Prohibited Tool", false).isProhibited());
        assertTrue(new MetallicItem("Spare Battery", false).isProhibited());
    }

    /**
     * Tests that the keyword check ignores case and matches substrings.
     */
    @Test
    public void testIsProhibited_KeywordCaseAndSubstring() {
        assertTrue(new MetallicItem("WEAPON", false).isProhibited());
        assertTrue(new MetallicItem("Weaponized Drone", false).isProhibited());
        assertTrue(new MetallicItem("BaTtErY pack", false).isProhibited());
    }

    // ~ getReason Tests .......................................................

    /**
     * Tests the reason message for allowed and prohibited items.
     */
    @Test
    public void testGetReason() {
        assertEquals("This item is allowed", allowed.getReason());
        assertEquals("This item is a prohibited item", prohibited.getReason());
        assertEquals("This item is a prohibited item",
                new MetallicItem("Battery", false).getReason());
    }

    // ~ toString Tests ........................................................

    /**
     * Tests that toString returns the item name.
     */
    @Test
    public void testToString() {
        assertEquals("Knife", prohibited.toString());
    }
}
