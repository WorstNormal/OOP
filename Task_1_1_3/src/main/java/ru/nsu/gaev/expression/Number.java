package ru.nsu.gaev.expression;

import java.util.Map;

/**
 * Класс, представляющий числовое значение в выражении.
 * Является конечным элементом дерева выражений.
 */
public class Number extends Expression {

    /** Числовое значение. */
    private final int value;

    /**
     * Создаёт объект {@code Number} с указанным значением.
     *
     * @param value числовое значение выражения
     */
    public Number(int value) {
        this.value = value;
    }

    /**
     * Возвращает числовое значение.
     *
     * @return значение числа
     */
    public int getValue() {
        return value;
    }

    /**
     * Возвращает числовое значение при вычислении.
     * Поскольку {@code Number} не зависит от переменных, результат всегда равен {@code value}.
     *
     * @param vars отображение переменных и их значений (игнорируется)
     * @return значение числа
     */
    @Override
    public int eval(Map<String, Integer> vars) {
        return value;
    }

    /**
     * Возвращает производную числа.
     * Производная любого числа равна нулю.
     *
     * @param var имя переменной
     * @return объект {@code Number(0)}
     */
    @Override
    public Expression derivative(String var) {
        return new Number(0);
    }

    /**
     * Упрощает выражение.
     * Для {@code Number} возвращает сам объект, так как число не может быть упрощено.
     *
     * @return текущий объект {@code this}
     */
    @Override
    public Expression simplify() {
        return this;
    }

    /**
     * Возвращает строковое представление числа.
     *
     * @return строка, содержащая значение числа
     */
    @Override
    public String toString() {
        return Integer.toString(value);
    }

    /**
     * Проверяет равенство двух чисел.
     * Числа равны, если их значения совпадают.
     *
     * @param o объект для сравнения
     * @return {@code true}, если значения совпадают, иначе {@code false}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Number)) {
            return false;
        }
        Number number = (Number) o;
        return value == number.value;
    }

    /**
     * Возвращает хэш-код числа.
     *
     * @return хэш-код значения
     */
    @Override
    public int hashCode() {
        return Integer.hashCode(value);
    }
}