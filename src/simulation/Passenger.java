package simulation;

import simulation.items.ItemADT;

/**
 * Represents a passenger in the TSA simulation.
 * Extends Person and adds pre-check, secondary screening flags, and items.
 * 
 * @author Team 32
 * @version Oct 20, 2026
 */
public class Passenger extends Person {
    // ~ Fields ................................................................
    private boolean isPreCheck;
    private boolean isSecondaryScreening;
    private ItemADT[] items;

    // ~ Constructors ..........................................................

    /**
     * Creates a new Passenger with specific attributes and item list.
     * 
     * @param name
     * @param flightNumber
     * @param isPreCheck
     * @param isSecondaryScreening
     * @param items
     */
    public Passenger(
            String name,
            int flightNumber,
            boolean isPreCheck,
            boolean isSecondaryScreening,
            ItemADT[] items) {
        super(name, flightNumber);
        this.isPreCheck = isPreCheck;
        this.isSecondaryScreening = isSecondaryScreening;
        this.items = items;
    }

    // ~ Public Methods ........................................................

    public boolean isPreCheck() {
        return isPreCheck;
    }

    public void setPreCheck(boolean isPreCheck) {
        this.isPreCheck = isPreCheck;
    }

    public boolean isSecondaryScreening() {
        return isSecondaryScreening;
    }

    public void setSecondaryScreening(boolean isSecondaryScreening) {
        this.isSecondaryScreening = isSecondaryScreening;
    }

    public ItemADT[] getItems() {
        return items;
    }

    public void setItems(ItemADT[] items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return super.toString() + " [PreCheck: " + isPreCheck +
                ", Secondary Screening: " + isSecondaryScreening + "]";
    }
}