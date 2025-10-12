package ru.nsu.gaev;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import ru.nsu.gaev.expression.Expression;
import ru.nsu.gaev.expression.Number;
import ru.nsu.gaev.expression.Variable;

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
    void testEvalMissingVariableThrows() {
        Variable x = new Variable("x");
        // ожидание RuntimeException при отсутствии значения
        assertThrows(RuntimeException.class, () -> x.eval(Map.of()));
    }

    @Test
    void testGetName() {
        Variable v = new Variable("abc");
        assertEquals("abc", v.getName());
    }

    @Test
    void testDerivativeSameVariable() {
        Variable x = new Variable("x");
        Expression derivative = x.derivative("x");
        assertEquals(new ru.nsu.gaev.expression.Number(1), derivative);
    }

    @Test
    void testDerivativeDifferentVariable() {
        Variable x = new Variable("x");
        Expression derivative = x.derivative("y");
        assertEquals(new ru.nsu.gaev.expression.Number(0), derivative);
    }

    @Test
    void testSimplifyReturnsItself() {
        Variable x = new Variable("x");
        assertSame(x, x.simplify());
    }

    @Test
    void testToExpressionString() {
        Variable var = new Variable("abc");
        assertEquals("abc", var.toString());
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
    void testHashCodeStable() {
        Variable x = new Variable("x");
        int h = x.hashCode();
        assertEquals(h, x.hashCode());
    }

    @Test
    void testNotEqualsDifferentClass() {
        Variable x = new Variable("x");
        assertNotEquals(new Number(5), x);
    }
}