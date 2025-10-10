package ru.nsu.gaev;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class ExpressionTest {

    @Test
    void testEvalStringAssignment() {
        Expression expr = new Add(new Variable("x"), new Number(3));

        int result = expr.eval("x=7");
        assertEquals(10, result);
    }

    @Test
    void testEvalStringAssignmentMultipleVars() {
        Expression expr = new Mul(new Add(new Variable("x"), new Variable("y")), new Number(2));

        int result = expr.eval("x=3;y=5");
        assertEquals(16, result);
    }

    @Test
    void testEvalStringAssignmentWithSpaces() {
        Expression expr = new Sub(new Variable("a"), new Number(4));

        int result = expr.eval("a = 10 ");
        assertEquals(6, result);
    }

    @Test
    void testDerivative() {
        Expression expr = new Add(new Variable("x"), new Number(5));
        Expression derivative = expr.derivative("x");
        assertEquals(new Number(1), derivative.simplify());
    }

    @Test
    void testSimplifyConstantExpression() {
        Expression expr = new Add(new Number(2), new Number(3));
        Expression simplified = expr.simplify();
        assertInstanceOf(Number.class, simplified);
        assertEquals(5, ((Number) simplified).getValue());
    }

    @Test
    void testPrint() {
        Expression expr = new Add(new Variable("x"), new Number(1));
        assertDoesNotThrow(expr::print);
    }

    @Test
    void testToExpressionString() {
        Expression expr = new Div(new Add(new Variable("x"), new Number(2)), new Number(5));
        String expected = "((x+2)/5)";
        assertEquals(expected, expr.toExpressionString());
    }
}
