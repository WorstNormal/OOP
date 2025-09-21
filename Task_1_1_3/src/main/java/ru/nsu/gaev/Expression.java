package ru.nsu.gaev;

import java.util.Map;

public abstract class Expression {
    public int eval(String assignment) {
        Map<String, Integer> map = Parser.parseAssignment(assignment);
        return eval(map);
    }

    public abstract int eval(Map<String, Integer> vars);

    public abstract Expression derivative(String var);

    public abstract Expression simplify();

    public void print() {
        System.out.print(this.toExpressionString());
    }

    protected abstract String toExpressionString();
}

