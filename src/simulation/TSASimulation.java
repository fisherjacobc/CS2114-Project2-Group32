package simulation;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import simulation.items.GenericItem;
import simulation.items.ItemADT;
import simulation.items.LiquidItem;
import simulation.items.MetallicItem;
import util.LinkedChain;
import util.LogEntry;
import util.Node;

/**
 * Write a one-sentence summary of your class here.
 * Follow it with additional details about its purpose, what abstraction
 * it represents, and how to use it.
 * 
 * @author
 * @version Oct 12, 2026
 */
public class TSASimulation {

    // ~ Fields ................................................................

    private static final ItemADT[] PRESET_ITEMS = {
            new GenericItem("Laptop", false),
            new LiquidItem("Water Bottle 16oz", 16.0), // > 3.4 oz liquid limit
            new MetallicItem("Pocket Knife", true), // Prohibited item
            new GenericItem("Travel Bag", false),
            new MetallicItem("Metal Scissors", false), // Metal material
            new LiquidItem("Travel Shampoo", 2.0), // Cleared
            new LiquidItem("Water Bottle 2oz", 2.0),
            new LiquidItem("Ranch", 4.0)
    };

    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss");

    private static final String[] PASSENGER_NAMES = {
            "Alice Johnson", "Bob Martinez", "John Patel", "Diana Prince",
            "Ethan Hunt", "Fiona Jones", "George Kim", "Hannah Schmidt",
            "Ivan Petrov", "Julia Roberts", "Kevin O'Brien", "Laura Chen",
            "Marcus Brown", "Nina Alvarez", "Oscar Nguyen", "Priya Shah",
            "Quentin Hayes", "Rachel Green", "Samuel Okafor", "Tara Walsh",
            "Umar Farouk", "Valerie Cruz", "William Turner", "Ximena Lopez",
            "Yusuf Demir", "Zoe Bennett", "Aaron Fischer", "Bianca Rossi",
            "Carlos Mendes", "Daniela Silva", "Elijah Moore", "Grace Park",
            "Hiroshi Tanaka", "Isla McKenzie", "Jamal Washington", "Keiko Sato",
            "Liam Murphy", "Maya Goldberg", "Noah Fitzgerald", "Olivia Wright"
    };

    private static final String[] CREW_NAMES = {
            "Capt. Fisher", "Capt. Quesenberry", "Capt. Moreno", "Capt. Molina",
            "Capt. Smith", "First Officer Miller", "Purser Davis",
            "Flight Attendant Lee", "Capt. Reynolds", "Capt. Okonkwo",
            "Capt. Larsen", "First Officer Grant", "First Officer Wu",
            "First Officer Novak", "Purser Ramirez", "Purser Thompson",
            "Attendant Garcia", "Attendant Nakamura", "Attendant Collins",
            "Attendant Dubois", "Attendant Singh", "Attendant Harper"
    };

    private final LinkedChain<Person> standardLine = new LinkedChain<>();
    private final LinkedChain<Person> preCheckLine = new LinkedChain<>();
    private final LinkedChain<Person> secondaryScreeningLine = new LinkedChain<>();
    private final List<LogEntry> detainedLog = new ArrayList<>();

    private int lastPassengerNameIndex = -1;
    private int lastCrewNameIndex = -1;

    // How many turns it takes to screen the person at the front of each line,
    // in getLines() order (Secondary, Standard, PreCheck). Raise these to make
    // the lines move slower.
    private static final int[] TURNS_TO_SCREEN = { 5, 3, 2 };

    // The person at the front of each line (in getLines() order) the last
    // time checkFrontOfLines ran, and how many turns they have been screened
    private final Person[] waitingAtFront = new Person[3];
    private final int[] turnsAtFront = new int[3];

    private int totalProcessed = 0;
    private int totalFlagged = 0;
    private final Random random = new Random();

    // ~Public Methods ........................................................

    /**
     * routes a passenger to the correct line, if a passenger
     * is marked for a specific line they go to that line
     * if the person object is a crew they are automatically in the
     * standard line
     * 
     * @param person the person being routed.
     */
    public void routePassenger(Person person) {
        if (person == null) {
            throw new IllegalArgumentException("Cannot route a null person.");
        }
        if (person instanceof Passenger) {
            Passenger p = (Passenger) person;
            if (p.isSecondaryScreening()) {
                secondaryScreeningLine.add(p);
                totalFlagged++;
            } else if (p.isPreCheck()) {
                preCheckLine.add(p);
            } else {
                standardLine.add(p);
            }
        } else {
            standardLine.add(person);
        }
    }

