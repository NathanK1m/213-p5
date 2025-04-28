/**
 * Enum class with the 5 addons and corresponding prices for each addon.
 * @author Ryan Bae
 */

package com.example.p5_213.model;

public enum AddOns {
    LETTUCE(0.30), TOMATOES(0.30), ONIONS(0.30), AVOCADO(0.50), CHEESE(1.00);

    private final double price;

    /**
     * Sets price to corresponding addon
     * @param price double
     */
    AddOns(double price) {
        this.price = price;
    }

    /**
     * Getter for price double
     * @return the price double
     */
    public double getPrice() {
        return price;
    }

}
