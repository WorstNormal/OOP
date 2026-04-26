package ru.nsu.gaev.model;

/**
 * Заказ пиццы.
 */
public class Order {
    private final int id;
    private volatile OrderState state;

    /**
     * Создает новый заказ с заданным id.
     *
     * @param id идентификатор заказа
     */
    public Order(int id) {
        this.id = id;
        this.state = OrderState.QUEUED;
    }

    /**
     * Возвращает идентификатор заказа.
     *
     * @return id заказа
     */
    public int getId() {
        return id;
    }

    /**
     * Возвращает текущее состояние заказа.
     *
     * @return текущее состояние
     */
    public OrderState getState() {
        return state;
    }

    /**
     * Устанавливает состояние заказа и выводит сообщение о статусе.
     *
     * @param state новое состояние
     */
    public void setState(OrderState state) {
        this.state = state;
        System.out.println("[Заказ #" + id + "] " + state);
    }

    @Override
    public String toString() {
        return "Order{id=" + id + ", state=" + state + "}";
    }
}
