package model;

public abstract class ClothingItem implements Priced {

    // Minimum 4 protected fields (inherited by child classes)
    protected int itemId;
    protected String name;
    protected String size;
    protected double price;

    /**
     * Constructor uses setters so validation is always applied.
     */
    public ClothingItem(int itemId, String name, String size, double price) {
        setItemId(itemId);
        setName(name);
        setSize(size);
        setPrice(price);
    }

    public abstract void wear();

    // ----------------------
    // Getters
    // ----------------------

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

    // ----------------------
    // Setters with validation
    // ----------------------

    public void setItemId(int itemId) {
        if (itemId <= 0) {
            throw new IllegalArgumentException("Item ID must be positive: " + itemId);
        }
        this.itemId = itemId;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty.");
        }
        this.name = name.trim();
    }

    public void setSize(String size) {
        if (size == null || size.trim().isEmpty()) {
            throw new IllegalArgumentException("Size cannot be empty.");
        }
        this.size = size.trim();
    }

    public void setPrice(double price) {
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative: " + price);
        }
        this.price = price;
    }

    public boolean isPremium() {
        return price > 20000.0;
    }

    // ----------------------
    // Priced interface implementation
    // ----------------------

    @Override
    public String priceLabel() {
        return String.format("%.2f KZT", price);
    }

    @Override
    public String toString() {
        // getClass().getSimpleName() avoids extra "getType()" method in children
        return "[" + getClass().getSimpleName() + "] " + name +
                " (ID: " + itemId +
                ", Size: " + size +
                ", Price: " + priceLabel() + ")";
    }
}
