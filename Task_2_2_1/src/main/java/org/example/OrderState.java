package org.example;

/**
 * Possible states of an order in the pizzeria.
 */
public enum OrderState {
    /** Order is waiting in the queue. */
    QUEUED("Queued"),
    /** Order is being cooked by a baker. */
    COOKING("Cooking"),
    /** Pizza is cooked and ready for storage. */
    COOKED("Cooked"),
    /** Pizza is stored in the storage. */
    IN_STORAGE("In Storage"),
    /** Pizza is being delivered by a courier. */
    DELIVERING("Delivering"),
    /** Pizza has been delivered to the customer. */
    DELIVERED("Delivered");

    private final String description;

    OrderState(String description) {
        this.description = description;
    }

    /**
     * Returns the human-readable description of the state.
     *
     * @return state description
     */
    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return description;
    }
}
