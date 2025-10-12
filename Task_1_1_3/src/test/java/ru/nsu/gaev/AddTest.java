package ru.nsu.gaev;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import ru.nsu.gaev.expression.Add;
import ru.nsu.gaev.expression.Expression;
import ru.nsu.gaev.expression.Number;
import ru.nsu.gaev.expression.Variable;

class AddTest {
    @Test
    void testEval() {
        Expression x = new Variable("x");
        Expression y = new Variable("y");
        Expression expr = new Add(new Add(x, new ru.nsu.gaev.expression.Number(2)),
                new Add(new ru.nsu.gaev.expression.Number(3), y));

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
        Expression expr = new Add(new Add(x, new ru.nsu.gaev.expression.Number(2)),
                new Add(new ru.nsu.gaev.expression.Number(3), y));
        Expression derivative = expr.derivative("x");
        Map<String, Integer> vars = new HashMap<>();
        int derivativeResult = derivative.eval(vars);
        assertEquals(1, derivativeResult);
    }

    @Test
    void testSimplify() {
        Expression expr = new Add(new ru.nsu.gaev.expression.Number(2),
                new ru.nsu.gaev.expression.Number(3));
        Expression simplifiedExpr = expr.simplify();
        assertTrue(simplifiedExpr instanceof ru.nsu.gaev.expression.Number);
        assertEquals(5, ((ru.nsu.gaev.expression.Number) simplifiedExpr).getValue());
    }

    @Test
    void testToExpressionString() {
        Expression x = new Variable("x");
        Expression expr = new Add(x, new ru.nsu.gaev.expression.Number(2));

        String expressionString = expr.toString();
        assertEquals("(x+2)", expressionString);
    }

    @Test
    void testEquals() {
        Expression x = new Variable("x");
        Expression expr1 = new Add(x, new ru.nsu.gaev.expression.Number(2));
        Expression expr2 = new Add(x, new ru.nsu.gaev.expression.Number(2));
        Expression expr3 = new Add(x, new ru.nsu.gaev.expression.Number(3));

        assertEquals(expr1, expr2);
        assertNotEquals(expr1, expr3);
    }

    @Test
    void testHashCode() {
        Expression x = new Variable("x");
        Expression expr1 = new Add(x, new ru.nsu.gaev.expression.Number(2));
        Expression expr2 = new Add(x, new ru.nsu.gaev.expression.Number(2));
        Expression expr3 = new Add(x, new Number(3));

        assertEquals(expr1.hashCode(), expr2.hashCode());
        assertNotEquals(expr1.hashCode(), expr3.hashCode());
    }
}