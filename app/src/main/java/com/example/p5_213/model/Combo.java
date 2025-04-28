/**
 * Represents a Combo meal that adds a small drink and a medium side for $2 on top of the chosen sandwich or burger.
 * @author Ryan Bae
 */
package com.example.p5_213.model;

public class Combo extends MenuItem {
    private Sandwich sandwich;
    private Beverage drink;
    private Side side;

    /**
     * Constructor for the Combo class
     * @param sandwich the Sandwich item to include in the combo.
     * @param drink the Beverage item to include in the combo.
     * @param side the Side item to include in the combo.
     * @param quantity the number of combos.
     */
    public Combo(Sandwich sandwich, Beverage drink, Side side, int quantity) {
        super(quantity);
        this.sandwich = sandwich;
        this.drink = drink;
        this.side = side;
    }

    /**
     * Calculates the total price of the combo based on the quantity and the additional $2.
     * @return the total combo price.
     */
    @Override
    public double price() {
        double comboBase = sandwich.price() + 2.00;
        return comboBase * quantity;
    }

    /**
     * Returns the combo object in a String form with quantity, sandwich type, drink flavor, side type, and price.
     * @return the combo object in a String form.
     */
    public String toString() {
        return "Combo [" + quantity + "] " + sandwich.toString() + " [" + drink.getFlavor() + ", " + side.getType() + "] [$" + String.format("%.2f", price()) + "]";
    }

    /**
     * Getter for Sandwich in the combo.
     * @return the sandwich menu item.
     */
    public Sandwich getSandwich() {
        return sandwich;
    }

    /**
     * Getter for Beverage in the combo.
     * @return the Beverage menu item.
     */
    public Beverage getDrink() {
        return drink;
    }

    /**
     * Getter for Side in the combo.
     * @return the Side menu item.
     */
    public Side getSide() {
        return side;
    }
}
