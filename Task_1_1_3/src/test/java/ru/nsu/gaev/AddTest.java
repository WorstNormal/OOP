package ru.nsu.gaev;

import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AddTest {
    @Test
    void testEval() {
        Expression x = new Variable("x");
        Expression y = new Variable("y");
        Expression expr = new Add(new Add(x, new ru.nsu.gaev.Number(2)), new Add(new ru.nsu.gaev.Number(3), y));

        Map<String, Integer> vars = new HashMap<>();
        vars.put("x", 5);
        vars.put("y", 10);

        int result = expr.eval(vars);
        assertEquals(20, result);
    }

    @Test
    void testDerivative() {
        Expression x = new Variable("x");
        Expression y = new Variable("y");
        Expression expr = new Add(new Add(x, new ru.nsu.gaev.Number(2)), new Add(new ru.nsu.gaev.Number(3), y));
        Expression derivative = expr.derivative("x");
        Map<String, Integer> vars = new HashMap<>();
        int derivativeResult = derivative.eval(vars);
        assertEquals(1, derivativeResult);
    }

    @Test
    void testSimplify() {
        Expression expr = new Add(new ru.nsu.gaev.Number(2), new ru.nsu.gaev.Number(3));
        Expression simplifiedExpr = expr.simplify();
        assertTrue(simplifiedExpr instanceof ru.nsu.gaev.Number);
        assertEquals(5, ((ru.nsu.gaev.Number) simplifiedExpr).getValue());
    }

    @Test
    void testToExpressionString() {
        Expression x = new Variable("x");
        Expression expr = new Add(x, new ru.nsu.gaev.Number(2));

        String expressionString = expr.toExpressionString();
        assertEquals("(x+2)", expressionString);
    }

    @Test
    void testEquals() {
        Expression x = new Variable("x");
        Expression expr1 = new Add(x, new ru.nsu.gaev.Number(2));
        Expression expr2 = new Add(x, new ru.nsu.gaev.Number(2));
        Expression expr3 = new Add(x, new ru.nsu.gaev.Number(3));

        assertEquals(expr1, expr2);
        assertNotEquals(expr1, expr3);
    }

    @Test
    void testHashCode() {
        Expression x = new Variable("x");
        Expression expr1 = new Add(x, new ru.nsu.gaev.Number(2));
        Expression expr2 = new Add(x, new ru.nsu.gaev.Number(2));
        Expression expr3 = new Add(x, new ru.nsu.gaev.Number(3));

        assertEquals(expr1.hashCode(), expr2.hashCode());
        assertNotEquals(expr1.hashCode(), expr3.hashCode());
    }
}
