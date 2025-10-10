package ru.nsu.gaev;

import java.util.Map;
import java.util.Objects;

/**
 * Класс {@code Sub} представляет операцию вычитания двух выражений.
 * Поддерживает вычисление значения, нахождение производной и упрощение выражения.
 */
public class Sub extends Expression {

    /** Левый операнд вычитания. */
    private final Expression left;

    /** Правый операнд вычитания. */
    private final Expression right;

    /**
     * Создаёт выражение вычитания с указанными левым и правым операндами.
     *
     * @param left левый операнд
     * @param right правый операнд
     */
    public Sub(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    /**
     * Вычисляет значение выражения вычитания с использованием переданных значений переменных.
     *
     * @param vars отображение имён переменных и их значений
     * @return результат вычитания
     */
    @Override
    public int eval(Map<String, Integer> vars) {
        return left.eval(vars) - right.eval(vars);
    }

    /**
     * Вычисляет производную выражения по заданной переменной.
     *
     * @param var имя переменной, по которой берётся производная
     * @return новое выражение {@code Sub}, представляющее производную
     */
    @Override
    public Expression derivative(String var) {
        return new Sub(left.derivative(var), right.derivative(var));
    }

    /**
     * Упрощает выражение вычитания.
     * <ul>
     *   <li>Если оба операнда — числа, возвращает результат вычитания как {@code Number}.</li>
     *   <li>Если оба операнда равны, возвращает {@code Number(0)}.</li>
     *   <li>В остальных случаях возвращает новое выражение {@code Sub(l, r)}.</li>
     * </ul>
     *
     * @return упрощённое выражение
     */
    @Override
    public Expression simplify() {
        Expression l = left.simplify();
        Expression r = right.simplify();
        if (l instanceof Number && r instanceof Number) {
            return new Number(((Number) l).getValue() - ((Number) r).getValue());
        }
        if (l.equals(r)) {
            return new Number(0);
        }
        return new Sub(l, r);
    }

    /**
     * Возвращает строковое представление выражения в виде {@code (left-right)}.
     *
     * @return строка, представляющая выражение
     */
    @Override
    public String toString() {
        return "(" + left.toString() + "-" + right.toString() + ")";
    }

    /**
     * Проверяет равенство двух выражений вычитания.
     *
     * @param o объект для сравнения
     * @return {@code true}, если оба выражения имеют одинаковые операнды
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Sub)) {
            return false;
        }
        Sub sub = (Sub) o;
        return Objects.equals(left, sub.left) && Objects.equals(right, sub.right);
    }

    /**
     * Вычисляет хеш-код выражения.
     *
     * @return хеш-код объекта
     */
    @Override
    public int hashCode() {
        return Objects.hash(left, right);
    }
}