    /**
     * Adds a random passenger (Option A)
     */
    public void addRandomPassenger() {
        int flightNum = 100 + random.nextInt(900);
        if (random.nextBoolean()) {
            // Pick a non-repeating passenger name
            int nameIndex = random.nextInt(PASSENGER_NAMES.length);
            while (nameIndex == lastPassengerNameIndex && PASSENGER_NAMES.length > 1) {
                nameIndex = random.nextInt(PASSENGER_NAMES.length);
            }
            lastPassengerNameIndex = nameIndex;
            String name = PASSENGER_NAMES[nameIndex];

            boolean preCheck = random.nextBoolean();
            boolean secondaryScreening = !preCheck && random.nextInt(100) < 25;

            ItemADT[] items = { PRESET_ITEMS[random.nextInt(PRESET_ITEMS.length)] };

            Passenger passenger = new Passenger(name, flightNum, preCheck, secondaryScreening, items);
            routePassenger(passenger);

            System.out.println("┌──Passenger Arrived────────────────────┐");
            printPersonDetails(passenger);
            System.out.println("└───────────────────────────────────────┘");
        } else {
            // Pick a non-repeating crew name
            int nameIndex = random.nextInt(CREW_NAMES.length);
            while (nameIndex == lastCrewNameIndex && CREW_NAMES.length > 1) {
                nameIndex = random.nextInt(CREW_NAMES.length);
            }
            lastCrewNameIndex = nameIndex;
            String name = CREW_NAMES[nameIndex];

            String employeeID = "EmpID" + (1000 + random.nextInt(9000));
            Boolean fit = random.nextInt(100) < 90;

            Crew crew = new Crew(name, flightNum, employeeID, fit);
            routePassenger(crew);

            System.out.println("┌──Crew Arrived─────────────────────────┐");
            printPersonDetails(crew);
            System.out.println("└───────────────────────────────────────┘");
        }
    }

    /**
     * adds custom passenger taking scanner input (Option B)
     * 
     * @param scanner input from scanner
     */
    public void addCustomPassenger(Scanner scanner) {
        System.out.println("┌──Add Custom Passenger─────────────────┐");
        System.out.print("│ Name: ");
        String name = scanner.nextLine().trim();
        while (name.isEmpty()) {
            printRow("Error: Name cannot be blank.");
            System.out.print("│ Name: ");
            name = scanner.nextLine().trim();
        }

        int flightNum = readValidFlightNumber(scanner);
        boolean isPreCheck = readValidBoolean(scanner,
                "TSA PreCheck? (y/n)");
        boolean isSSSS = readValidBoolean(scanner,
                "Flagged for Secondary Screening? (y/n)");
        ItemADT[] items = selectPresetItems(scanner);

        Passenger passenger = new Passenger(name, flightNum, isPreCheck, isSSSS, items);
        routePassenger(passenger);

        System.out.println("├──Passenger Added──────────────────────┤");
        printPersonDetails(passenger);
        System.out.println("└───────────────────────────────────────┘");
    }

    /**
     * Searches and checks a passenger for items they shouldnt have sends them
     * to secondaryScreening if they do (Option C)
     * 
     * @param scanner
     */
    public void searchAndCheckPassenger(Scanner scanner) {
        System.out.println("┌──Search & Check Passenger─────────────┐");
        System.out.print("│ Name: ");
        String name = scanner.nextLine().trim();
        int flightNum = readValidFlightNumber(scanner);

        Person found = searchPassenger(name, flightNum);

        if (found == null) {
            System.out.println("├──Not Found────────────────────────────┤");
            System.out.println("│ No one matches in any line.           │");
            System.out.println("└───────────────────────────────────────┘");
            return;
        }

        System.out.println("├──Found────────────────────────────────┤");
        printPersonDetails(found);
        System.out.println("├──Screening────────────────────────────┤");

        if (found instanceof Passenger) {
            Passenger passenger = (Passenger) found;
            checkPassengerItems(passenger, scanner);
        } else {
            removeFromLines(found);
            totalProcessed++;
            System.out.println("│ CLEARED: Crew member                  │");
        }

        System.out.println("└───────────────────────────────────────┘");
    }

