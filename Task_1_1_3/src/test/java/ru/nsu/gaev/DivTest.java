package ru.nsu.gaev;

import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DivTest {
    @Test
    void testEval() {
        Expression x = new Variable("x");
        Expression y = new Variable("y");
        Expression expr = new Div(new Add(x, new Number(2)), new Add(new Number(3), y));

        Map<String, Integer> vars = new HashMap<>();
        vars.put("x", 5);
        vars.put("y", 10);
        System.out.println("Vars: " + vars);

        int result = expr.eval(vars);
        System.out.println("Result: " + result);

        assertEquals(0, result);
    }

    @Test
    void testDerivative() {
        Expression x = new Variable("x");
        Expression y = new Variable("y");
        Expression expr = new Div(new Add(x, new ru.nsu.gaev.Number(2)), new Add(new ru.nsu.gaev.Number(3), y));
        Expression derivative = expr.derivative("x");
        Map<String, Integer> vars = new HashMap<>();
        vars.put("x", 5);
        vars.put("y", 10);

        // Печать переменных для отладки
        System.out.println("Vars: " + vars);

        int derivativeResult = derivative.eval(vars);
        System.out.println("Derivative result: " + derivativeResult);
        assertEquals(1 / 169, derivativeResult);
    }

    @Test
    void testSimplify() {
        Expression expr = new Div(new ru.nsu.gaev.Number(6), new ru.nsu.gaev.Number(3));
        Expression simplifiedExpr = expr.simplify();
        assertTrue(simplifiedExpr instanceof ru.nsu.gaev.Number);
        assertEquals(2, ((ru.nsu.gaev.Number) simplifiedExpr).getValue());
    }

    @Test
    void testToExpressionString() {
        Expression x = new Variable("x");
        Expression expr = new Div(x, new ru.nsu.gaev.Number(2));

        String expressionString = expr.toExpressionString();
        assertEquals("(x/2)", expressionString);
    }

    @Test
    void testEquals() {
        Expression x = new Variable("x");
        Expression expr1 = new Div(x, new ru.nsu.gaev.Number(2));
        Expression expr2 = new Div(x, new ru.nsu.gaev.Number(2));
        Expression expr3 = new Div(x, new ru.nsu.gaev.Number(3));

        assertEquals(expr1, expr2);
        assertNotEquals(expr1, expr3);
    }

    @Test
    void testHashCode() {
        Expression x = new Variable("x");
        Expression expr1 = new Div(x, new ru.nsu.gaev.Number(2));
        Expression expr2 = new Div(x, new ru.nsu.gaev.Number(2));
        Expression expr3 = new Div(x, new ru.nsu.gaev.Number(3));

        assertEquals(expr1.hashCode(), expr2.hashCode());
        assertNotEquals(expr1.hashCode(), expr3.hashCode());
    }

    @Test
    void testSimplifyDivByOne() {
        Expression expr = new Div(new ru.nsu.gaev.Number(5), new ru.nsu.gaev.Number(1));
        Expression simplifiedExpr = expr.simplify();
        assertTrue(simplifiedExpr instanceof ru.nsu.gaev.Number);
        assertEquals(5, ((ru.nsu.gaev.Number) simplifiedExpr).getValue());
    }
    @Test
    void testSimplifyDivZeroNumerator() {
        Expression expr = new Div(new ru.nsu.gaev.Number(0), new ru.nsu.gaev.Number(3));
        Expression simplifiedExpr = expr.simplify();
        assertTrue(simplifiedExpr instanceof ru.nsu.gaev.Number);
        assertEquals(0, ((ru.nsu.gaev.Number) simplifiedExpr).getValue());
    }
}
