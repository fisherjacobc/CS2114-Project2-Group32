package simulation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
/**
 *  Write a one-sentence summary of your class here.
 *  Follow it with additional details about its purpose, what abstraction
 *  it represents, and how to use it.
 * 
 *  @author 
 *  @version Oct 12, 2026
 */
public class TSASimulation
{
     
    //~ Fields ................................................................
    
    private static final String[] PRESET_ITEMS = {
        "Laptop", "Water Bottle", "Knife", "Bag", "Metal", "Prohibited Item"
    };
    
    private static final String[] PASSENGER_NAMES = {
        "Alice Johnson", "Bob Martinez", "John Patel", "Diana Prince", 
        "Ethan Hunt", "Fiona Jones"
    };

    private static final String[] CREW_NAMES = {
        "Capt. Smith", "First Officer Miller", "Purser Davis",
        "Flight Attendant Lee"
    };
    
    private final LinkedChain<Person> standardLine = new LinkedChain<>();
    private final LinkedChain<Person> preCheckLine = new LinkedChain<>();
    private final LinkedChain<Person> secondaryScreeningLine = new LinkedChain<>();
    private final List<LogEntry> detainedLog = new ArrayList<>();
    
    private int totalProcessed = 0;
    private int totalFlagged = 0;
    private final Random random = new Random();
 
    //~Public  Methods ........................................................
    
    /**
     * routes a passenger to the correct line, if a passenger
     * is marked for a specific line they go to that line
     * if the person object is a crew they are automatically in the
     * standard line 
     * @param person the person being routed.
     */
    public void routePassenger(Person person)
    {
        if (person == null)
        {
            throw new IllegalArgumentException("Cannot route a null person.");
        }
        if (person instanceof Passenger p)
        {
            if (p.isSecondaryScreening())
            {
                secondaryScreeningLine.add(p);
                totalFlagged++;
            }
            else if (p.isPreCheck())
            {
                preCheckLine.add(p);
            }
            else 
            {
                standardLine.add(p);
            }
        }
        else
        {
            standardLine.add(person);
        }
    }
    
   /**
    * Adds a random passenger (Option A)
    */
    public void addRandomPassenger()
    {
        int flightNum = 100 + random.nextInt(900);
        if (random.nextBoolean())
        {
            String name = 
                PASSENGER_NAMES[random.nextInt(PASSENGER_NAMES.length)];
            
            boolean preCheck = random.nextBoolean();
            boolean secondaryScreening = !preCheck && random.nextInt(100) < 25;
            
            String[] items = 
                {PRESET_ITEMS[random.nextInt(PRESET_ITEMS.length)]};
           
            Passenger passenger = 
                new Passenger(name, flightNum, preCheck, secondaryScreening, items);
            
            routePassenger(passenger);
            
            System.out.println
            ("Added Passenger: " + name + " with Flight # " + flightNum);
        }
        else
        {
            String name = CREW_NAMES[random.nextInt(CREW_NAMES.length)];
            String employeeID = "EmpID" + (1000 + random.nextInt(9000));
            Boolean fit = random.nextInt(100) < 90;
            
            Crew crew = new Crew(name, flightNum, employeeID, fit);
            routePassenger(crew);
            System.out.println
                ("Added Crew: " + name + " with flight #: " + flightNum);
        }
        
    }
    
    /**
     * adds custom passengertaking scanner input (Option B)
     * @param scanner input from scanner 
     */
    public void addCustomPassenger(Scanner scanner) 
    {
        System.out.println("\n-- Add Custom Passenger --");
        System.out.print("Enter Name: ");
        String name = scanner.nextLine().trim();
        while (name.isEmpty()) {
            System.out.print("Name cannot be blank. Enter Name: ");
            name = scanner.nextLine().trim();
        }

        int flightNum = readValidFlightNumber(scanner);
        boolean isPreCheck = readValidBoolean(scanner, 
            "Is TSA PreCheck? (y/n): ");
        boolean isSSSS = readValidBoolean(scanner, 
            "Is Flagged for Secondary Screening? (y/n): ");
        String[] items = selectPresetItems(scanner);

        routePassenger(new Passenger(name, flightNum, isPreCheck, isSSSS, items));
        System.out.println("Success: Added " + name + " to line.");
    }
    
    /**
     * Searches and checks a passenger for items they shouldnt have sends them
     * to secondaryScreening if they do (Option C)
     * @param scanner
     */
    public void searchAndCheckPassenger(Scanner scanner) 
    {
        System.out.println("-- Search & Check Passenger --");
        System.out.print("Enter Name: ");
        String name = scanner.nextLine().trim();
        int flightNum = readValidFlightNumber(scanner);

        Person found = searchPassenger(name, flightNum);

        if (found == null) 
        {
            System.out.println("Passenger not found in any queue.");
            return;
        }

        System.out.println("Found: " + found);

        if (found instanceof Passenger passenger) 
        {
            checkPassengerItems(passenger);
        } else {
            System.out.println("Person is Crew; cleared for security.");
        }
    }
   
    /**
     * searches for a passenger
     * @param name name of passenger
     * @param flightNumber flight # of passenger being looked for
     * @return Person object if found, null if not
     */
    public Person searchPassenger(String name, int flightNumber) {
        if (name == null || name.isBlank() || flightNumber <= 0) {
            return null;
        }

        Person match = findInChain(standardLine, name, flightNumber);
        if (match != null) return match;

        match = findInChain(preCheckLine, name, flightNumber);
        if (match != null) return match;

        return findInChain(secondaryScreeningLine, name, flightNumber);
    }
    
