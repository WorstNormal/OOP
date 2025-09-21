package ru.nsu.gaev;

import java.util.Map;
import java.util.Objects;

public class Sub extends Expression {
    private final Expression left;
    private final Expression right;

    public Sub(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public int eval(Map<String, Integer> vars) {
        return left.eval(vars) - right.eval(vars);
    }

    @Override
    public Expression derivative(String var) {
        return new Sub(left.derivative(var), right.derivative(var));
    }

    @Override
    public Expression simplify() {
        Expression l = left.simplify();
        Expression r = right.simplify();
        if (l instanceof ru.nsu.gaev.Number && r instanceof ru.nsu.gaev.Number) {
            return new ru.nsu.gaev.Number(((ru.nsu.gaev.Number) l).getValue() - ((ru.nsu.gaev.Number) r).getValue());
        }
        if (l.equals(r)) {
            return new ru.nsu.gaev.Number(0);
        }
        return new Sub(l, r);
    }

    @Override
    protected String toExpressionString() {
        return "(" + left.toExpressionString() + "-" + right.toExpressionString() + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Sub)) return false;
        Sub sub = (Sub) o;
        return Objects.equals(left, sub.left) && Objects.equals(right, sub.right);
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right);
    }
}
