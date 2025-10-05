package ru.nsu.gaev;

import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class ParserTest {

    // ======== ТЕСТЫ НА parse() ========

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
        Expression expected = new Mul(new Add(new Variable("x"), new Number(2)), new Number(3));
        assertEquals(expected, expr);

        int result = expr.eval(Map.of("x", 4)); // (4 + 2) * 3 = 18
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
}
