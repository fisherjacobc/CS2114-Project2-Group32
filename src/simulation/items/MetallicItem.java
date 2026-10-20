package simulation.items;

public class MetallicItem extends GenericItem {
    public MetallicItem(String name, boolean isProhibited) {
        super(name, isProhibited);
    }

    @Override
    public boolean isProhibited() {
        if (super.isProhibited()) {
            return true;
        }

        String loweredName = name.toLowerCase();
        if (loweredName.contains("metal") || loweredName.contains("weapon") || loweredName.contains("prohibited")
                || loweredName.contains("battery")) {
            return true;
        }

        return false;
    }

    @Override
    public String getReason() {
        if (!isProhibited()) {
            return "This item is allowed";
        }

        return "This item is a prohibited item";
    }
}