    /**
     * Checks a passenger's items and decides what happens to them. Every
     * check removes the passenger from the line they were in.
     * 
     * On the initial check, a prohibited item lets the user choose to detain
     * the passenger or send them to secondary screening, and a metallic item
     * sends them to secondary screening automatically. If the passenger is
     * already in secondary screening, a prohibited item detains them and
     * otherwise they are cleared.
     * 
     * @param passenger the passenger getting checked
     * @param scanner   input
     */
    public void checkPassengerItems(Passenger passenger, Scanner scanner) {
        screenPassenger(passenger, scanner, true);
    }

    /**
     * Works on screening the person at the front of each line. Someone who
     * reaches the front waits one turn, then takes TURNS_TO_SCREEN turns
     * for their line before they are checked, so slower lines build up.
     * 
     * Secondary screening is checked first, so anyone moved there this turn
     * waits until the next turn. Passengers with a prohibited item are sent
     * to secondary screening automatically instead of asking for a choice.
     */
    public void checkFrontOfLines() {
        LinkedChain<Person>[] lines = getLines();
        String[] lineNames = { "Secondary", "Standard", "PreCheck" };
        boolean printedHeader = false;

        for (int i = 0; i < lines.length; i++) {
            Node<Person> front = lines[i].getLastNode();
            if (front == null || front.getData() != waitingAtFront[i]) {
                continue;
            }

            if (!printedHeader) {
                System.out.println("┌──Line Activity────────────────────────┐");
                printedHeader = true;
            } else {
                System.out.println("│                                       │");
            }

            Person person = front.getData();
            printRow("[" + lineNames[i] + "] " + person.getName());

            turnsAtFront[i]++;
            if (turnsAtFront[i] < TURNS_TO_SCREEN[i]) {
                printRow("Screening... (" + turnsAtFront[i] + "/" + TURNS_TO_SCREEN[i] + ")");
                continue;
            }

            if (person instanceof Passenger) {
                screenPassenger((Passenger) person, null, false);
            } else {
                removeFromLines(person);
                totalProcessed++;
                System.out.println("│ CLEARED: Crew member                  │");
            }
        }

        if (printedHeader) {
            System.out.println("└───────────────────────────────────────┘");
        }

        for (int i = 0; i < lines.length; i++) {
            Node<Person> front = lines[i].getLastNode();
            Person person = front == null ? null : front.getData();
            if (person != waitingAtFront[i]) {
                waitingAtFront[i] = person;
                turnsAtFront[i] = 0;
            }
        }
    }

    /**
     * gets the lines in the order checkFrontOfLines checks them
     *
     * @return the secondary screening, standard, and PreCheck lines
     */
    @SuppressWarnings("unchecked")
    private LinkedChain<Person>[] getLines() {
        return new LinkedChain[] { secondaryScreeningLine, standardLine, preCheckLine };
    }

    /**
     * Checks a passenger's items and decides what happens to them. Every
     * check removes the passenger from the line they were in.
     * 
     * @param passenger    the passenger getting checked
     * @param scanner      input, only used if askForChoice is true
     * @param askForChoice true to ask whether to detain a passenger with a
     *                     prohibited item, false to send them to secondary
     *                     screening automatically
     */
    private void screenPassenger(Passenger passenger, Scanner scanner, boolean askForChoice) {
        boolean inSecondary = secondaryScreeningLine.getFirstReferenceToValue(passenger) != null;
        String prohibitedReason = null;
        boolean hasMetallic = false;

        if (passenger.getItems() != null) {
            for (ItemADT item : passenger.getItems()) {
                if (item == null) {
                    continue;
                }
                if (prohibitedReason == null && item.isProhibited()) {
                    prohibitedReason = item.getReason();
                }
                if (item instanceof MetallicItem) {
                    hasMetallic = true;
                }
            }
        }

        removeFromLines(passenger);

        if (inSecondary) {
            if (prohibitedReason != null) {
                printRow("[FLAGGED] Prohibited item still found");
                printRow("  " + prohibitedReason);
                detainPassenger(passenger, prohibitedReason);
            } else {
                clearPassenger(passenger);
            }
        } else if (prohibitedReason != null && !askForChoice) {
            printRow("[FLAGGED] Violation detected");
            printRow("  " + prohibitedReason);
            sendToSecondaryScreening(passenger);
        } else if (prohibitedReason != null) {
            printRow("[FLAGGED] Violation detected");
            printRow("  " + prohibitedReason);
            System.out.println("│                                       │");
            System.out.println("│ [1] Detain Passenger                  │");
            System.out.println("│ [2] Send to Secondary Screening       │");
            System.out.println("│                                       │");
            System.out.print("│ Choice: ");

            String choice = scanner.nextLine().trim();

            if ("1".equals(choice)) {
                detainPassenger(passenger, prohibitedReason);
            } else {
                sendToSecondaryScreening(passenger);
            }
        } else if (hasMetallic) {
            System.out.println("│ [FLAGGED] Metallic item detected      │");
            sendToSecondaryScreening(passenger);
        } else {
            clearPassenger(passenger);
        }
    }

