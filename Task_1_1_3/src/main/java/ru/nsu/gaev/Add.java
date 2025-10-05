package ru.nsu.gaev;

import java.util.Map;
import java.util.Objects;

public class Add extends Expression {
    private final Expression left;
    private final Expression right;

    public Add(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    public Expression getLeft() { return left; }
    public Expression getRight() { return right; }

    @Override
    public int eval(Map<String, Integer> vars) {
        return left.eval(vars) + right.eval(vars);
    }

    @Override
    public Expression derivative(String var) {
        return new Add(left.derivative(var), right.derivative(var));
    }

    @Override
    public Expression simplify() {
        Expression l = left.simplify();
        Expression r = right.simplify();
        if (l instanceof ru.nsu.gaev.Number && r instanceof ru.nsu.gaev.Number) {
            int v = ((ru.nsu.gaev.Number) l).getValue() + ((ru.nsu.gaev.Number) r).getValue();
            return new ru.nsu.gaev.Number(v);
        }
        return new Add(l, r);
    }

    @Override
    protected String toExpressionString() {
        return "(" + left.toExpressionString() + "+" + right.toExpressionString() + ")";
    }

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

    @Override
    public int hashCode() {
        return Objects.hash(left, right);
    }
}
