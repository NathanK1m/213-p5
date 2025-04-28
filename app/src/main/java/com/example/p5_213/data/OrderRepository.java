package com.example.p5_213.data;

import java.util.ArrayList;
import com.example.p5_213.model.*;

public final class OrderRepository {
    private static final OrderRepository INST = new OrderRepository();
    private final ArrayList<Order> history = new ArrayList<>();
    private Order current = new Order();      // active cart
    private int nextNo = 1;
    private OrderRepository() {}
    public static OrderRepository get() { return INST; }

    public Order current() { return current; }
    public java.util.List<Order> history() { return history; }

    public void place() {
        if (current.getItems().isEmpty()) return;
        current.setNumber(nextNo++);
        history.add(current);
        current = new Order();
    }
}
