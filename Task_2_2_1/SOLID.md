# Принципы SOLID в проекте Pizzeria

На основе разработанной архитектуры и диаграммы классов, можно выделить применение следующих принципов SOLID:

## SRP - Single Responsibility Principle (Принцип единственной ответственности)

Каждый класс в системе выполняет свою четко определенную задачу:
*   **Order**: Отвечает только за хранение данных заказа и его состояния.
*   **Baker**: Эмулирует работу пекаря (получение заказа, имитация готовки, передача на склад).
*   **Courier**: Эмулирует работу курьера (получение со склада, имитация доставки).
*   **SharedQueue**: Реализует потокобезопасную очередь с блокировками, не зная о бизнес-логике пиццерии.
*   **Storage**: Обертка над очередью, специализирующаяся на хранении заказов и управлении вместимостью склада.
*   **PizzeriaConfig**: DTO (Data Transfer Object) для хранения конфигурации.
*   **Pizzeria**: Управляет жизненным циклом компонентов системы (запуск, остановка).

## OCP - Open/Closed Principle (Принцип открытости/закрытости)

*   **PizzeriaConfig**: Класс конфигурации отделен от логики. Мы можем менять параметры работы (количество пекарей, время, вместимость) через JSON-файл или изменяя класс конфигурации, не переписывая логику работы Pizzeria.
*   **SharedQueue<T>**: Написана как обобщенный (generic) класс. Она открыта для использования с любыми типами данных, не только с `Order`, при этом ее код менять не нужно.

## LSP - Liskov Substitution Principle (Принцип подстановки Барбары Лисков)

*   В данном проекте иерархия наследования невелика. Однако, классы **Baker** и **Courier** реализуют интерфейс `Runnable`. Это позволяет использовать их взаимозаменяемо в любом контексте, где ожидается `Runnable` (например, при создании `Thread`), что соответствует принципу подстановки.

## ISP - Interface Segregation Principle (Принцип разделения интерфейса)

*   Классы **Baker** и **Courier** реализуют стандартный интерфейс `Runnable`. Они не вынуждены реализовывать методы, которые им не нужны.
*   Взаимодействие между компонентами происходит через узкоспециализированные методы. Например, `Storage` предоставляет методы `put` (используется пекарем) и `takeUpTo` (используется курьером). Хотя они находятся в одном классе, логически интерфейс использования разделен.

## DIP - Dependency Inversion Principle (Принцип инверсии зависимостей)

*   Модули верхнего уровня (**Pizzeria**) зависят от абстракций конфигурации.
*   **Baker** и **Courier** зависят от `SharedQueue` и `Storage`. Хотя это конкретные классы, они выступают в роли абстракции механизма синхронизации и передачи данных. Пекарь не знает, как именно реализована блокировка внутри `SharedQueue`, он просто вызывает `take()`.
*   Использование `Runnable` для запуска потоков также является примером инверсии зависимости: `Thread` зависит от интерфейса `Runnable`, а не от конкретных классов работников.
classDiagram
    class Main {
        +main(String[] args)
    }

    class Pizzeria {
        -PizzeriaConfig config
        -SharedQueue~Order~ orderQueue
        -Storage storage
        -List~Thread~ bakerThreads
        -List~Thread~ courierThreads
        -List~Order~ interruptedOrders
        -AtomicInteger orderCounter
        -boolean acceptingOrders
        +Pizzeria(PizzeriaConfig config)
        +start()
        +placeOrder() Order
        +shutdown()
        +isAcceptingOrders() boolean
        +static loadConfig(String path) PizzeriaConfig
    }

    class PizzeriaConfig {
        -BakerConfig[] bakers
        -CourierConfig[] couriers
        -int storageCapacity
        -int workingTimeMs
        +getBakers() BakerConfig[]
        +getCouriers() CourierConfig[]
        +getStorageCapacity() int
        +getWorkingTimeMs() int
    }

    class BakerConfig {
        -int id
        -int cookingTimeMs
        +getId() int
        +getCookingTimeMs() int
    }

    class CourierConfig {
        -int id
        -int trunkCapacity
        -int deliveryTimeMs
        +getId() int
        +getTrunkCapacity() int
        +getDeliveryTimeMs() int
    }

    class Order {
        -int id
        -OrderState state
        +Order(int id)
        +getId() int
        +getState() OrderState
        +setState(OrderState state)
        +toString() String
    }

    class OrderState {
        <<enumeration>>
        QUEUED
        COOKING
        COOKED
        IN_STORAGE
        DELIVERING
        DELIVERED
        -String description
        +getDescription() String
    }

    class Baker {
        -int id
        -int cookingTimeMs
        -SharedQueue~Order~ orderQueue
        -Storage storage
        -List~Order~ interruptedOrders
        +run()
        +getBakerId() int
        +getCookingTimeMs() int
    }

    class Courier {
        -int id
        -int trunkCapacity
        -int deliveryTimeMs
        -Storage storage
        -List~Order~ interruptedOrders
        +run()
        +getCourierId() int
        +getTrunkCapacity() int
        +getDeliveryTimeMs() int
    }

    class SharedQueue~T~ {
        -LinkedList~T~ queue
        -int capacity
        -boolean closed
        +put(T item) boolean
        +take() T
        +takeUpTo(int maxCount) List~T~
        +close()
        +isClosed() boolean
        +isClosedAndEmpty() boolean
        +size() int
        +drainAll() List~T~
    }

    class Storage {
        -SharedQueue~Order~ queue
        -int capacity
        +put(Order order) boolean
        +takeUpTo(int maxCount) List~Order~
        +close()
        +isClosedAndEmpty() boolean
        +isClosed() boolean
        +size() int
        +getCapacity() int
        +drainAll() List~Order~
    }

    Main ..> Pizzeria : creates
    Main ..> PizzeriaConfig : loads
    Pizzeria --> PizzeriaConfig : uses
    Pizzeria *-- SharedQueue : owns
    Pizzeria *-- Storage : owns
    Pizzeria ..> Baker : creates
    Pizzeria ..> Courier : creates
    PizzeriaConfig *-- BakerConfig : contains
    PizzeriaConfig *-- CourierConfig : contains
    Baker ..|> Runnable : implements
    Courier ..|> Runnable : implements
    Baker --> SharedQueue : consumes
    Baker --> Storage : produces
    Courier --> Storage : consumes
    Storage *-- SharedQueue : uses
    Order *-- OrderState : has
    SharedQueue ..> Order : stores

