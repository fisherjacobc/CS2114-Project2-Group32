package simulation;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the Crew class. Tests employee ID, fit-to-fly status,
 * and toString.
 * 
 * @author fisherjc
 * @version Oct 13, 2026
 */
public class CrewTest {

    private Crew crew;

    /**
     * Sets up a fresh Crew instance before each test executes.
     */
    @BeforeEach
    public void setUp() {
        crew = new Crew("Capt. Smith", 505, "Emp123", true);
    }

    // ~ Accessor & Mutator Tests ..............................................

    /**
     * Tests that the constructor stores every field.
     */
    @Test
    public void testConstructor() {
        assertEquals("Capt. Smith", crew.getName());
        assertEquals(505, crew.getFlightNumber());
        assertEquals("Emp123", crew.getEmployeeID());
        assertTrue(crew.isFitToFly());
    }

    /**
     * Tests setEmployeeID and setFitToFly.
     */
    @Test
    public void testSetters() {
        crew.setEmployeeID("Emp456");
        crew.setFitToFly(false);

        assertEquals("Emp456", crew.getEmployeeID());
        assertFalse(crew.isFitToFly());
    }

    // ~ toString Tests ........................................................

    /**
     * Tests that toString includes the Person details and crew details.
     */
    @Test
    public void testToString() {
        assertEquals("Name: Capt. Smith, Flight #: 505 "
                + "[Crew ID: Emp123, Fit to Fly: true]",
                crew.toString());
    }
}
