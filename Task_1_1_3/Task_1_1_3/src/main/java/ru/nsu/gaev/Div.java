package ru.nsu.gaev;

import java.util.Map;
import java.util.Objects;

/**
 * Класс, представляющий операцию деления двух выражений.
 * Реализует вычисление, дифференцирование и упрощение выражения.
 */
public class Div extends Expression {

    /** Левый операнд деления. */
    private final Expression left;

    /** Правый операнд деления. */
    private final Expression right;

    /**
     * Создаёт объект {@code Div} с заданными левым и правым операндами.
     *
     * @param left  левое подвыражение
     * @param right правое подвыражение
     */
    public Div(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    /**
     * Вычисляет результат деления выражений на основе переданных значений переменных.
     * @param vars отображение (map), где ключ — имя переменной, а значение — её числовое значение
     * @return целое число — результат деления
     */
    @Override
    public int eval(Map<String, Integer> vars) {
        return left.eval(vars) / right.eval(vars);
    }

    /**
     * Вычисляет выражения деления по заданной переменной.
     * @param var имя переменной
     * @return новое выражение {@code Div}
     */
    @Override
    public Expression derivative(String var) {
        return new Div(
                new Sub(
                        new Mul(left.derivative(var), right),
                        new Mul(left, right.derivative(var))
                ),
                new Mul(right, right)
        );
    }

    /**
     * Упрощает выражение деления.
     * Применяются следующие правила:
     * Если числитель равен 0 — результат равен 0.
     * Если знаменатель равен 1 — возвращается числитель.
     * Если оба операнда — числа, возвращается результат деления как {@code Number}
     *
     * @return упрощённое выражение {@code Expression}
     */
    @Override
    public Expression simplify() {
        Expression l = left.simplify();
        Expression r = right.simplify();

        if (l instanceof Number && ((Number) l).getValue() == 0) {
            return new Number(0);
        }

        if (r instanceof Number && ((Number) r).getValue() == 1) {
            return l;
        }

        if (l instanceof Number && r instanceof Number) {
            return new Number(
                    ((Number) l).getValue() / ((Number) r).getValue()
            );
        }

        return new Div(l, r);
    }

    /**
     * Возвращает строковое представление выражения деления.
     *
     * @return строка в виде "(левое/правое)"
     */
    @Override
    protected String toExpressionString() {
        return "(" + left.toExpressionString() + "/" + right.toExpressionString() + ")";
    }

    /**
     * Проверяет равенство данного выражения с другим объектом.
     *
     * @param o объект для сравнения
     * @return {@code true}, если оба объекта — экземпляры {@code Div} и
     * имеют одинаковые операнды
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Div)) {
            return false;
        }
        Div div = (Div) o;
        return Objects.equals(left, div.left)
                && Objects.equals(right, div.right);
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
