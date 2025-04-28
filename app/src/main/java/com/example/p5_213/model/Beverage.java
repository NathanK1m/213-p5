/**
 * Represents a drink item and extends the MenuItem.
 * Each Beverage has a size and flavor, choosing from 3 sizes (small, medium, large) and 15 flavors.
 * @author Ryan Bae
 */

package com.example.p5_213.model;

public class Beverage extends MenuItem {
    private Size size;
    private Flavor flavor;

    /**
     * Beverage object constructor with specified size, flavor, and quantity.
     * @param size the size of the beverage (small, medium, large).
     * @param flavor one of the 15 flavors of the beverage.
     * @param quantity the amount of beverages.
     */
    public Beverage(Size size, Flavor flavor, int quantity) {
        super(quantity);
        this.size = size;
        this.flavor = flavor;
    }

    /**
     * Calculates the total price based on size and quantity.
     * @return the total price of the beverage order.
     */
    @Override
    public double price() {
        double unitPrice = switch (size) {
            case SMALL -> 1.99;
            case MEDIUM -> 2.49;
            case LARGE -> 2.99;
        };
        return unitPrice * quantity;
    }

    /**
     * Returns the Beverage object in a String form with quantity, size, flavor, and price.
     * @return the Beverage object in a String form.
     */
    @Override
    public String toString() {
        return "Beverage [" + quantity + "] [" + size + ", " + flavor + "] [$" + String.format("%.2f", price()) + "]";
    }

    /**
     * Getter for size.
     * @return size of beverage.
     */
    public Size getSize() {
        return size;
    }

    /**
     * Getter for flavor.
     * @return flavor of beverage.
     */
    public Flavor getFlavor() {
        return flavor;
    }

}