package simulation;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the Person class. Tests accessors, mutators, toString,
 * and equality through concrete subclasses.
 * 
 * @author fisherjc
 * @version Oct 13, 2026
 */
public class PersonTest {

    private Person person;

    /**
     * Sets up a fresh Person instance before each test executes.
     */
    @BeforeEach
    public void setUp() {
        person = new Passenger("Alice Johnson", 101, false, false, new String[]{"Bag"});
    }

    // ~ Accessor & Mutator Tests ..............................................

    /**
     * Tests that the constructor stores the name and flight number.
     */
    @Test
    public void testGetters() {
        assertEquals("Alice Johnson", person.getName());
        assertEquals(101, person.getFlightNumber());
    }

    /**
     * Tests setName and setFlightNumber.
     */
    @Test
    public void testSetters() {
        person.setName("Bob Martinez");
        person.setFlightNumber(202);

        assertEquals("Bob Martinez", person.getName());
        assertEquals(202, person.getFlightNumber());
    }

    // ~ toString Tests ........................................................

    /**
     * Tests that toString begins with the name and flight number.
     */
    @Test
    public void testToString() {
        assertTrue(person.toString().startsWith("Name: Alice Johnson, Flight #: 101"));
    }

    // ~ equals Tests ..........................................................

    /**
     * Tests equals against itself, null, and an unrelated type.
     */
    @Test
    public void testEquals_SameNullAndOtherType() {
        assertEquals(person, person);
        assertNotEquals(person, null);
        assertNotEquals(person, "Alice Johnson");
    }

    /**
     * Tests that equals ignores name case and non-Person fields.
     */
    @Test
    public void testEquals_MatchingNameAndFlight() {
        Passenger other = new Passenger("ALICE JOHNSON", 101, true, true, null);
        assertEquals(person, other);
    }

    /**
     * Tests that equals fails when the name or flight number differs.
     */
    @Test
    public void testEquals_DifferentNameOrFlight() {
        assertNotEquals(person, new Passenger("Alice Smith", 101, false, false, null));
        assertNotEquals(person, new Passenger("Alice Johnson", 999, false, false, null));
    }

    /**
     * Tests that a Passenger and Crew with the same details are equal.
     */
    @Test
    public void testEquals_DifferentSubclass() {
        Crew crew = new Crew("Alice Johnson", 101, "Emp001", true);
        assertEquals(person, crew);
        assertEquals(crew, person);
    }
}
