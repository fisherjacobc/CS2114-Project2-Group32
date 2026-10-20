package simulation;

import static org.junit.jupiter.api.Assertions.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import simulation.items.GenericItem;
import simulation.items.ItemADT;
import simulation.items.LiquidItem;
import simulation.items.MetallicItem;

/**
 * Unit tests for the TSASimulation class. Tests pure domain logic, 
 * passenger routing, search functionality, item screening, and validation.
 * 
 * @author 
 * @version Oct 12, 2026
 */
public class TSASimulationTest {

    private TSASimulation sim;
    private PrintStream originalOut;
    private ByteArrayOutputStream output;

    /**
     * Sets up a fresh TSASimulation instance and redirects System.out
     * before each test executes.
     */
    @BeforeEach
    public void setUp() {
        sim = new TSASimulation();
        originalOut = System.out;
        output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
    }

    /**
     * Restores System.out after each test executes.
     */
    @AfterEach
    public void tearDown() {
        System.setOut(originalOut);
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
        Passenger p = new Passenger("Alice Johnson", 202, false, false, new ItemADT[]{ new GenericItem("Laptop", false) });
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
        Passenger p = new Passenger("Bob Martinez", 303, true, false, new ItemADT[]{ new GenericItem("Bag", false) });
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
        Passenger p = new Passenger("John Patel", 404, false, true, new ItemADT[]{ new GenericItem("Bag", false) });
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
        Passenger p = new Passenger("Diana Prince", 606, false, false, new ItemADT[]{ new MetallicItem("Metal", false) });
        sim.routePassenger(p);

        assertNotNull(sim.searchPassenger("diana prince", 606));
        assertNotNull(sim.searchPassenger("DIANA PRINCE", 606));
    }

    /**
     * Tests searchPassenger when a person is not present in any queue or details do not match.
     */
    @Test
    public void testSearchPassenger_NotFound() {
        Passenger p = new Passenger("Ethan Hunt", 707, false, false, new ItemADT[]{ new GenericItem("Bag", false) });
        sim.routePassenger(p);

        assertNull(sim.searchPassenger("Ethan Hunt", 999));
        assertNull(sim.searchPassenger("Unknown Person", 707));
    }

    // ~ Security Item Checking Tests ..........................................

    /**
     * Tests checkPassengerItems when a passenger carries no prohibited items,
     * verifying they are cleared and leave the line.
     */
    @Test
    public void testCheckPassengerItems_SafeItems() {
        Passenger p = new Passenger("Fiona Jones", 808, false, false, new ItemADT[]{ new GenericItem("Laptop", false), new GenericItem("Bag", false) });
        sim.routePassenger(p);

        sim.checkPassengerItems(p, new Scanner(""));

        assertNull(sim.searchPassenger("Fiona Jones", 808));
        assertEquals(1, sim.getTotalProcessed());
        assertEquals(0, sim.getTotalDetained());
    }

    /**
     * Tests checkPassengerItems when a passenger carries a water bottle, verifying 
     * they are flagged and moved to secondary screening.
     */
    @Test
    public void testCheckPassengerItems_ProhibitedWaterBottle() {
        Passenger p = new Passenger("Bad Actor", 909, false, false, new ItemADT[]{ new LiquidItem("Water Bottle", 16.0) });
        sim.routePassenger(p);

        sim.checkPassengerItems(p, new Scanner("2\n"));

        Person found = sim.searchPassenger("Bad Actor", 909);
        assertNotNull(found);
        assertTrue(p.isSecondaryScreening());
        assertEquals(1, sim.getTotalFlagged());
    }

    /**
     * Tests checkPassengerItems when a passenger carries a knife.
     */
    @Test
    public void testCheckPassengerItems_ProhibitedKnife() {
        Passenger p = new Passenger("Risk Person", 111, true, false, new ItemADT[]{ new MetallicItem("Knife", true) });
        sim.routePassenger(p);

        sim.checkPassengerItems(p, new Scanner("2\n"));

        Person found = sim.searchPassenger("Risk Person", 111);
        assertNotNull(found);
        assertTrue(p.isSecondaryScreening());
    }

    /**
     * Tests that choosing to detain a passenger with a prohibited item logs
     * them and removes them from the line.
     */
    @Test
    public void testCheckPassengerItems_ProhibitedDetain() {
        Passenger p = new Passenger("Risk Person", 111, false, false, new ItemADT[]{ new LiquidItem("Ranch", 4.0) });
        sim.routePassenger(p);

        sim.checkPassengerItems(p, new Scanner("1\n"));

        assertNull(sim.searchPassenger("Risk Person", 111));
        assertEquals(1, sim.getTotalDetained());
        assertEquals(1, sim.getTotalProcessed());
        assertEquals(0, sim.getTotalFlagged());
    }

