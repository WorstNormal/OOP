package ru.nsu.gaev;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;


class MulTest {

    @Test
    void testEvalSimple() {
        Expression expr = new Mul(new Number(3), new Number(4));
        assertEquals(12, expr.eval(new HashMap<>()));
    }

    @Test
    void testEvalWithVariables() {
        Expression expr = new Mul(new Variable("x"),
                new Add(new Variable("y"), new Number(2)));
        Map<String, Integer> vars = new HashMap<>();
        vars.put("x", 3);
        vars.put("y", 4);
        assertEquals(18, expr.eval(vars));
    }

    @Test
    void testDerivativeSimple() {
        Expression x = new Variable("x");
        Expression expr = new Mul(x, x);
        Expression derivative = expr.derivative("x").simplify();
        assertEquals(new Add(x, x).simplify(), derivative);
    }

    @Test
    void testDerivativeMixed() {
        Expression expr = new Mul(new Variable("x"), new Number(5));
        Expression derivative = expr.derivative("x").simplify();
        assertEquals(new Number(5), derivative);
    }

    @Test
    void testSimplifyZeroLeft() {
        Expression expr = new Mul(new Number(0), new Variable("x"));
        assertEquals(new Number(0), expr.simplify());
    }

    @Test
    void testSimplifyZeroRight() {
        Expression expr = new Mul(new Variable("x"), new Number(0));
        assertEquals(new Number(0), expr.simplify());
    }

    @Test
    void testSimplifyOneLeft() {
        Expression expr = new Mul(new Number(1), new Variable("x"));
        assertEquals(new Variable("x"), expr.simplify());
    }

    @Test
    void testSimplifyOneRight() {
        Expression expr = new Mul(new Variable("x"), new Number(1));
        assertEquals(new Variable("x"), expr.simplify());
    }

    @Test
    void testSimplifyBothNumbers() {
        Expression expr = new Mul(new Number(3), new Number(5));
        assertEquals(new Number(15), expr.simplify());
    }

    @Test
    void testToExpressionString() {
        Expression expr = new Mul(new Add(new Number(2), new Variable("x")),
                new Number(3));
        assertEquals("((2+x)*3)", expr.toExpressionString());
    }

    @Test
    void testEqualsAndHashCode() {
        Expression e1 = new Mul(new Variable("x"), new Number(2));
        Expression e2 = new Mul(new Variable("x"), new Number(2));
        Expression e3 = new Mul(new Variable("y"), new Number(2));

        assertEquals(e1, e2);
        assertEquals(e1.hashCode(), e2.hashCode());
        assertNotEquals(e1, e3);
    }
}