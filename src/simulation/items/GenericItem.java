package simulation.items;

public class GenericItem extends ItemADT {
    protected boolean isProhibited;

    public GenericItem(String name, boolean isProhibited) {
        super(name);
        this.isProhibited = isProhibited;
    }

    public boolean isProhibited() {
        if (isProhibited) {
            return true;
        }

        return false;
    }

    public String getReason() {
        if (!isProhibited()) {
            return "This item is allowed";
        }

        return "This item is a prohibited item";
    }
}
