package model;

public class Shirt extends ClothingItem {

    private String sleeveType; // Example: "Short", "Long"

    public Shirt(int itemId, String name, String size, double price, String sleeveType) {
        super(itemId, name, size, price);
        setSleeveType(sleeveType);
    }

    public String getSleeveType() {
        return sleeveType;
    }

    // Setter throws exception for invalid data
    public void setSleeveType(String sleeveType) {
        if (sleeveType == null || sleeveType.trim().isEmpty()) {
            throw new IllegalArgumentException("Sleeve type cannot be empty.");
        }
        this.sleeveType = sleeveType.trim();
    }

    @Override
    public void wear() {
        System.out.println("Putting on shirt \"" + name + "\" (" + sleeveType + " sleeves).");
    }

    @Override
    public String toString() {
        return super.toString() + " | Sleeves: " + sleeveType;
    }
}
