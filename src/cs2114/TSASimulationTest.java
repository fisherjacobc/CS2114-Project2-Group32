package cs2114;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the TSASimulation class. Tests pure domain logic, 
 * passenger routing, search functionality, item screening, and validation.
 * 
 * @author 
 * @version Oct 12, 2026
 */
public class TSASimulationTest {

    private TSASimulation sim;

    /**
     * Sets up a fresh TSASimulation instance before each test executes.
     */
    @BeforeEach
    public void setUp() {
        sim = new TSASimulation();
    }

    // ~ Validation Logic Tests ................................................

    /**
     * Tests isValidFlightNumber with positive integer strings.
     */
    @Test
    public void testIsValidFlightNumber_ValidInputs() {
        assertTrue(sim.isValidFlightNumber("101"));
        assertTrue(sim.isValidFlightNumber(" 500 "));
        assertTrue(sim.isValidFlightNumber("9999"));
    }

    /**
     * Tests isValidFlightNumber with zero, negative numbers, non-numeric strings, 
     * whitespace, and null inputs.
     */
    @Test
    public void testIsValidFlightNumber_InvalidInputs() {
        assertFalse(sim.isValidFlightNumber("0"));
        assertFalse(sim.isValidFlightNumber("-100"));
        assertFalse(sim.isValidFlightNumber("abc"));
        assertFalse(sim.isValidFlightNumber("12ab"));
        assertFalse(sim.isValidFlightNumber(""));
        assertFalse(sim.isValidFlightNumber("   "));
        assertFalse(sim.isValidFlightNumber(null));
    }

    /**
     * Tests isValidBooleanInput with valid affirmative and negative string variations.
     */
    @Test
    public void testIsValidBooleanInput_ValidInputs() {
        assertTrue(sim.isValidBooleanInput("y"));
        assertTrue(sim.isValidBooleanInput("Y"));
        assertTrue(sim.isValidBooleanInput("yes"));
        assertTrue(sim.isValidBooleanInput("YES"));
        assertTrue(sim.isValidBooleanInput("true"));
        assertTrue(sim.isValidBooleanInput("n"));
        assertTrue(sim.isValidBooleanInput("no"));
        assertTrue(sim.isValidBooleanInput("false"));
    }

    /**
     * Tests isValidBooleanInput with invalid choices, empty strings, and null.
     */
    @Test
    public void testIsValidBooleanInput_InvalidInputs() {
        assertFalse(sim.isValidBooleanInput("maybe"));
        assertFalse(sim.isValidBooleanInput("123"));
        assertFalse(sim.isValidBooleanInput(""));
        assertFalse(sim.isValidBooleanInput(null));
    }

    // ~ Routing Tests .........................................................