    /**
     * checks a passengers items 
     * @param passenger the passenger being checked 
     */
    public void checkPassengerItems(Passenger passenger) 
    {
        boolean hasProhibited = false;
        String foundItem = "";

        if (passenger.getItems() != null) {
            for (String item : passenger.getItems()) {
                if (isProhibitedItem(item)) {
                    hasProhibited = true;
                    foundItem = item;
                    break;
                }
            }
        }

        if (hasProhibited) {
            // Unlink from current line (standard or precheck)
            standardLine.remove(passenger);
            preCheckLine.remove(passenger);

            // Move to secondary screening
            secondaryScreeningLine.add(passenger);
            totalFlagged++;

            System.out.println("FLAGGED: Prohibited item found (" 
                + foundItem + ")!");
            System.out.println("Passenger " + passenger.getName() 
                + " moved to Secondary Screening Line.");
        } else {
            System.out.println("CLEARED: No prohibited items found for " 
                + passenger.getName() + ".");
        }
    }

    /**
     * boolean helper that looks for a prohibited item
     * @param item
     * @return true if person has a prohibited item
     */
    private boolean isProhibitedItem(String item) {
        if (item == null)
        {
            return false;
        }
        String clean = item.toLowerCase();
        return clean.contains("water bottle") || clean.contains("knife") || 
            clean.contains("prohibited item");
    }
   
    /**
    * prints an overview of the simulation, option D
    */
    public void printSimulationOverview() 
    {
        System.out.println("==================================================");
        System.out.println("               SIMULATION OVERVIEW                ");
        System.out.println("==================================================");
        System.out.println("Passengers Processed:     " 
            + totalProcessed);
        System.out.println("Passengers Flagged (SSSS): " 
            + totalFlagged);
         System.out.println("Total Detained:           " 
            + detainedLog.size());
        System.out.println("Standard Line Queue:      " 
            + standardLine.size());
        System.out.println("PreCheck Line Queue:      " 
            + preCheckLine.size());
        System.out.println("Secondary Screening Queue: " 
            + secondaryScreeningLine.size());
    }
    
    /**
     * checks validation of number for flight number 
     * @param input the flight number inputed as a string
     * @return true if its a valid number false if not
     */
    public boolean isValidFlightNumber(String input) {
        if (input == null || input.trim().isEmpty()) return false;
        try {
            int num = Integer.parseInt(input.trim());
            return num > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    /**
     * checks if an input is a valid boolean
     * @param input the string input of the boolean
     * @return true if its a valid boolean, false if null or not valid
     */
    public boolean isValidBooleanInput(String input) 
    {
        if (input == null) 
        {
            return false;
        }
        String clean = input.trim().toLowerCase();
        return clean.equals("y") || clean.equals("yes") || clean.equals("true") ||
               clean.equals("n") || clean.equals("no")  || clean.equals("false");
    }
    
    /**
     * decides whether to accept user entry 
     * @param scanner the user input
     * @return the number if successfully checked and accepted
     */
    private int readValidFlightNumber(Scanner scanner) {
        while (true) 
        {
            System.out.print("Enter Flight Number: ");
            String input = scanner.nextLine();
            if (isValidFlightNumber(input)) 
            {
                return Integer.parseInt(input.trim());
            }
            System.out.println("Error: Flight number must be a positive integer.");
        }
    }
  
    /**
     * decides whether to accept user entry
     * @param scanner the input being checked
     * @param prompt the prompt its being checked with
     * @return true if boolean is true, false otherwise
     */
    private boolean readValidBoolean(Scanner scanner, String prompt) {
        while (true) 
        {
            System.out.print(prompt);
            String input = scanner.nextLine();
            if (isValidBooleanInput(input)) 
            {
                String clean = input.trim().toLowerCase();
                return clean.equals("y") || clean.equals("yes") 
                    || clean.equals("true");
            }
            System.out.println("Error: Please enter 'y/yes' or 'n/no'.");
        }
    }
    /**
     * makes a selection of the items from the array
     * @param scanner the user input
     * @return 
     */
    private String[] selectPresetItems(Scanner scanner) {
        List<String> chosen = new ArrayList<>();
        System.out.println("Select Items (0 when finished):");
        for (int i = 0; i < PRESET_ITEMS.length; i++) 
        {
            System.out.println((i + 1) + ": " + PRESET_ITEMS[i]);
        }

        while (true) 
        {
            System.out.print("Enter item choice #: ");
            String input = scanner.nextLine().trim();
            if (isValidFlightNumber(input) || input.equals("0")) 
            {
                int choice = Integer.parseInt(input);
                if (choice == 0)
                {
                    break;
                }
                if (choice >= 1 && choice <= PRESET_ITEMS.length) 
                {
                    chosen.add(PRESET_ITEMS[choice - 1]);
                    System.out.println("-> Added: " + PRESET_ITEMS[choice - 1]);
                } 
                else 
                {
                    System.out.println("Error: Choice must be between 1 and " + PRESET_ITEMS.length + " (or 0 to stop).");
                }
            } else 
            {
                System.out.println("Error: Please enter a valid number.");
            }
        }
        return chosen.toArray(new String[0]);
    }

    /**
     * finds person in a chain
     * @param chain the chain being checked
     * @param name the name being checked
     * @param flightNumber the flight number being checked
     * @return returns null if person not found or the person if they were found
     */
    private Person findInChain(LinkedChain<Person> chain, String name, int flightNumber) 
    {
        for (Person p : chain) 
        {
            if (p.getName().equalsIgnoreCase(name) && 
                p.getFlightNumber() == flightNumber) 
            {
                return p;
            }
        }
        return null;
    }
    
}
