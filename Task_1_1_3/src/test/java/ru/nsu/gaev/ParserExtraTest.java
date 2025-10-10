package ru.nsu.gaev;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ParserExtraTest {

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
        Expression expected = new Add(new Variable("a"), new Mul(new Variable("b"), new Variable("c")));
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

