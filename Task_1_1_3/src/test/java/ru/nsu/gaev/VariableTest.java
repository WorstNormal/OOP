package ru.nsu.gaev;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class VariableTest {

    @Test
    void testEvalWithAssignedVariable() {
        Variable x = new Variable("x");
        Map<String, Integer> vars = new HashMap<>();
        vars.put("x", 10);

        int result = x.eval(vars);
        assertEquals(10, result);
    }

    @Test
    void testDerivativeSameVariable() {
        Variable x = new Variable("x");
        Expression derivative = x.derivative("x");
        assertEquals(new Number(1), derivative);
    }

    @Test
    void testDerivativeDifferentVariable() {
        Variable x = new Variable("x");
        Expression derivative = x.derivative("y");
        assertEquals(new Number(0), derivative);
    }

    @Test
    void testSimplifyReturnsItself() {
        Variable x = new Variable("x");
        assertSame(x, x.simplify());
    }

    @Test
    void testToExpressionString() {
        Variable var = new Variable("abc");
        assertEquals("abc", var.toExpressionString());
    }

    @Test
    void testEqualsAndHashCode() {
        Variable x1 = new Variable("x");
        Variable x2 = new Variable("x");
        Variable y = new Variable("y");

        assertEquals(x1, x2);
        assertNotEquals(x1, y);
        assertEquals(x1.hashCode(), x2.hashCode());
    }

    @Test
    void testNotEqualsDifferentClass() {
        Variable x = new Variable("x");
        assertNotEquals(new Number(5), x);
    }
}