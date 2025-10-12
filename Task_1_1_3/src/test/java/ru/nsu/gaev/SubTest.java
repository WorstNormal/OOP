package ru.nsu.gaev;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import ru.nsu.gaev.expression.Add;
import ru.nsu.gaev.expression.Expression;
import ru.nsu.gaev.expression.Number;
import ru.nsu.gaev.expression.Sub;
import ru.nsu.gaev.expression.Variable;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class SubTest {

    @Test
    void testEvalWithConstants() {
        Expression expr = new Sub(new ru.nsu.gaev.expression.Number(10),
                new ru.nsu.gaev.expression.Number(4));
        assertEquals(6, expr.eval(Map.of()));
    }

    @Test
    void testEvalWithVariables() {
        Expression expr = new Sub(new Variable("x"), new Variable("y"));
        Map<String, Integer> vars = new HashMap<>();
        vars.put("x", 8);
        vars.put("y", 3);
        assertEquals(5, expr.eval(vars));
    }

    @Test
    void testDerivative() {
        Expression expr = new Sub(new Variable("x"), new ru.nsu.gaev.expression.Number(3));
        Expression derivative = expr.derivative("x");
        Expression expected = new Sub(new ru.nsu.gaev.expression.Number(1),
                new ru.nsu.gaev.expression.Number(0));
        assertEquals(expected, derivative);
    }

    @Test
    void testSimplifyConstantSubtraction() {
        Expression expr = new Sub(new ru.nsu.gaev.expression.Number(9),
                new ru.nsu.gaev.expression.Number(4));
        Expression simplified = expr.simplify();
        assertEquals(new ru.nsu.gaev.expression.Number(5), simplified);
    }

    @Test
    void testSimplifyEqualExpressions() {
        Expression expr = new Sub(new Variable("x"), new Variable("x"));
        Expression simplified = expr.simplify();
        assertEquals(new ru.nsu.gaev.expression.Number(0), simplified);
    }

    @Test
    void testSimplifyNonTrivial() {
        Expression expr = new Sub(new Add(new ru.nsu.gaev.expression.Number(2),
                new ru.nsu.gaev.expression.Number(3)),
                new ru.nsu.gaev.expression.Number(1));
        Expression simplified = expr.simplify();
        assertEquals(new ru.nsu.gaev.expression.Number(4), simplified);
    }

    @Test
    void testToExpressionString() {
        Expression expr = new Sub(new ru.nsu.gaev.expression.Number(7), new Variable("y"));
        assertEquals("(7-y)", expr.toString());
    }

    @Test
    void testEqualsAndHashCode() {
        Sub a = new Sub(new ru.nsu.gaev.expression.Number(2), new Variable("x"));
        Sub b = new Sub(new ru.nsu.gaev.expression.Number(2), new Variable("x"));
        Sub c = new Sub(new ru.nsu.gaev.expression.Number(3), new Variable("x"));

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, c);
    }

    @Test
    void testEqualsWithDifferentType() {
        Sub sub = new Sub(new ru.nsu.gaev.expression.Number(1),
                new ru.nsu.gaev.expression.Number(2));
        assertNotEquals("not an expression", sub);
    }

    @Test
    void testEqualsWithNull() {
        Sub sub = new Sub(new ru.nsu.gaev.expression.Number(1),
                new ru.nsu.gaev.expression.Number(2));
        assertNotEquals(null, sub);
    }

    @Test
    void testDerivativeWithOtherVariable() {
        Expression expr = new Sub(new Variable("x"), new Variable("y"));
        Expression derivative = expr.derivative("y");
        Expression expected = new Sub(new ru.nsu.gaev.expression.Number(0), new Number(1));
        assertEquals(expected, derivative);
    }
}