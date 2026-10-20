package util;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the LinkedChain class. Tests adding, searching,
 * removing, and string representation of the chain.
 *
 * @author fisherjc
 * @version Oct 13, 2026
 */
public class LinkedChainTest {

    private LinkedChain<String> chain;

    /**
     * Sets up an empty LinkedChain instance before each test executes.
     */
    @BeforeEach
    public void setUp() {
        chain = new LinkedChain<>();
    }

    // ~ Add & Size Tests ......................................................

    /**
     * Tests that a new chain is empty.
     */
    @Test
    public void testEmptyChain() {
        assertEquals(0, chain.getCurrentSize());
        assertFalse(chain.contains("A"));
        assertNull(chain.getLastNode());
        assertEquals("", chain.toString());
    }

    /**
     * Tests adding entries increases the size and makes them searchable.
     */
    @Test
    public void testAdd() {
        assertTrue(chain.add("A"));
        assertTrue(chain.add("B"));

        assertEquals(2, chain.getCurrentSize());
        assertTrue(chain.contains("A"));
        assertTrue(chain.contains("B"));
        assertFalse(chain.contains("C"));
    }

    /**
     * Tests that adding null is rejected and leaves the chain unchanged.
     */
    @Test
    public void testAdd_Null() {
        assertFalse(chain.add(null));
        assertEquals(0, chain.getCurrentSize());
    }

    // ~ Search Tests ..........................................................

    /**
     * Tests that getLastNode returns the first entry added.
     */
    @Test
    public void testGetLastNode() {
        chain.add("A");
        chain.add("B");
        chain.add("C");

        Node<String> last = chain.getLastNode();
        assertEquals("A", last.getData());
        assertNull(last.getNext());
    }

    /**
     * Tests getFirstReferenceToValue on an empty chain and with null.
     */
    @Test
    public void testGetFirstReferenceToValue_EmptyOrNull() {
        assertNull(chain.getFirstReferenceToValue("A"));

        chain.add("A");
        assertNull(chain.getFirstReferenceToValue(null));
    }

    /**
     * Tests getFirstReferenceToValue finds entries anywhere in the chain.
     */
    @Test
    public void testGetFirstReferenceToValue_Found() {
        chain.add("A");
        chain.add("B");
        chain.add("C");

        assertEquals("C", chain.getFirstReferenceToValue("C").getData());
        assertEquals("B", chain.getFirstReferenceToValue("B").getData());
        assertEquals("A", chain.getFirstReferenceToValue("A").getData());
        assertNull(chain.getFirstReferenceToValue("D"));
    }

    /**
     * Tests getFirstReferenceToValue on a chain with a single entry.
     */
    @Test
    public void testGetFirstReferenceToValue_SingleEntry() {
        chain.add("A");

        assertNotNull(chain.getFirstReferenceToValue("A"));
    }

    // ~ Remove Tests ..........................................................

    /**
     * Tests remove with null and with an empty chain.
     */
    @Test
    public void testRemove_NullOrEmpty() {
        assertNull(chain.remove(new Node<>("A")));

        chain.add("A");
        assertNull(chain.remove(null));
        assertEquals(1, chain.getCurrentSize());
    }

    /**
     * Tests removing a node that is not in the chain.
     */
    @Test
    public void testRemove_NotInChain() {
        chain.add("A");
        chain.add("B");

        assertNull(chain.remove(new Node<>("A")));
        assertEquals(2, chain.getCurrentSize());
        assertEquals("[B,A]", chain.toString());
    }

    /**
     * Tests removing the head node.
     */
    @Test
    public void testRemove_Head() {
        chain.add("A");
        chain.add("B");

        assertEquals("B", chain.remove(chain.getFirstReferenceToValue("B")));
        assertEquals(1, chain.getCurrentSize());
        assertEquals("[A]", chain.toString());
    }

    /**
     * Tests removing the only node in the chain.
     */
    @Test
    public void testRemove_OnlyNode() {
        chain.add("A");

        assertEquals("A", chain.remove(chain.getLastNode()));
        assertEquals(0, chain.getCurrentSize());
        assertEquals("", chain.toString());
    }

    /**
     * Tests removing a node from the middle and end of the chain.
     */
    @Test
    public void testRemove_MiddleAndEnd() {
        chain.add("A");
        chain.add("B");
        chain.add("C");

        assertEquals("B", chain.remove(chain.getFirstReferenceToValue("B")));
        assertEquals("[C,A]", chain.toString());

        assertEquals("A", chain.remove(chain.getLastNode()));
        assertEquals("[C]", chain.toString());
        assertEquals(1, chain.getCurrentSize());
    }

    /**
     * Tests that removeAll empties the chain.
     */
    @Test
    public void testRemoveAll() {
        chain.add("A");
        chain.add("B");
        chain.removeAll();

        assertEquals(0, chain.getCurrentSize());
        assertFalse(chain.contains("A"));
        assertEquals("", chain.toString());
    }

    // ~ toList Tests ..........................................................

    /**
     * Tests toList returns entries from head to tail, and an empty list for
     * an empty chain.
     */
    @Test
    public void testToList() {
        assertTrue(chain.toList().isEmpty());

        chain.add("A");
        chain.add("B");
        chain.add("C");
        assertEquals(List.of("C", "B", "A"), chain.toList());
    }

    // ~ toString Tests ........................................................

    /**
     * Tests toString lists entries from head to tail.
     */
    @Test
    public void testToString() {
        chain.add("A");
        assertEquals("[A]", chain.toString());

        chain.add("B");
        chain.add("C");
        assertEquals("[C,B,A]", chain.toString());
    }
}
