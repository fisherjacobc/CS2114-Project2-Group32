package simulation.items;

public abstract class ItemADT {
    protected String name;

    /**
     * Create a new Item object.
     * 
     * @param name name of the item
     */
    public ItemADT(String name) {
        this.name = name;
    }

    /**
     * Checks if the item violates TSA safety rules.
     * 
     * @return true if prohibited, false if not prohibited item
     */
    public abstract boolean isProhibited();

    /**
     * gets reason for item not allowed
     * 
     * @return the reason of why an item is not allowed
     */
    public abstract String getReason();

    /**
     * Place a description of your method here.
     * 
     * @return name of the item
     */
    public String getName() {
        return name;
    }

    /**
     * String representation
     */
    @Override
    public String toString() {
        return name;
    }
}
