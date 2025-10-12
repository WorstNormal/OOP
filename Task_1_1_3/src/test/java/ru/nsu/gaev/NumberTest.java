package ru.nsu.gaev;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.HashMap;
import org.junit.jupiter.api.Test;
import ru.nsu.gaev.expression.Expression;
import ru.nsu.gaev.expression.Number;

class NumberTest {

    @Test
    void testGetValue() {
        ru.nsu.gaev.expression.Number num = new ru.nsu.gaev.expression.Number(42);
        assertEquals(42, num.getValue());
    }

    @Test
    void testEvalReturnsValueIgnoringVars() {
        ru.nsu.gaev.expression.Number num = new ru.nsu.gaev.expression.Number(10);
        assertEquals(10, num.eval(new HashMap<>()));
    }

    @Test
    void testDerivativeAlwaysZero() {
        ru.nsu.gaev.expression.Number num = new ru.nsu.gaev.expression.Number(5);
        Expression derivative = num.derivative("x");
        assertEquals(new ru.nsu.gaev.expression.Number(0), derivative);
    }

    @Test
    void testSimplifyReturnsSameInstance() {
        ru.nsu.gaev.expression.Number num = new ru.nsu.gaev.expression.Number(7);
        Expression simplified = num.simplify();
        assertSame(num, simplified);
    }

    @Test
    void testToExpressionString() {
        ru.nsu.gaev.expression.Number num = new ru.nsu.gaev.expression.Number(-8);
        assertEquals("-8", num.toString());
    }

    @Test
    void testEqualsAndHashCode() {
        ru.nsu.gaev.expression.Number a = new ru.nsu.gaev.expression.Number(3);
        ru.nsu.gaev.expression.Number b = new ru.nsu.gaev.expression.Number(3);
        ru.nsu.gaev.expression.Number c = new ru.nsu.gaev.expression.Number(4);

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, c);
    }

    @Test
    void testEqualsSelf() {
        ru.nsu.gaev.expression.Number num = new ru.nsu.gaev.expression.Number(9);
        assertEquals(num, num);
    }

    @Test
    void testEqualsWithDifferentType() {
        ru.nsu.gaev.expression.Number num = new ru.nsu.gaev.expression.Number(5);
        assertNotEquals(num, "5");
    }

    @Test
    void testEqualsWithNull() {
        ru.nsu.gaev.expression.Number num = new Number(5);
        assertNotEquals(null, num);
    }
}