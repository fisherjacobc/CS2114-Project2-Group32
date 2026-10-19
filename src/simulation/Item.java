package simulation;
public class Item
{
    private String name;
    private String material;
    private double liquidOunces;
    private boolean isProhibitedType;

    public static final double LIQUID_LIMIT_OZ = 3.4;

    
    /**
     * Create a new Item object.
     * @param name name of the item
     * @param material item material
     * @param liquidOunces ounces item holds (0.0 if not applicable)
     * @param isProhibitedType is it prohibited? true or false 
     */
    public Item(String name, String material, double liquidOunces, 
            boolean isProhibitedType) {
        this.name = name;
        this.material = material;
        this.liquidOunces = liquidOunces;
        this.isProhibitedType = isProhibitedType;
    }

    /**
     * Checks if the item violates TSA safety rules.
     * @return true if prohibited, false if not prohibited item
     */
    public boolean isProhibited() {
        if (isProhibitedType) {
            return true;
        }
        if (liquidOunces > LIQUID_LIMIT_OZ) { // > 3.4 oz
            return true;
        }

        String nameClean = (name != null) ? name.toLowerCase() : "";
        String matClean = (material != null) ? material.toLowerCase() : "";

        // Flag if name or material contains metal, knife, or explosive
        if (nameClean.contains("metal") || matClean.contains("metal") ||
            nameClean.contains("knife") || nameClean.contains("prohibited")) {
            return true;
        }

        return false;
    }
    
    /**
     * gets reason for item not allowed
     * @return the reason of why an item is not allowed
     */
    public String getReason()
    {
        if (isProhibitedType)
        {
            if (liquidOunces > LIQUID_LIMIT_OZ)
            {
               return "Over the liquid limit: " + name;
            }
            else if (material.equals("metal"))
            {
                return "Prohibited material type: " + name;
            }
        }
        return ("Forbidden Item: " + name);
        
    }
    
    /**
     * Place a description of your method here.
     * @return name of the item
     */
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name + " [" + material + ", " + liquidOunces + " oz]";
    }
}