    /**
     * Tests that an allowed metallic item sends the passenger to secondary
     * screening without asking for a choice.
     */
    @Test
    public void testCheckPassengerItems_MetallicItem() {
        Passenger p = new Passenger("Clark Kent", 222, true, false, new ItemADT[]{ new MetallicItem("Belt Buckle", false) });
        sim.routePassenger(p);

        sim.checkPassengerItems(p, new Scanner(""));

        assertNotNull(sim.searchPassenger("Clark Kent", 222));
        assertTrue(p.isSecondaryScreening());
        assertEquals(1, sim.getTotalFlagged());
        assertEquals(0, sim.getTotalProcessed());
    }

    /**
     * Tests that a passenger in secondary screening with only an allowed
     * metallic item is cleared on their second check.
     */
    @Test
    public void testCheckPassengerItems_SecondaryCleared() {
        Passenger p = new Passenger("Clark Kent", 222, false, false, new ItemADT[]{ new MetallicItem("Belt Buckle", false) });
        sim.routePassenger(p);

        sim.checkPassengerItems(p, new Scanner(""));
        sim.checkPassengerItems(p, new Scanner(""));

        assertNull(sim.searchPassenger("Clark Kent", 222));
        assertEquals(0, sim.getTotalDetained());
        assertEquals(1, sim.getTotalProcessed());
        assertEquals(1, sim.getTotalFlagged());
    }

    /**
     * Tests that a passenger who is still carrying a prohibited item in
     * secondary screening is detained without being asked for a choice.
     */
    @Test
    public void testCheckPassengerItems_SecondaryDetained() {
        Passenger p = new Passenger("Bad Actor", 909, false, false, new ItemADT[]{ new LiquidItem("Water Bottle", 16.0) });
        sim.routePassenger(p);

        sim.checkPassengerItems(p, new Scanner("2\n"));
        sim.checkPassengerItems(p, new Scanner(""));

        assertNull(sim.searchPassenger("Bad Actor", 909));
        assertEquals(1, sim.getTotalDetained());
        assertEquals(1, sim.getTotalProcessed());
        assertEquals(1, sim.getTotalFlagged());
    }

    /**
     * Tests that a passenger routed straight into secondary screening is
     * detained on their first check if they carry a prohibited item.
     */
    @Test
    public void testCheckPassengerItems_RoutedSecondaryDetained() {
        Passenger p = new Passenger("John Patel", 404, false, true, new ItemADT[]{ new MetallicItem("Knife", true) });
        sim.routePassenger(p);

        sim.checkPassengerItems(p, new Scanner(""));

        assertNull(sim.searchPassenger("John Patel", 404));
        assertEquals(1, sim.getTotalDetained());
    }

    /**
     * Tests checkPassengerItems when a passenger carries null or empty item arrays.
     */
    @Test
    public void testCheckPassengerItems_NullOrEmptyItems() {
        Passenger p1 = new Passenger("No Items", 222, false, false, null);
        Passenger p2 = new Passenger("Empty Array", 333, false, false, new ItemADT[]{});

        sim.routePassenger(p1);
        sim.routePassenger(p2);

        sim.checkPassengerItems(p1, new Scanner(""));
        sim.checkPassengerItems(p2, new Scanner(""));

        assertNull(sim.searchPassenger("No Items", 222));
        assertNull(sim.searchPassenger("Empty Array", 333));
        assertEquals(2, sim.getTotalProcessed());
    }

    /**
     * Tests searchAndCheckPassenger on a crew member, verifying they are
     * cleared and leave the line.
     */
    @Test
    public void testSearchAndCheckPassenger_Crew() {
        sim.routePassenger(new Crew("Capt. Smith", 505, "Emp123", true));

        sim.searchAndCheckPassenger(new Scanner("Capt. Smith\n505\n"));

        assertNull(sim.searchPassenger("Capt. Smith", 505));
        assertEquals(1, sim.getTotalProcessed());
    }

    // ~ Front of Line Tests ...................................................

    /**
     * Runs checkFrontOfLines the given number of times.
     * 
     * @param turns how many turns to run
     */
    private void advance(int turns) {
        for (int i = 0; i < turns; i++) {
            sim.checkFrontOfLines();
        }
    }

