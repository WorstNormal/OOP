package ru.nsu.gaev;

import java.util.Map;
import java.util.Objects;

/**
 * Класс, представляющий операцию умножения двух выражений.
 * Реализует вычисление значения, производной и упрощение выражения.
 */
public class Mul extends Expression {

    /** Левый операнд умножения. */
    private final Expression left;

    /** Правый операнд умножения. */
    private final Expression right;

    /**
     * Создаёт объект {@code Mul} с заданными левым и правым операндами.
     *
     * @param left  левое выражение
     * @param right правое выражение
     */
    public Mul(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    /**
     * Вычисляет результат умножения двух выражений при заданных значениях переменных.
     *
     * @param vars отображение, где ключ — имя переменной, значение — её числовое значение
     * @return результат вычисления выражения
     */
    @Override
    public int eval(Map<String, Integer> vars) {
        return left.eval(vars) * right.eval(vars);
    }

    /**
     * Вычисляет производную от произведения по правилу Лейбница.
     * <pre>(f * g)' = f' * g + f * g'</pre>
     *
     * @param var имя переменной, по которой берётся производная
     * @return выражение, представляющее производную
     */
    @Override
    public Expression derivative(String var) {
        return new Add(
                new Mul(left.derivative(var), right),
                new Mul(left, right.derivative(var))
        );
    }

    /**
     * Упрощает выражение умножения, если это возможно:
     * <ul>
     *   <li>Если один из операндов равен 0 — возвращает {@code Number(0)}</li>
     *   <li>Если один из операндов равен 1 — возвращает другой операнд</li>
     *   <li>Если оба операнда — числа — возвращает их произведение</li>
     * </ul>
     *
     * @return упрощённое выражение
     */
    @Override
    public Expression simplify() {
        Expression l = left.simplify();
        Expression r = right.simplify();

        if (l instanceof Number && ((Number) l).getValue() == 0) {
            return new Number(0);
        }
        if (r instanceof Number && ((Number) r).getValue() == 0) {
            return new Number(0);
        }
        if (l instanceof Number && ((Number) l).getValue() == 1) {
            return r;
        }
        if (r instanceof Number && ((Number) r).getValue() == 1) {
            return l;
        }
        if (l instanceof Number && r instanceof Number) {
            return new Number(((Number) l).getValue() * ((Number) r).getValue());
        }
        return new Mul(l, r);
    }

    /**
     * Возвращает строковое представление выражения в виде {@code (left*right)}.
     *
     * @return строка с представлением выражения
     */
    @Override
    protected String toExpressionString() {
        return "(" + left.toExpressionString() + "*" + right.toExpressionString() + ")";
    }

    /**
     * Проверяет равенство двух выражений {@code Mul}.
     * Выражения равны, если равны их левый и правый операнды.
     *
     * @param o объект для сравнения
     * @return {@code true}, если выражения равны, иначе {@code false}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Mul)) {
            return false;
        }
        Mul mul = (Mul) o;
        return Objects.equals(left, mul.left) && Objects.equals(right, mul.right);
    }

    /**
     * Возвращает хэш-код для выражения {@code Mul}.
     *
     * @return хэш-код выражения
     */
    @Override
    public int hashCode() {
        return Objects.hash(left, right);
    }
}
