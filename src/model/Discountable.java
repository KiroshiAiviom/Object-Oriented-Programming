package model;

/**
 * Second interface (Week 6 requirement: at least 2 interfaces in the project).
 * <p>
 * Simple and useful:
 * - formatted price for printing
 * - discounted price calculation (does not change stored price)
 */
public interface Discountable {

    double getDiscountedPrice(double percent);

    String getPriceLabel();
}
