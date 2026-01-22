package model;

public class Jacket extends ClothingItem {

    private String season; // Example: "Winter", "Autumn"

    public Jacket(int itemId, String name, String size, double price, String season) {
        super(itemId, name, size, price);
        setSeason(season);
    }

    public String getSeason() {
        return season;
    }

    // Setter throws exception for invalid data
    public void setSeason(String season) {
        if (season == null || season.trim().isEmpty()) {
            throw new IllegalArgumentException("Season cannot be empty.");
        }
        this.season = season.trim();
    }

    @Override
    public void wear() {
        System.out.println("Wearing jacket \"" + name + "\" (Season: " + season + ").");
    }

    @Override
    public String toString() {
        return super.toString() + " | Season: " + season;
    }
}
