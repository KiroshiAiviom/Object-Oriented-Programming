package model;

/**
 * Child class: Shirt.
 * Extra field: sleeveType.
 */
public class Shirt extends ClothingItem {

    private String sleeveType; // e.g. "Short", "Long"

    public Shirt(int itemId, String name, String size, double price, String sleeveType) {
        super(itemId, name, size, price);
        setSleeveType(sleeveType);
    }

    @Override
    public String getType() {
        return "SHIRT";
    }

    public String getSleeveType() {
        return sleeveType;
    }

    public void setSleeveType(String sleeveType) {
        if (sleeveType == null || sleeveType.trim().isEmpty()) {
            throw new IllegalArgumentException("sleeveType cannot be empty.");
        }
        this.sleeveType = sleeveType.trim();
    }

    @Override
    public String toString() {
        return super.toString() + " | Sleeves: " + sleeveType;
    }
}