    /**
     * searches for a passenger
     * 
     * @param name         name of passenger
     * @param flightNumber flight # of passenger being looked for
     * @return Person object if found, null if not
     */
    public Person searchPassenger(String name, int flightNumber) {
        if (name == null || name.isBlank() || flightNumber <= 0) {
            return null;
        }

        Person equivelant = new Passenger(name, flightNumber, false, false, new ItemADT[0]);

        Node<Person> match = standardLine.getFirstReferenceToValue(equivelant);
        if (match != null)
            return match.getData();

        match = preCheckLine.getFirstReferenceToValue(equivelant);
        if (match != null)
            return match.getData();

        match = secondaryScreeningLine.getFirstReferenceToValue(equivelant);

        if (match != null)
            return match.getData();

        return null;
    }

    /**
     * gets the number of people cleared or detained
     *
     * @return the total processed
     */
    public int getTotalProcessed() {
        return totalProcessed;
    }

    /**
     * gets the number of passengers sent to secondary screening
     *
     * @return the total flagged
     */
    public int getTotalFlagged() {
        return totalFlagged;
    }

    /**
     * gets the number of detained passengers
     *
     * @return the number of entries in the detained log
     */
    public int getTotalDetained() {
        return detainedLog.size();
    }

    /**
     * prints an overview of the simulation, option D
     */
    public void printSimulationOverview() {
        System.out.println("┌──Simulation Overview──────────────────┐");
        System.out.println("│                                       │");
        printRow("Processed:            " + totalProcessed);
        printRow("Flagged (Secondary):  " + totalFlagged);
        printRow("Detained:             " + detainedLog.size());
        System.out.println("│                                       │");
        System.out.println("├──Lines────────────────────────────────┤");
        System.out.println("│                                       │");
        printRow("Standard:             " + standardLine.getCurrentSize());
        printRow("PreCheck:             " + preCheckLine.getCurrentSize());
        printRow("Secondary Screening:  " + secondaryScreeningLine.getCurrentSize());
        System.out.println("│                                       │");
        System.out.println("├──Detained Log─────────────────────────┤");
        System.out.println("│                                       │");
        if (detainedLog.isEmpty()) {
            System.out.println("│ No one was detained.                  │");
        } else {
            for (LogEntry entry : detainedLog) {
                printRow("[" + entry.timestamp() + "] " + entry.passengerName());
                printRow("  " + entry.reason());
            }
        }
        System.out.println("│                                       │");
        System.out.println("└───────────────────────────────────────┘");
    }

