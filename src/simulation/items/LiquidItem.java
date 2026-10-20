package simulation.items;

public class LiquidItem extends ItemADT {
    public static final double MAX_FLUID_OZ = 3.4;

    private double fluidOz;

    public LiquidItem(String name, double fluidOz) {
        super(name);
        this.fluidOz = fluidOz;
    }

    public boolean isProhibited() {
        return fluidOz > MAX_FLUID_OZ;
    }

    public String getReason() {
        if (!isProhibited()) {
            return "This liquid is allowed";
        }

        return "This liquid is over the allowed fluid ounces amount: " + fluidOz + " > " + MAX_FLUID_OZ;
    }

    @Override
    public String toString() {
        return name + "[" + fluidOz + "oz]";
    }
}
