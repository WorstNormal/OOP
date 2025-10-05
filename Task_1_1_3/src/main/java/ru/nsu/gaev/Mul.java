package ru.nsu.gaev;

import java.util.Map;
import java.util.Objects;

public class Mul extends Expression {
    private final Expression left;
    private final Expression right;

    public Mul(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public int eval(Map<String, Integer> vars) {
        return left.eval(vars) * right.eval(vars);
    }

    @Override
    public Expression derivative(String var) {
        return new Add(new Mul(left.derivative(var), right), new Mul(left, right.derivative(var)));
    }

    @Override
    public Expression simplify() {
        Expression l = left.simplify();
        Expression r = right.simplify();
        if (l instanceof ru.nsu.gaev.Number && ((ru.nsu.gaev.Number) l).getValue() == 0) {
            return new Number(0);
        }
        if (r instanceof ru.nsu.gaev.Number && ((ru.nsu.gaev.Number) r).getValue() == 0) {
            return new Number(0);
        }
        if (l instanceof ru.nsu.gaev.Number && ((ru.nsu.gaev.Number) l).getValue() == 1) {
            return r;
        }
        if (r instanceof ru.nsu.gaev.Number && ((ru.nsu.gaev.Number) r).getValue() == 1) {
            return l;
        }
        if (l instanceof ru.nsu.gaev.Number && r instanceof ru.nsu.gaev.Number) {
            return new ru.nsu.gaev.Number(((ru.nsu.gaev.Number) l).getValue() * ((ru.nsu.gaev.Number) r).getValue());
        }
        return new Mul(l, r);
    }

    @Override
    protected String toExpressionString() {
        return "(" + left.toExpressionString() + "*" + right.toExpressionString() + ")";
    }

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

    @Override
    public int hashCode() {
        return Objects.hash(left, right);
    }
}
