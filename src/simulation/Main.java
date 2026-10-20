package simulation;

import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Runs the simulation from top to bottom, handles input from user,
 * presents choices A - D and makes sure it runs as it should
 *
 * @author emerson
 * @version Oct 12, 2026
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        TSASimulation simulation = new TSASimulation();
        boolean running = true;

        while (running) {
            clearScreen();

            System.out.println("┌──CS International Airport─────────────┐");
            System.out.println("│                                       │");
            System.out.println("│ [A] Add Random Passenger/Crew         │");
            System.out.println("│ [B] Add Manual Passenger              │");
            System.out.println("│ [C] Search and Check Passenger        │");
            System.out.println("│ [D] End Simulation and Print Overview │");
            System.out.println("│                                       │");
            System.out.println("├───────────────────────────────────────┘");
            System.out.print("└ Choice: ");

            String choice = scanner.nextLine().trim().toUpperCase();
            switch (choice) {
                case "A":
                    simulation.addRandomPassenger();
                    pause(scanner);
                    break;
                case "B":
                    simulation.addCustomPassenger(scanner);
                    pause(scanner);
                    break;
                case "C":
                    simulation.searchAndCheckPassenger(scanner);
                    pause(scanner);
                    break;
                case "D": {
                    clearScreen();
                    simulation.printSimulationOverview();
                    running = false;
                    break;
                }
                default:
                    System.out.println("Invalid Input! Please enter"
                            + " A, B, C, or D to continue.");
                    pause(scanner);
            }

        }
        scanner.close();

    }

    /**
     * Pauses until the user presses Enter.
     *
     * @param scanner the user input
     */
    private static void pause(Scanner scanner) {
        System.out.println();
        System.out.print("Press Enter to continue...");
        try {
            scanner.nextLine();
        } catch (NoSuchElementException e) {
            // No more input, so continue on
        }
    }

    /**
     * Clears the terminal screen.
     */
    private static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

}
