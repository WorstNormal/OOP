package ru.nsu.gaev;

import java.util.Map;

public class Variable extends Expression {
    private final String name;

    public Variable(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public int eval(Map<String, Integer> vars) {
        if (!vars.containsKey(name)) {
            throw new RuntimeException("Variable '" + name + "' is not assigned");
        }
        return vars.get(name);
    }

    @Override
    public Expression derivative(String var) {
        return new ru.nsu.gaev.Number(name.equals(var) ? 1 : 0);
    }

    @Override
    public Expression simplify() {
        return this;
    }

    @Override
    protected String toExpressionString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Variable)) return false;
        Variable variable = (Variable) o;
        return name.equals(variable.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
