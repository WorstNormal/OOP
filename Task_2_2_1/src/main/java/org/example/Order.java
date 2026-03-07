package org.example;

/**
 * Pizza order.
 */
public class Order {
    private final int id;
    private volatile OrderState state;

    /**
     * Creates a new order with the given id.
     *
     * @param id order identifier
     */
    public Order(int id) {
        this.id = id;
        this.state = OrderState.QUEUED;
    }

    /**
     * Returns the order identifier.
     *
     * @return order id
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the current state of the order.
     *
     * @return current state
     */
    public OrderState getState() {
        return state;
    }

    /**
     * Sets the state of the order and prints a status message.
     *
     * @param state new state
     */
    public void setState(OrderState state) {
        this.state = state;
        System.out.println("[Order #" + id + "] " + state);
    }

    @Override
    public String toString() {
        return "Order{id=" + id + ", state=" + state + "}";
    }
}
