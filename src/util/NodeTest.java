package util;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the Node class. Tests both constructors and linking
 * nodes together.
 * 
 * @author fisherjc
 * @version Oct 13, 2026
 */
public class NodeTest {

    private Node<String> node;

    /**
     * Sets up a fresh Node instance before each test executes.
     */
    @BeforeEach
    public void setUp() {
        node = new Node<>("A");
    }

    // ~ Constructor Tests .....................................................

    /**
     * Tests that the single-argument constructor has no next node.
     */
    @Test
    public void testConstructor_DataOnly() {
        assertEquals("A", node.getData());
        assertNull(node.getNext());
    }

    /**
     * Tests that the two-argument constructor links to the given node.
     */
    @Test
    public void testConstructor_WithNext() {
        Node<String> first = new Node<>("B", node);

        assertEquals("B", first.getData());
        assertSame(node, first.getNext());
    }

    // ~ Linking Tests .........................................................

    /**
     * Tests setNext with a node and with null.
     */
    @Test
    public void testSetNext() {
        Node<String> next = new Node<>("B");
        node.setNext(next);
        assertSame(next, node.getNext());

        node.setNext(null);
        assertNull(node.getNext());
    }
}
