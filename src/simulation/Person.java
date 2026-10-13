package simulation;

/**
 * An abstract class representing a person (passenger or crew member).
 * 
 * @author Team 32
 * @version Oct 12, 2026
 */
public abstract class Person
{
    // ~ Fields ................................................................
    private String name;
    private int flightNumber;

    // ~ Constructors ..........................................................

    /**
     * Creates a new Person with the specified name and flight number.
     * 
     * @param name
     * @param flightNumber
     */
    public Person(String name, int flightNumber)
    {
        this.name = name;
        this.flightNumber = flightNumber;
    }

    // ~ Public Methods ........................................................

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getFlightNumber()
    {
        return flightNumber;
    }

    public void setFlightNumber(int flightNumber)
    {
        this.flightNumber = flightNumber;
    }

    @Override
    public String toString()
    {
        return "Name: " + name + ", Flight #: " + flightNumber;
    }
}