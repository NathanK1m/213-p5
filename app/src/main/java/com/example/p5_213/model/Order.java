/**
 * Represents the customer's order which contains the list of all placed orders.
 * The class can add or remove items from the order.
 * Calculates the subtotal, tax,and the total amount of the order.
 * @author Ryan Bae
 */


package com.example.p5_213.model;

import java.util.ArrayList;

public class Order {
    private int number;
    private ArrayList<MenuItem> items;
    private static final double TAX = 0.06625; //The tax is 6.625%

    /**
     * Default constructor for an Order object
     */
    public Order() {
        this.items = new ArrayList<>();
    }

    /**
     * Constructor for an Order object that takes in the number of the order.
     * @param number the number of the order.
     */
    public Order(int number) {
        this.number = number;
        this.items = new ArrayList<>();

    }

    /**
     * Setter for the current order number.
     * @param number the order number.
     */
    public void setNumber(int number) {
        this.number = number;
    }

    /**
     * Adds a menu item to the order.
     * @param item the menu item object.
     */
    public void addItem(MenuItem item) {
        items.add(item);
    }

    /**
     * Removes a menu item from the order based on the order number.
     * @param index of the order in the list to remove.
     */
    public void removeItem(int index) {
        if (index >= 0 && index < items.size()) {
            items.remove(index);
        }
    }

    /**
     * Getter for the list of menu items in the order.
     * @return
     */
    public ArrayList<MenuItem> getItems() {
        return items;
    }

    /**
     * getter for the order number
     * @return the order number
     */
    public int getNumber() {
        return number;
    }

    /**
     * Calcualtes the subtotal of the order by adding the prices of all itemms.
     * @return the subtotal of the order as a double.
     */
    public double getSubtotal() {
        double subtotal = 0.0;
        for (MenuItem item : items) {
            subtotal += item.price();
        }
        return subtotal;
    }

    /**
     * Calculates the tax of the order based on the 6.625% tax.
     * @return the tax of the order as a double.
     */
    public double getTax() {
        return getSubtotal() * TAX;
    }

    /**
     * Calculates the total price of the order based on the 6.625% tax.
     * @return the total price of the order as a double.
     */
    public double getTotal() {
        return getSubtotal() + getTax();
    }

    /**
     * Returns the order object in a String form with order number, menu items, and total price.
     * @return the order object in a String form.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Order #").append(this.getNumber()).append("\n");
        sb.append("Items:\n");
        for (MenuItem item : items) {
            sb.append("- ").append(item.toString()).append("\n");
        }
        sb.append("Total: $").append(String.format("%.2f", getTotal())).append("\n");
        return sb.toString();
    }

}