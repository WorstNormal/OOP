package ru.nsu.gaev;

import java.util.Map;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


class ParserTest {

    @Test
    void testParseSimpleNumber() {
        Expression expr = Parser.parse("42");
        assertEquals(new Number(42), expr);
        assertEquals(42, expr.eval(Map.of()));
    }

    @Test
    void testParseNegativeNumber() {
        Expression expr = Parser.parse("-7");
        assertEquals(new Number(-7), expr);
    }

    @Test
    void testParseVariable() {
        Expression expr = Parser.parse("x");
        assertEquals(new Variable("x"), expr);
    }

    @Test
    void testParseSimpleAddition() {
        Expression expr = Parser.parse("(2+3)");
        assertEquals(new Add(new Number(2), new Number(3)), expr);
        assertEquals(5, expr.eval(Map.of()));
    }

    @Test
    void testParseNestedExpression() {
        Expression expr = Parser.parse("((x+2)*3)");
        Expression expected = new Mul(
                new Add(new Variable("x"), new Number(2)),
                new Number(3)
        );
        assertEquals(expected, expr);

        int result = expr.eval(Map.of("x", 4));
        assertEquals(18, result);
    }

    @Test
    void testParseExpressionWithDivisionAndSubtraction() {
        Expression expr = Parser.parse("((10-4)/2)");
        Expression expected = new Div(new Sub(new Number(10), new Number(4)), new Number(2));
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
        assertThrows(IllegalArgumentException.class, () -> Parser.parseAssignment("x=3 y=5"));
        assertThrows(IllegalArgumentException.class, () -> Parser.parseAssignment("x:3"));
        assertThrows(IllegalArgumentException.class, () -> Parser.parseAssignment("a=b=c"));
    }

    @Test
    void testTripleParenthesesProducesVariable() {
        Expression expr = Parser.parse("(((x)))");
        assertEquals(new Variable("x"), expr);
    }

    @Test
    void testSimpleAdditionWithParentheses() {
        Expression expr = Parser.parse("((x)+(y))");
        assertEquals(new Add(new Variable("x"), new Variable("y")), expr);
    }

    @Test
    void testNestedAddMulParsing() {
        Expression expr = Parser.parse("((a)+((b)*(c)))");
        Expression expected = new Add(
                new Variable("a"),
                new Mul(new Variable("b"), new Variable("c"))
        );
        assertEquals(expected, expr);
    }

    @Test
    void testMultiLetterVariableNames() {
        Expression expr = Parser.parse("(alpha+beta)");
        Expression expected = new Add(new Variable("alpha"), new Variable("beta"));
        assertEquals(expected, expr);

        Expression expr2 = Parser.parse("(veryLongVariableName * short)");
        Expression expected2 = new Mul(new Variable("veryLongVariableName"), new Variable("short"));
        assertEquals(expected2, expr2);
    }

    @Test
    void testWhitespaceVariations() {
        assertEquals(new Add(new Variable("x"), new Variable("y")), Parser.parse("( x + y )"));
        assertEquals(new Add(new Variable("x"), new Variable("y")), Parser.parse("(x+ y)"));
        assertEquals(new Add(new Variable("x"), new Variable("y")), Parser.parse("(x +y)"));
    }

    @Test
    void testNegativeNumberInParensAndAddWithNegative() {
        Expression n = Parser.parse("(-5)");
        assertEquals(new Number(-5), n);

        Expression expr = Parser.parse("(x + (-3))");
        Expression expected = new Add(new Variable("x"), new Number(-3));
        assertEquals(expected, expr);
    }

    @Test
    void testComplexExpressionParsing() {
        String s = "((a*b)+((c-d)/(1+2)))";
        Expression parsed = Parser.parse(s);
        Expression expected = new Add(
                new Mul(new Variable("a"), new Variable("b")),
                new Div(
                        new Sub(new Variable("c"), new Variable("d")),
                        new Add(new Number(1), new Number(2))
                )
        );
        assertEquals(expected, parsed);
    }

    @Test
    void testInvalidSyntaxCases() {
        assertThrows(IllegalArgumentException.class, () -> Parser.parse("()"));
        assertThrows(IllegalArgumentException.class, () -> Parser.parse("(+)"));
        assertThrows(IllegalArgumentException.class, () -> Parser.parse("(x++)"));
    }
}