    /**
     * prints all three lines side by side, with the front of each line at
     * the top and the back of each line at the bottom
     */
    public void printLines() {
        List<Person> standard = standardLine.toList();
        List<Person> preCheck = preCheckLine.toList();
        List<Person> secondary = secondaryScreeningLine.toList();

        // New people are added at the head of the chain, so the front of the
        // line is the end of the list
        Collections.reverse(standard);
        Collections.reverse(preCheck);
        Collections.reverse(secondary);

        int rows = Math.max(1, Math.max(standard.size(), Math.max(preCheck.size(), secondary.size())));

        System.out.println("┌──Security Lines (front to back)─────────────────────────────────────────────────────────────────┐");
        System.out.println("├────┬──────────────────────────────┬──────────────────────────────┬──────────────────────────────┤");
        System.out.printf("│  # │ %-28s │ %-28s │ %-28s │%n",
                "Standard (" + standard.size() + ")",
                "PreCheck (" + preCheck.size() + ")",
                "Secondary (" + secondary.size() + ")");
        System.out.println("├────┼──────────────────────────────┼──────────────────────────────┼──────────────────────────────┤");
        for (int i = 0; i < rows; i++) {
            System.out.printf("│ %2d │ %-28s │ %-28s │ %-28s │%n", i + 1,
                    getLineCell(standard, i), getLineCell(preCheck, i), getLineCell(secondary, i));
        }
        System.out.println("└────┴──────────────────────────────┴──────────────────────────────┴──────────────────────────────┘");
    }

    /**
     * gets the text for one spot in a line
     *
     * @param line     the people in the line, front first
     * @param position the spot in the line
     * @return the person's name and flight number, "(empty)" for an empty
     *         line, or blank if the line is shorter than the position
     */
    private String getLineCell(List<Person> line, int position) {
        if (line.isEmpty()) {
            return position == 0 ? "(empty)" : "";
        }
        if (position >= line.size()) {
            return "";
        }

        Person person = line.get(position);
        String name = person.getName();
        if (name.length() > 20) {
            name = name.substring(0, 19) + "…";
        }
        return String.format("%-20s #%d", name, person.getFlightNumber());
    }

