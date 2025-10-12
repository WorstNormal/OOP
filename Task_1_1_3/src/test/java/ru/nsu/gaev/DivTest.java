package ru.nsu.gaev;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import ru.nsu.gaev.expression.*;
import ru.nsu.gaev.expression.Number;

class DivTest {
    @Test
    void testEval() {
        final Expression x = new Variable("x");
        final Expression y = new Variable("y");
        final Expression expr = new Div(new Add(x, new ru.nsu.gaev.expression.Number(2)),
                new Add(new ru.nsu.gaev.expression.Number(3), y));

        Map<String, Integer> vars = new HashMap<>();
        vars.put("x", 5);
        vars.put("y", 10);

        int result = expr.eval(vars);
        assertEquals(0, result);
    }

    @Test
    void testDerivative() {
        final Expression x = new Variable("x");
        final Expression y = new Variable("y");
        final Expression expr = new Div(new Add(x, new ru.nsu.gaev.expression.Number(2)),
                new Add(new ru.nsu.gaev.expression.Number(3), y));
        final Expression derivative = expr.derivative("x");
        Map<String, Integer> vars = new HashMap<>();
        vars.put("x", 5);
        vars.put("y", 10);

        int derivativeResult = derivative.eval(vars);
        assertEquals(1 / 169, derivativeResult);
    }

    @Test
    void testSimplify() {
        Expression expr = new Div(new ru.nsu.gaev.expression.Number(6), new ru.nsu.gaev.expression.Number(3));
        Expression simplifiedExpr = expr.simplify();
        assertInstanceOf(ru.nsu.gaev.expression.Number.class, simplifiedExpr);
        assertEquals(2, ((ru.nsu.gaev.expression.Number) simplifiedExpr).getValue());
    }

    @Test
    void testToExpressionString() {
        Expression x = new Variable("x");
        Expression expr = new Div(x, new ru.nsu.gaev.expression.Number(2));

        String expressionString = expr.toString();
        assertEquals("(x/2)", expressionString);
    }

    @Test
    void testEquals() {
        Expression x = new Variable("x");
        Expression expr1 = new Div(x, new ru.nsu.gaev.expression.Number(2));
        Expression expr2 = new Div(x, new ru.nsu.gaev.expression.Number(2));
        Expression expr3 = new Div(x, new ru.nsu.gaev.expression.Number(3));

        assertEquals(expr1, expr2);
        assertNotEquals(expr1, expr3);
    }

    @Test
    void testHashCode() {
        Expression x = new Variable("x");
        Expression expr1 = new Div(x, new ru.nsu.gaev.expression.Number(2));
        Expression expr2 = new Div(x, new ru.nsu.gaev.expression.Number(2));
        Expression expr3 = new Div(x, new ru.nsu.gaev.expression.Number(3));

        assertEquals(expr1.hashCode(), expr2.hashCode());
        assertNotEquals(expr1.hashCode(), expr3.hashCode());
    }

    @Test
    void testSimplifyDivByOne() {
        Expression expr = new Div(new ru.nsu.gaev.expression.Number(5), new ru.nsu.gaev.expression.Number(1));
        Expression simplifiedExpr = expr.simplify();
        assertInstanceOf(ru.nsu.gaev.expression.Number.class, simplifiedExpr);
        assertEquals(5, ((ru.nsu.gaev.expression.Number) simplifiedExpr).getValue());
    }

    @Test
    void testSimplifyDivZeroNumerator() {
        Expression expr = new Div(new ru.nsu.gaev.expression.Number(0), new ru.nsu.gaev.expression.Number(3));
        Expression simplifiedExpr = expr.simplify();
        assertInstanceOf(ru.nsu.gaev.expression.Number.class, simplifiedExpr);
        assertEquals(0, ((Number) simplifiedExpr).getValue());
    }
}