    /**
     * Tests that someone in the standard line waits one turn and is then
     * screened for three turns before being cleared.
     */
    @Test
    public void testCheckFrontOfLines_StandardTakesThreeTurns() {
        sim.routePassenger(new Passenger("Fiona Jones", 808, false, false, new ItemADT[]{ new GenericItem("Laptop", false) }));

        advance(3);
        assertNotNull(sim.searchPassenger("Fiona Jones", 808));
        assertTrue(output.toString().contains("Screening... (2/3)"));

        advance(1);
        assertNull(sim.searchPassenger("Fiona Jones", 808));
        assertEquals(1, sim.getTotalProcessed());
    }

    /**
     * Tests that the PreCheck line is screened in two turns.
     */
    @Test
    public void testCheckFrontOfLines_PreCheckTakesTwoTurns() {
        sim.routePassenger(new Passenger("PreCheck Person", 102, true, false, null));

        advance(2);
        assertNotNull(sim.searchPassenger("PreCheck Person", 102));

        advance(1);
        assertNull(sim.searchPassenger("PreCheck Person", 102));
    }

    /**
     * Tests that the secondary screening line takes five turns.
     */
    @Test
    public void testCheckFrontOfLines_SecondaryTakesFiveTurns() {
        sim.routePassenger(new Passenger("Secondary Person", 103, false, true, null));

        advance(5);
        assertNotNull(sim.searchPassenger("Secondary Person", 103));

        advance(1);
        assertNull(sim.searchPassenger("Secondary Person", 103));
    }

    /**
     * Tests that only the front of a line is screened, in the order people
     * joined, and that the next person starts once the front leaves.
     */
    @Test
    public void testCheckFrontOfLines_FrontOnly() {
        sim.routePassenger(new Passenger("First Person", 101, false, false, null));
        sim.routePassenger(new Passenger("Second Person", 102, false, false, null));

        advance(4);
        assertNull(sim.searchPassenger("First Person", 101));
        assertNotNull(sim.searchPassenger("Second Person", 102));

        advance(2);
        assertNotNull(sim.searchPassenger("Second Person", 102));

        advance(1);
        assertNull(sim.searchPassenger("Second Person", 102));
    }

    /**
     * Tests that a prohibited item sends the passenger to secondary
     * screening without asking, and that they are detained once secondary
     * screening finishes if they still have it.
     */
    @Test
    public void testCheckFrontOfLines_ProhibitedThenDetained() {
        Passenger p = new Passenger("Bad Actor", 909, false, false, new ItemADT[]{ new LiquidItem("Water Bottle", 16.0) });
        sim.routePassenger(p);

        advance(4);
        assertNotNull(sim.searchPassenger("Bad Actor", 909));
        assertTrue(p.isSecondaryScreening());
        assertEquals(1, sim.getTotalFlagged());

        advance(4);
        assertNotNull(sim.searchPassenger("Bad Actor", 909));

        advance(1);
        assertNull(sim.searchPassenger("Bad Actor", 909));
        assertEquals(1, sim.getTotalDetained());
    }

    /**
     * Tests that a crew member at the front of a line is cleared.
     */
    @Test
    public void testCheckFrontOfLines_Crew() {
        sim.routePassenger(new Crew("Capt. Smith", 505, "Emp123", true));

        advance(4);

        assertNull(sim.searchPassenger("Capt. Smith", 505));
        assertEquals(1, sim.getTotalProcessed());
    }

    /**
     * Tests that nothing is printed when no one is waiting at a front.
     */
    @Test
    public void testCheckFrontOfLines_NothingWaiting() {
        sim.checkFrontOfLines();
        assertFalse(output.toString().contains("Line Activity"));
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

    // ~ Line Display Tests ....................................................

    /**
     * Tests that printLines shows each line from front to back, with the
     * size of each line in its column header.
     */
    @Test
    public void testPrintLines() {
        sim.routePassenger(new Passenger("First Person", 101, false, false, null));
        sim.routePassenger(new Passenger("Second Person", 102, false, false, null));
        sim.routePassenger(new Passenger("PreCheck Person", 103, true, false, null));

        sim.printLines();
        String printed = output.toString();

        assertTrue(printed.contains("Standard (2)"));
        assertTrue(printed.contains("PreCheck (1)"));
        assertTrue(printed.contains("Secondary (0)"));
        assertTrue(printed.indexOf("First Person") < printed.indexOf("Second Person"));
        assertTrue(printed.contains("#101"));
        assertTrue(printed.contains("#103"));
    }

    /**
     * Tests that printLines shows empty lines and shortens long names.
     */
    @Test
    public void testPrintLines_EmptyAndLongName() {
        sim.printLines();
        assertTrue(output.toString().contains("(empty)"));

        sim.routePassenger(new Passenger("Bartholomew Montgomery III", 101, false, false, null));
        sim.printLines();
        assertTrue(output.toString().contains("Bartholomew Montgom…"));
    }
}
