package ru.nsu.gaev.model;

/**
 * Возможные состояния заказа в пиццерии.
 */
public enum OrderState {
    /** Заказ ожидает в очереди. */
    QUEUED("В очереди"),
    /** Заказ готовится пекарем. */
    COOKING("Готовится"),
    /** Пицца приготовлена и готова к отправке на склад. */
    COOKED("Приготовлено"),
    /** Пицца находится на складе. */
    IN_STORAGE("На складе"),
    /** Пицца доставляется курьером. */
    DELIVERING("Доставляется"),
    /** Пицца доставлена клиенту. */
    DELIVERED("Доставлено");

    private final String description;

    OrderState(String description) {
        this.description = description;
    }

    /**
     * Возвращает человекочитаемое описание состояния.
     *
     * @return описание состояния
     */
    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return description;
    }
}
