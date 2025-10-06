package ru.nsu.gaev;

import java.util.Map;
import java.util.Objects;

/**
 * Класс, представляющий операцию сложения двух выражений.
 * Реализует базовые операции: вычисление значения, дифференцирование и упрощение.
 */
public class Add extends Expression {

    /** Левый операнд сложения. */
    private final Expression left;

    /** Правый операнд сложения. */
    private final Expression right;

    /**
     * Создаёт объект {@code Add} с заданными левым и правым операндами.
     *
     * @param left  левый операнд
     * @param right правый операнд
     */
    public Add(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    /**
     * Возвращает левый операнд выражения.
     *
     * @return левое подвыражение
     */
    public Expression getLeft() {
        return left;
    }

    /**
     * Возвращает правый операнд выражения.
     *
     * @return правое подвыражение
     */
    public Expression getRight() {
        return right;
    }

    /**
     * Вычисляет результат сложения на основе переданных значений переменных.
     *
     * @param vars отображение (map), где ключ — имя переменной,
     *             а значение — её числовое значение
     * @return целое число — результат сложения
     */
    @Override
    public int eval(Map<String, Integer> vars) {
        return left.eval(vars) + right.eval(vars);
    }

    /**
     * Вычисляет сумму от данного выражения по заданной переменной.
     *
     * @param var имя переменной
     * @return новое выражение {@code Add}, представляющее сумму
     */
    @Override
    public Expression derivative(String var) {
        return new Add(left.derivative(var), right.derivative(var));
    }

    /**
     * Упрощает выражение сложения.
     * Если оба операнда являются числами, возвращается одно
     * числовое выражение {@code Number}.
     *
     * @return упрощённое выражение {@code Expression}
     */
    @Override
    public Expression simplify() {
        Expression l = left.simplify();
        Expression r = right.simplify();
        if (l instanceof Number && r instanceof Number) {
            int v = ((Number) l).getValue() + ((Number) r).getValue();
            return new Number(v);
        }
        return new Add(l, r);
    }

    /**
     * Возвращает строковое представление выражения сложения.
     *
     * @return строка в виде "(левое+правое)"
     */
    @Override
    protected String toExpressionString() {
        return "(" + left.toExpressionString() + "+" + right.toExpressionString() + ")";
    }

    /**
     * Проверяет равенство данного выражения с другим объектом.
     *
     * @param o объект для сравнения
     * @return {@code true}, если оба объекта — экземпляры {@code Add}
     *         и имеют одинаковые операнды
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Add)) {
            return false;
        }
        Add add = (Add) o;
        return Objects.equals(left, add.left) && Objects.equals(right, add.right);
    }

    /**
     * Возвращает хэш-код данного выражения.
     *
     * @return значение хэш-кода
     */
    @Override
    public int hashCode() {
        return Objects.hash(left, right);
    }
}