    /**
     * checks validation of number for flight number
     * 
     * @param input the flight number inputed as a string
     * @return true if its a valid number false if not
     */
    public boolean isValidFlightNumber(String input) {
        if (input == null || input.trim().isEmpty())
            return false;
        try {
            int num = Integer.parseInt(input.trim());
            return num > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * checks if an input is a valid boolean
     * 
     * @param input the string input of the boolean
     * @return true if its a valid boolean, false if null or not valid
     */
    public boolean isValidBooleanInput(String input) {
        if (input == null) {
            return false;
        }
        String clean = input.trim().toLowerCase();
        return clean.equals("y") || clean.equals("yes") || clean.equals("true") ||
                clean.equals("n") || clean.equals("no") || clean.equals("false");
    }

    /**
     * removes a person from whichever line they are currently in
     *
     * @param person the person being removed
     */
    private void removeFromLines(Person person) {
        standardLine.remove(standardLine.getFirstReferenceToValue(person));
        preCheckLine.remove(preCheckLine.getFirstReferenceToValue(person));
        secondaryScreeningLine.remove(secondaryScreeningLine.getFirstReferenceToValue(person));
    }

    /**
     * clears a passenger through security
     *
     * @param passenger the passenger being cleared
     */
    private void clearPassenger(Passenger passenger) {
        totalProcessed++;
        System.out.println("│ CLEARED: No prohibited items found    │");
    }

    /**
     * detains a passenger and records them in the detained log
     *
     * @param passenger the passenger being detained
     * @param reason    why the passenger is being detained
     */
    private void detainPassenger(Passenger passenger, String reason) {
        String timestamp = LocalTime.now().format(TIME_FORMAT);
        detainedLog.add(new LogEntry(timestamp, passenger.getName(), reason, false));
        totalProcessed++;
        printRow("DETAINED: " + passenger.getName());
    }

    /**
     * moves a passenger to the secondary screening line
     *
     * @param passenger the passenger being moved
     */
    private void sendToSecondaryScreening(Passenger passenger) {
        passenger.setSecondaryScreening(true);
        secondaryScreeningLine.add(passenger);
        totalFlagged++;
        System.out.println("│ Moved to Secondary Screening          │");
    }

    /**
     * prints a person's details inside a box
     *
     * @param person the person to print
     */
    private void printPersonDetails(Person person) {
        printRow("Name:      " + person.getName());
        printRow("Flight #:  " + person.getFlightNumber());
        printRow("Line:      " + getLineName(person));

        if (person instanceof Passenger) {
            Passenger passenger = (Passenger) person;
            printRow("PreCheck:  " + (passenger.isPreCheck() ? "Yes" : "No"));
            printRow("Items:     " + formatItems(passenger.getItems()));
        } else if (person instanceof Crew) {
            Crew crew = (Crew) person;
            printRow("Crew ID:   " + crew.getEmployeeID());
            printRow("Fit:       " + (crew.isFitToFly() ? "Yes" : "No"));
        }
    }

    /**
     * gets the name of the line a person is in
     *
     * @param person the person being looked up
     * @return the name of the line, or "None" if they are not in one
     */
    private String getLineName(Person person) {
        if (secondaryScreeningLine.getFirstReferenceToValue(person) != null) {
            return "Secondary Screening";
        }
        if (preCheckLine.getFirstReferenceToValue(person) != null) {
            return "PreCheck";
        }
        if (standardLine.getFirstReferenceToValue(person) != null) {
            return "Standard";
        }
        return "None";
    }

    /**
     * prints text as a row inside a box, wrapping it onto more rows if it
     * is too long to fit
     *
     * @param text the text to print
     */
    private void printRow(String text) {
        int maxLength = 37;
        String indent = text.substring(0, text.length() - text.stripLeading().length());
        String remaining = text;

        while (remaining.length() > maxLength) {
            int split = remaining.lastIndexOf(' ', maxLength);
            if (split <= indent.length()) {
                split = maxLength;
            }
            System.out.printf("│ %-38s│%n", remaining.substring(0, split));
            remaining = indent + remaining.substring(split).trim();
        }

        System.out.printf("│ %-38s│%n", remaining);
    }

    /**
     * formats a list of items as a comma separated string
     *
     * @param items the items to format
     * @return the formatted items, or "None" if there are none
     */
    private String formatItems(ItemADT[] items) {
        if (items == null || items.length == 0) {
            return "None";
        }

        StringBuilder builder = new StringBuilder();
        for (ItemADT item : items) {
            if (builder.length() > 0) {
                builder.append(", ");
            }
            builder.append(item);
        }
        return builder.toString();
    }

    /**
     * decides whether to accept user entry
     *
     * @param scanner the user input
     * @return the number if successfully checked and accepted
     */
    private int readValidFlightNumber(Scanner scanner) {
        while (true) {
            System.out.print("│ Flight Number: ");
            String input = scanner.nextLine();
            if (isValidFlightNumber(input)) {
                return Integer.parseInt(input.trim());
            }
            printRow("Error: Flight number must be a positive integer.");
        }
    }

    /**
     * decides whether to accept user entry
     * 
     * @param scanner the input being checked
     * @param prompt  the prompt its being checked with
     * @return true if boolean is true, false otherwise
     */
    private boolean readValidBoolean(Scanner scanner, String prompt) {
        while (true) {
            System.out.print("│ " + prompt + ": ");
            String input = scanner.nextLine();
            if (isValidBooleanInput(input)) {
                String clean = input.trim().toLowerCase();
                return clean.equals("y") || clean.equals("yes")
                        || clean.equals("true");
            }
            printRow("Error: Please enter 'y/yes' or 'n/no'.");
        }
    }

    /**
     * makes a selection of the items from the array
     * 
     * @param scanner the user input
     * @return
     */
    private ItemADT[] selectPresetItems(Scanner scanner) {
        List<ItemADT> chosen = new ArrayList<>();
        System.out.println("├──Select Items─────────────────────────┤");
        System.out.println("│                                       │");
        for (int i = 0; i < PRESET_ITEMS.length; i++) {
            System.out.printf("│ %-38s│%n", "[" + (i + 1) + "] " + PRESET_ITEMS[i].getName());
        }
        System.out.println("│ [0] Done                              │");
        System.out.println("│                                       │");

        while (true) {
            System.out.print("│ Item: ");
            String input = scanner.nextLine().trim();
            if (isValidFlightNumber(input) || input.equals("0")) {
                int choice = Integer.parseInt(input);
                if (choice == 0) {
                    break;
                }
                if (choice >= 1 && choice <= PRESET_ITEMS.length) {
                    chosen.add(PRESET_ITEMS[choice - 1]);
                    printRow("  + " + PRESET_ITEMS[choice - 1].getName());
                } else {
                    printRow("Error: Choice must be between 0 and " + PRESET_ITEMS.length + ".");
                }
            } else {
                printRow("Error: Please enter a valid number.");
            }
        }
        return chosen.toArray(new ItemADT[0]);
    }
}
