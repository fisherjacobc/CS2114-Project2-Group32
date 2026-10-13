package simulation;
import java.util.Scanner;
/**
 *  Runs the simulation from top to bottom, handles input from user, 
 *  presents choices A - D and makes sure it runs as it should
 *  @author emerson
 *  @version Oct 12, 2026
 */
public class Main
{
    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);
        TSASimulation simulation = new TSASimulation();
        boolean running = true;
        
        System.out.println("=== CS International Airport Simulation ==");
        while (running)
        {
            System.out.println("---MAIN MENU---");
            System.out.println("A: Add Random Passenger/Crew");
            System.out.println("B: Add Manual Passenger");
            System.out.println("C: Search and Check Passenger");
            System.out.println("D: End Simulation and Print Overview");
            System.out.print("Select choice: ");
            
            String choice = scanner.nextLine().trim().toUpperCase();
            switch (choice)
            {
                case "A":
                    simulation.addRandomPassenger();
                case "B":
                    simulation.addCustomPassenger(scanner);
                case "C":
                    simulation.searchAndCheckPassenger(scanner);
                case "D": 
                {
                    simulation.printSimulationOverview();
                    running = false;
                }
                default:
                    System.out.println("Invalid Input! Please enter"
                    + " A, B, C, or D to continue: ");
            }
            
        }
        scanner.close();

    }
    
}
