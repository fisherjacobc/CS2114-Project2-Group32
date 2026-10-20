package simulation.items;

import java.util.Random;

import simulation.Passenger;

public class IDItem extends ItemADT {
    private static Random random = new Random();

    private Passenger passenger;
    private String idNumber;

    public IDItem(String name, Passenger passenger, boolean isReal) {
        super(name);
        this.passenger = passenger;

        // Random letter to let passengers with the same name have a "unique" ID #
        if (isReal) {
            idNumber = (char) ('A' + random.nextInt(6)) + "" + passenger.getName().hashCode();
        } else {
            idNumber = (char) ('G' + random.nextInt(19)) + "" + random.nextInt(1 << 28);
        }
    }

    public boolean isProhibited() {
        char firstChar = idNumber.charAt(0);

        if (!(firstChar >= 'A' && firstChar <= 'F')) {
            return true;
        }

        if (!idNumber.substring(1).equals("" + passenger.getName().hashCode())) {
            return true;
        }

        return false;
    }

    public String getReason() {
        if (!isProhibited()) {
            return "This is a real and allowed ID";
        }

        return "This is a fake ID";
    }

    @Override
    public String toString() {
        return name + "[" + idNumber + "]";
    }
}
