package model;

/**
 * Child class: Jacket.
 * Extra field: season.
 */
public class Jacket extends ClothingItem {

    private String season; // e.g. "Winter", "Autumn", "Spring"

    public Jacket(int itemId, String name, String size, double price, String season) {
        super(itemId, name, size, price);
        setSeason(season);
    }

    @Override
    public String getType() {
        return "JACKET";
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        if (season == null || season.trim().isEmpty()) {
            throw new IllegalArgumentException("season cannot be empty.");
        }
        this.season = season.trim();
    }

    @Override
    public String toString() {
        return super.toString() + " | Season: " + season;
    }
}
