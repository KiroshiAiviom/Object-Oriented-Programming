package model;

/**
 * Abstract parent class (Week 6 requirement).
 * Shirt and Jacket extend this.
 * <p>
 * Stored in ONE DB table using a 'type' column.
 */
public abstract class ClothingItem implements Discountable {

    // Protected fields are accessible in child classes (Week 4 style).
    protected int itemId;
    protected String name;
    protected String size;
    protected double price;

    protected ClothingItem(int itemId, String name, String size, double price) {
        // Centralized validation through setters (Week 6: throw exceptions).
        setItemId(itemId);
        setName(name);
        setSize(size);
        setPrice(price);
    }

    /**
     * Used for DB mapping ('SHIRT' or 'JACKET').
     * Also demonstrates polymorphism (child overrides).
     */
    public abstract String getType();

    // ------------------ Getters ------------------

    public int getItemId() {
        return itemId;
    }

    public String getName() {
        return name;
    }

    public String getSize() {
        return size;
    }

    public double getPrice() {
        return price;
    }

    // ------------------ Setters with validation ------------------
    // Week 6 requirement: invalid values -> throw exception (no print warnings)

    public void setItemId(int itemId) {
        if (itemId <= 0) {
            throw new IllegalArgumentException("itemId must be positive.");
        }
        this.itemId = itemId;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("name cannot be empty.");
        }
        this.name = name.trim();
    }

    public void setSize(String size) {
        if (size == null || size.trim().isEmpty()) {
            throw new IllegalArgumentException("size cannot be empty.");
        }
        this.size = size.trim();
    }

    public void setPrice(double price) {
        if (price < 0) {
            throw new IllegalArgumentException("price cannot be negative.");
        }
        this.price = price;
    }

    // ------------------ Discountable ------------------

    @Override
    public double getDiscountedPrice(double percent) {
        if (percent < 0 || percent > 100) {
            throw new IllegalArgumentException("discount percent must be between 0 and 100.");
        }
        return price * (1 - percent / 100.0);
    }

    @Override
    public String getPriceLabel() {
        return String.format("%.2f KZT", price);
    }

    @Override
    public String toString() {
        // Polymorphism: getType() comes from actual child class.
        return "[" + getType() + "] " + name +
                " (ID: " + itemId +
                ", Size: " + size +
                ", Price: " + getPriceLabel() + ")";
    }
}
