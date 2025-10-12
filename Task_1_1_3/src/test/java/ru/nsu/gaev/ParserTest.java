package ru.nsu.gaev;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;
import org.junit.jupiter.api.Test;
import ru.nsu.gaev.expression.Add;
import ru.nsu.gaev.expression.Div;
import ru.nsu.gaev.expression.Expression;
import ru.nsu.gaev.expression.Mul;
import ru.nsu.gaev.expression.Number;
import ru.nsu.gaev.expression.Sub;
import ru.nsu.gaev.expression.Variable;
import ru.nsu.gaev.parser.Parser;

class ParserTest {
    @Test
    void testParseSimpleNumber() {
        Expression expr = Parser.parse("42");
        assertEquals(new ru.nsu.gaev.expression.Number(42), expr);
        assertEquals(42, expr.eval(Map.of()));
    }

    @Test
    void testParseNegativeNumber() {
        Expression expr = Parser.parse("-7");
        assertEquals(new ru.nsu.gaev.expression.Number(-7), expr);
    }

    @Test
    void testParseVariable() {
        Expression expr = Parser.parse("x");
        assertEquals(new Variable("x"), expr);
    }

    @Test
    void testParseSimpleAddition() {
        Expression expr = Parser.parse("(2+3)");
        assertEquals(new Add(new ru.nsu.gaev.expression.Number(2),
                new ru.nsu.gaev.expression.Number(3)), expr);
        assertEquals(5, expr.eval(Map.of()));
    }

    @Test
    void testParseNestedExpression() {
        Expression expr = Parser.parse("((x+2)*3)");
        Expression expected = new Mul(new Add(new Variable("x"),
                new ru.nsu.gaev.expression.Number(2)),
                new ru.nsu.gaev.expression.Number(3));
        assertEquals(expected, expr);

        int result = expr.eval(Map.of("x", 4));
        assertEquals(18, result);
    }

    @Test
    void testParseExpressionWithDivisionAndSubtraction() {
        Expression expr = Parser.parse("((10-4)/2)");
        Expression expected = new Div(new Sub(new ru.nsu.gaev.expression.Number(10),
                new ru.nsu.gaev.expression.Number(4)),
                new ru.nsu.gaev.expression.Number(2));
        assertEquals(expected, expr);
        assertEquals(3, expr.eval(Map.of()));
    }

    @Test
    void testParseThrowsOnInvalidOperator() {
        assertThrows(IllegalArgumentException.class, () -> Parser.parse("(2#3)"));
    }

    @Test
    void testParseThrowsOnMissingParenthesis() {
        assertThrows(IllegalArgumentException.class, () -> Parser.parse("(2+3"));
    }

    @Test
    void testParseThrowsOnUnexpectedChar() {
        assertThrows(IllegalArgumentException.class, () -> Parser.parse("$"));
    }

    @Test
    void testParseThrowsOnNullInput() {
        assertThrows(IllegalArgumentException.class, () -> Parser.parse(null));
    }

    @Test
    void testParseAssignmentSingle() {
        Map<String, Integer> vars = Parser.parseAssignment("x=5");
        assertEquals(1, vars.size());
        assertEquals(5, vars.get("x"));
    }

    @Test
    void testParseAssignmentMultiple() {
        Map<String, Integer> vars = Parser.parseAssignment("x=3; y=7; z=10");
        assertEquals(Map.of("x", 3, "y", 7, "z", 10), vars);
    }

    @Test
    void testParseAssignmentEmptyString() {
        Map<String, Integer> vars = Parser.parseAssignment("");
        assertTrue(vars.isEmpty());
    }

    @Test
    void testParseAssignmentNullString() {
        Map<String, Integer> vars = Parser.parseAssignment(null);
        assertTrue(vars.isEmpty());
    }

    @Test
    void testParseAssignmentWithSpacesAndExtraSemicolons() {
        Map<String, Integer> vars = Parser.parseAssignment("  x = 2  ;  y = 3 ;  ");
        assertEquals(Map.of("x", 2, "y", 3), vars);
    }

    @Test
    void testParseAssignmentThrowsOnBadFormat() {
        assertThrows(IllegalArgumentException.class,
                () -> Parser.parseAssignment("x=3 y=5"));
        assertThrows(IllegalArgumentException.class,
                () -> Parser.parseAssignment("x:3"));
        assertThrows(IllegalArgumentException.class,
                () -> Parser.parseAssignment("a=b=c"));
    }

    @Test
    void testParseParenthesesVariants() {
        assertEquals(new Variable("x"), Parser.parse("(((x)))"));
        assertEquals(new Add(new Variable("x"), new Variable("y")), Parser.parse("((x)+(y))"));
        assertEquals(
                new Add(new Variable("a"), new Mul(new Variable("b"), new Variable("c"))),
                Parser.parse("((a)+((b)*(c)))")
        );
    }

    @Test
    void testParseMultiLetterVariables() {
        assertEquals(new Add(new Variable("alpha"),
                new Variable("beta")), Parser.parse("(alpha+beta)"));
        assertEquals(new Mul(new Variable("veryLongVariableName"), new Variable("short")),
                Parser.parse("(veryLongVariableName*short)"));
    }

    @Test
    void testParseSpacesVariants() {
        assertEquals(new Add(new Variable("x"), new Variable("y")), Parser.parse("( x + y )"));
        assertEquals(new Add(new Variable("x"), new Variable("y")), Parser.parse("(x+ y)"));
        assertEquals(new Add(new Variable("x"), new Variable("y")), Parser.parse("(x +y)"));
    }

    @Test
    void testParseNegativeNumbers() {
        assertEquals(new ru.nsu.gaev.expression.Number(-5), Parser.parse("(-5)"));
        assertEquals(new Add(new Variable("x"), new ru.nsu.gaev.expression.Number(-3)),
                Parser.parse("(x+(-3))"));
    }

    @Test
    void testParseComplexExpression() {
        Expression expected = new Add(
                new Mul(new Variable("a"), new Variable("b")),
                new Div(
                        new Sub(new Variable("c"), new Variable("d")),
                        new Add(new ru.nsu.gaev.expression.Number(1),
                                new ru.nsu.gaev.expression.Number(2))
                )
        );
        assertEquals(expected, Parser.parse("((a*b)+((c-d)/(1+2)))"));
    }

    @Test
    void testEvalWithAssignmentStringVarious() {
        Expression e = new Add(
                new Mul(new Variable("x"), new Variable("y")),
                new Div(new Variable("z"), new Number(2))
        );
        assertEquals(17, e.eval("x=3; y=4; z=10"));
        assertEquals(4, e.eval("x=0; y=100; z=8"));
        assertEquals(17, e.eval("z=10; x=3; y=4"));
        assertEquals(17, e.eval("x=3; y=4; z=10; w=100"));
        assertThrows(RuntimeException.class, () -> e.eval("x=3; y=4"));
    }

    @Test
    void testParseAssignmentThrowsOnEmptyValue() {
        assertThrows(IllegalArgumentException.class, () -> Parser.parseAssignment("x=; y=5"));
    }
}