/**
 * Represents a Sandwich menu item. Extends the MenuItem class.
 * Each Sandwich has 3 protein options (roast beef, salmon, chicken), 5 bread options (Brioche,Wheat,Pretzel,Bagel,Sourdough), and an optional 5 different addons.
 * @author Nathan Kim
 */

package com.example.p5_213.model;
import java.util.ArrayList;

public class Sandwich extends MenuItem {
    protected Bread bread;
    protected Protein protein;
    protected ArrayList<AddOns> addOns;

    /**
     * Constructor for the Sandwich object.
     * @param bread the type of bread (Brioche,Wheat,Pretzel,Bagel,Sourdough).
     * @param protein the type of protein (roast beef, salmon, chicken).
     * @param addOns a list of 5 different addons.
     * @param quantity the amount of sandwiches.
     */
    public Sandwich(Bread bread, Protein protein, ArrayList<AddOns> addOns, int quantity) {
        super(quantity);
        this.bread = bread;
        this.protein = protein;
        this.addOns = addOns;
    }

    /**
     * Calculates the total price of the sandwich based on protein type, addons, and quantity.
     * @return the total price of the burger.
     */
    @Override
    public double price() {
        double base = 0;
        switch (protein) {
            case ROAST_BEEF:
                base = 10.99;
                break;
            case CHICKEN:
                base = 8.99;
                break;
            case SALMON:
                base = 9.99;
                break;
        }

        for (AddOns add : addOns) {
            base += add.getPrice();
        }

        return base * quantity;
    }

    /**
     * Returns the sandwich object in a String form with quantity, bread type, protein type, addons, and price.
     * @return the sandwich object in a String form.
     */
    @Override
    public String toString() {
        return "Sandwich [" + quantity + "] [" + bread + ", " + protein + ", " + addOns + "] [$" + String.format("%.2f", price()) + "]";
    }
}
