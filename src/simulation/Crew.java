package simulation;

/**
 * Represents an crew member in the TSA simulation.
 * Extends Person and adds employee ID and fit-to-fly status.
 * 
 * @author Team 32
 * @version Oct 12, 2026
 */
public class Crew extends Person
{
    // ~ Fields ................................................................
    private String employeeID;
    private boolean fitToFly;

    // ~ Constructors ..........................................................

    /**
     * Creates a new Crew member with employee credentials.
     * 
     * @param name
     * @param flightNumber
     * @param employeeID
     * @param fitToFly
     */
    public Crew(String name, int flightNumber, String employeeID, boolean fitToFly)
    {
        super(name, flightNumber);
        this.employeeID = employeeID;
        this.fitToFly = fitToFly;
    }

    // ~ Public Methods ........................................................

    public String getEmployeeID()
    {
        return employeeID;
    }

    public void setEmployeeID(String employeeID)
    {
        this.employeeID = employeeID;
    }

    public boolean isFitToFly()
    {
        return fitToFly;
    }

    public void setFitToFly(boolean fitToFly)
    {
        this.fitToFly = fitToFly;
    }

    @Override
    public String toString()
    {
        return super.toString() + " [Crew ID: " + employeeID + 
               ", Fit to Fly: " + fitToFly + "]";
    }
}