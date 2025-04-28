/**
 * Represents the Burger object menu item. Extends the Sandwich class.
 * Each Burger has a single or double patty, a choice between 3 pieces of bread (Brioche, Wheat, Pretzel), and an optional 5 different addons.
 * @author Nathan Kim
 */

package com.example.p5_213.model;
import java.util.ArrayList;

public class Burger extends Sandwich {
    private boolean doublePatty;

    /**
     * Constructor for the Burger object
     * @param bread the type of bread (Brioche, Wheat, Pretzel).
     * @param doublePatty false if single, true if double patty.
     * @param addOns a list of the 5 different addons.
     * @param quantity the amount of burgers.
     */
    public Burger(Bread bread, boolean doublePatty, ArrayList<AddOns> addOns, int quantity) {
        super(bread, Protein.BEEF, addOns, quantity);
        this.doublePatty = doublePatty;
    }

    /**
     * Calculates the total price of the burger based on patty type, addons, and quantity.
     * @return the total price of the burger.
     */
    @Override
    public double price() {
        double base = 6.99;
        if (doublePatty){
            base += 2.50;
        }

        for (AddOns add : addOns) {
            base += add.getPrice();
        }

        return base * quantity;
    }

    /**
     * Returns the burger object in a String form with quantity, bread type, patty type, addons, and price.
     * @return the burger object in a String form.
     */
    @Override
    public String toString() {
        String pattyType;
        if (doublePatty) {
            pattyType = "double";
        } else {
            pattyType = "single";
        }
        return "Burger [" + quantity + "] [" + bread + ", " + pattyType + " patty, " + addOns + "] [$" + String.format("%.2f", price()) + "]";
    }

}
