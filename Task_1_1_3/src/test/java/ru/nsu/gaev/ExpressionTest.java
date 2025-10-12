package ru.nsu.gaev;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import org.junit.jupiter.api.Test;
import ru.nsu.gaev.expression.Add;
import ru.nsu.gaev.expression.Div;
import ru.nsu.gaev.expression.Expression;
import ru.nsu.gaev.expression.Mul;
import ru.nsu.gaev.expression.Number;
import ru.nsu.gaev.expression.Sub;
import ru.nsu.gaev.expression.Variable;


class ExpressionTest {

    @Test
    void testEvalStringAssignment() {
        Expression expr = new Add(new Variable("x"), new ru.nsu.gaev.expression.Number(3));

        int result = expr.eval("x=7");
        assertEquals(10, result);
    }

    @Test
    void testEvalStringAssignmentMultipleVars() {
        Expression expr = new Mul(new Add(new Variable("x"), new Variable("y")),
                new ru.nsu.gaev.expression.Number(2));

        int result = expr.eval("x=3;y=5");
        assertEquals(16, result);
    }

    @Test
    void testEvalStringAssignmentWithSpaces() {
        Expression expr = new Sub(new Variable("a"),
                new ru.nsu.gaev.expression.Number(4));

        int result = expr.eval("a = 10 ");
        assertEquals(6, result);
    }

    @Test
    void testDerivative() {
        Expression expr = new Add(new Variable("x"),
                new ru.nsu.gaev.expression.Number(5));
        Expression derivative = expr.derivative("x");
        assertEquals(new ru.nsu.gaev.expression.Number(1), derivative.simplify());
    }

    @Test
    void testSimplifyConstantExpression() {
        Expression expr = new Add(new ru.nsu.gaev.expression.Number(2),
                new ru.nsu.gaev.expression.Number(3));
        Expression simplified = expr.simplify();
        assertInstanceOf(ru.nsu.gaev.expression.Number.class, simplified);
        assertEquals(5, ((ru.nsu.gaev.expression.Number) simplified).getValue());
    }

    @Test
    void testPrint() {
        Expression expr = new Add(new Variable("x"),
                new ru.nsu.gaev.expression.Number(1));
        assertDoesNotThrow(() -> System.out.println(expr));
    }

    @Test
    void testToExpressionString() {
        Expression expr = new Div(new Add(new Variable("x"),
                new ru.nsu.gaev.expression.Number(2)),
                new Number(5));
        String expected = "((x+2)/5)";
        assertEquals(expected, expr.toString());
    }
}
