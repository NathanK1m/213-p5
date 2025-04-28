/**
 * Represents a Side menu item. Extends MenuItem class.
 * There are 4 side types (chips, fries, onionrings, appleslices) and 3 sizes (small, medium, large)
 * @author Ryan Bae
 */

package com.example.p5_213.model;

public class Side extends MenuItem {
    private SideType type;
    private Size size;

    /**
     * Constructor for the Side object.
     * @param type the type of side (chips, fries, onionrings, appleslices).
     * @param size the size of the side (small, medium, large).
     * @param quantity the amount of sides.
     */
    public Side(SideType type, Size size, int quantity) {
        super(quantity);
        this.type = type;
        this.size = size;
    }

    /**
     * Calculates the total price of the side based on side type and size.
     * @return the total price of the side.
     */
    @Override
    public double price() {
        double basePrice;
        switch (type) {
            case CHIPS:
                basePrice = 1.99;
                break;
            case FRIES:
                basePrice = 2.49;
                break;
            case ONION_RINGS:
                basePrice = 3.29;
                break;
            case APPLE_SLICES:
                basePrice = 1.29;
                break;
            default:
                basePrice = 0.0;
        }

        switch (size) {
            case MEDIUM:
                basePrice += 0.50;
                break;
            case LARGE:
                basePrice += 1.00;
                break;
            default:
                break;
        }

        return basePrice * quantity;
    }

    /**
     * Returns the side object in a String form with quantity, size, side type, and price.
     * @return the side object in a String form.
     */
    @Override
    public String toString() {
        return "Side [" + quantity + "] [" + size + ", " + type + "] [$" + String.format("%.2f", price()) + "]";
    }

    /**
     * getter for the side type.
     * @return the side type.
     */
    public SideType getType() {
        return type;
    }

    /**
     * getter for the side size/
     * @return the side size.
     */
    public Size getSize() {
        return size;
    }
}