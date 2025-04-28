/**
 * Represents MenuItems as an abstract class that holds information about the quantity of the menu item.
 * Defines information for all menu items.
 * Has getter and setter methods for the quantity variable.
 * @author Nathan Kim
 */

package com.example.p5_213.model;

public abstract class MenuItem {
    protected int quantity;
    public abstract double price();

    /**
     * Constructor for MenuItem class
     * @param quantity double
     */
    public MenuItem(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Getter for quantity double
     * @return quantity double
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Setter for quantity double
     * @param quantity double
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}