    /**
     * Tests that routePassenger throws an IllegalArgumentException when passing null.
     */
    @Test
    public void testRoutePassenger_NullThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            sim.routePassenger(null);
        });
    }

    /**
     * Tests routing a standard passenger with no special flags into the standard line.
     */
    @Test
    public void testRoutePassenger_StandardPassenger() {
        Passenger p = new Passenger("Alice Johnson", 202, false, false, new String[]{"Laptop"});
        sim.routePassenger(p);

        Person found = sim.searchPassenger("Alice Johnson", 202);
        assertNotNull(found);
        assertEquals("Alice Johnson", found.getName());
    }

    /**
     * Tests routing a PreCheck passenger into the PreCheck line.
     */
    @Test
    public void testRoutePassenger_PreCheckPassenger() {
        Passenger p = new Passenger("Bob Martinez", 303, true, false, new String[]{"Bag"});
        sim.routePassenger(p);

        Person found = sim.searchPassenger("Bob Martinez", 303);
        assertNotNull(found);
    }

    /**
     * Tests routing a passenger flagged for secondary screening directly into 
     * the secondary screening line.
     */
    @Test
    public void testRoutePassenger_SecondaryScreeningPassenger() {
        Passenger p = new Passenger("John Patel", 404, false, true, new String[]{"Bag"});
        sim.routePassenger(p);

        Person found = sim.searchPassenger("John Patel", 404);
        assertNotNull(found);
    }

    /**
     * Tests routing a crew member into the default standard line.
     */
    @Test
    public void testRoutePassenger_CrewMember() {
        Crew crew = new Crew("Capt. Smith", 505, "Emp123", true);
        sim.routePassenger(crew);

        Person found = sim.searchPassenger("Capt. Smith", 505);
        assertNotNull(found);
    }

    // ~ Search Tests ..........................................................

    /**
     * Tests searchPassenger with null, blank, or invalid flight number arguments.
     */
    @Test
    public void testSearchPassenger_InvalidArguments() {
        assertNull(sim.searchPassenger(null, 101));
        assertNull(sim.searchPassenger("", 101));
        assertNull(sim.searchPassenger("   ", 101));
        assertNull(sim.searchPassenger("Alice", 0));
        assertNull(sim.searchPassenger("Alice", -5));
    }

    /**
     * Tests that searchPassenger is case-insensitive when matching passenger names.
     */
    @Test
    public void testSearchPassenger_CaseInsensitiveName() {
        Passenger p = new Passenger("Diana Prince", 606, false, false, new String[]{"Metal"});
        sim.routePassenger(p);

        assertNotNull(sim.searchPassenger("diana prince", 606));
        assertNotNull(sim.searchPassenger("DIANA PRINCE", 606));
    }

    /**
     * Tests searchPassenger when a person is not present in any queue or details do not match.
     */
    @Test
    public void testSearchPassenger_NotFound() {
        Passenger p = new Passenger("Ethan Hunt", 707, false, false, new String[]{"Bag"});
        sim.routePassenger(p);

        assertNull(sim.searchPassenger("Ethan Hunt", 999));
        assertNull(sim.searchPassenger("Unknown Person", 707));
    }

    // ~ Security Item Checking Tests ..........................................

    /**
     * Tests checkPassengerItems when a passenger carries no prohibited items.
     */
    @Test
    public void testCheckPassengerItems_SafeItems() {
        Passenger p = new Passenger("Fiona Jones", 808, false, false, new String[]{"Laptop", "Bag"});
        sim.routePassenger(p);

        sim.checkPassengerItems(p);

        Person found = sim.searchPassenger("Fiona Jones", 808);
        assertNotNull(found);
    }

    /**
     * Tests checkPassengerItems when a passenger carries a water bottle, verifying 
     * they are flagged and moved to secondary screening.
     */
    @Test
    public void testCheckPassengerItems_ProhibitedWaterBottle() {
        Passenger p = new Passenger("Bad Actor", 909, false, false, new String[]{"Water Bottle"});
        sim.routePassenger(p);

        sim.checkPassengerItems(p);

        Person found = sim.searchPassenger("Bad Actor", 909);
        assertNotNull(found);
    }

    /**
     * Tests checkPassengerItems when a passenger carries a knife.
     */
    @Test
    public void testCheckPassengerItems_ProhibitedKnife() {
        Passenger p = new Passenger("Risk Person", 111, true, false, new String[]{"Knife"});
        sim.routePassenger(p);

        sim.checkPassengerItems(p);

        Person found = sim.searchPassenger("Risk Person", 111);
        assertNotNull(found);
    }

    /**
     * Tests checkPassengerItems when a passenger carries null or empty item arrays.
     */
    @Test
    public void testCheckPassengerItems_NullOrEmptyItems() {
        Passenger p1 = new Passenger("No Items", 222, false, false, null);
        Passenger p2 = new Passenger("Empty Array", 333, false, false, new String[]{});

        sim.routePassenger(p1);
        sim.routePassenger(p2);

        sim.checkPassengerItems(p1);
        sim.checkPassengerItems(p2);

        assertNotNull(sim.searchPassenger("No Items", 222));
        assertNotNull(sim.searchPassenger("Empty Array", 333));
    }

    // ~ Random Generation & Overview Tests ....................................

    /**
     * Tests addRandomPassenger to ensure random generation executes without exceptions.
     */
    @Test
    public void testAddRandomPassenger() {
        sim.addRandomPassenger();
        sim.addRandomPassenger();
    }

    /**
     * Tests printSimulationOverview to ensure overview reporting runs without errors.
     */
    @Test
    public void testPrintSimulationOverview() {
        sim.printSimulationOverview();
    }
}
