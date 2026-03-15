package ru.nsu.gaev.staff;

import ru.nsu.gaev.model.Order;
import ru.nsu.gaev.model.OrderState;
import ru.nsu.gaev.storage.Storage;

import java.util.List;

/**
 * Курьер - берет пиццы со склада и доставляет их клиентам.
 */
public class Courier implements Runnable {
    private final int id;
    private final int trunkCapacity;
    private final int deliveryTimeMs;
    private final Storage storage;
    private final List<Order> interruptedOrders;

    /**
     * Создает нового курьера.
     *
     * @param id                идентификатор курьера
     * @param trunkCapacity     вместимость багажника (макс. пицц за одну доставку)
     * @param deliveryTimeMs    время доставки (мс)
     * @param storage           склад готовой продукции
     * @param interruptedOrders список для сохранения прерванных заказов
     */
    public Courier(int id, int trunkCapacity, int deliveryTimeMs,
                   Storage storage, List<Order> interruptedOrders) {
        this.id = id;
        this.trunkCapacity = trunkCapacity;
        this.deliveryTimeMs = deliveryTimeMs;
        this.storage = storage;
        this.interruptedOrders = interruptedOrders;
    }

    @Override
    public void run() {
        List<Order> currentOrders = null;
        while (true) {
            try {
                currentOrders = storage.takeUpTo(trunkCapacity);
                if (currentOrders.isEmpty()) {
                    // Склад закрыт и пуст - прекращаем работу
                    break;
                }

                // Установка статуса "Доставляется"
                for (Order order : currentOrders) {
                    order.setState(OrderState.DELIVERING);
                }

                // Имитация доставки
                Thread.sleep(deliveryTimeMs);

                // Доставлено
                for (Order order : currentOrders) {
                    order.setState(OrderState.DELIVERED);
                }
                currentOrders = null; // Заказы успешно доставлены
            } catch (InterruptedException e) {
                System.out.println("Курьер #" + id + " прерван.");
                if (currentOrders != null) {
                    interruptedOrders.addAll(currentOrders);
                }
                Thread.currentThread().interrupt();
                break;
            }
        }
        System.out.println("Курьер #" + id + " закончил работу.");
    }

    /**
     * Возвращает идентификатор курьера.
     *
     * @return id курьера
     */
    public int getCourierId() {
        return id;
    }

    /**
     * Возвращает вместимость багажника.
     *
     * @return вместимость багажника
     */
    public int getTrunkCapacity() {
        return trunkCapacity;
    }

    /**
     * Возвращает время доставки в миллисекундах.
     *
     * @return время доставки в мс
     */
    public int getDeliveryTimeMs() {
        return deliveryTimeMs;
    }
}
