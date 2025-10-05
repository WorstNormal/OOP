package ru.nsu.gaev;

import org.junit.jupiter.api.Test;
import java.util.HashMap;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;


class NumberTest {

    @Test
    void testGetValue() {
        Number num = new Number(42);
        assertEquals(42, num.getValue());
    }

    @Test
    void testEvalReturnsValueIgnoringVars() {
        Number num = new Number(10);
        assertEquals(10, num.eval(new HashMap<>()));
    }

    @Test
    void testDerivativeAlwaysZero() {
        Number num = new Number(5);
        Expression derivative = num.derivative("x");
        assertEquals(new Number(0), derivative);
    }

    @Test
    void testSimplifyReturnsSameInstance() {
        Number num = new Number(7);
        Expression simplified = num.simplify();
        assertSame(num, simplified);
    }

    @Test
    void testToExpressionString() {
        Number num = new Number(-8);
        assertEquals("-8", num.toExpressionString());
    }

    @Test
    void testEqualsAndHashCode() {
        Number a = new Number(3);
        Number b = new Number(3);
        Number c = new Number(4);

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, c);
    }

    @Test
    void testEqualsSelf() {
        Number num = new Number(9);
        assertEquals(num, num);
    }

    @Test
    void testEqualsWithDifferentType() {
        Number num = new Number(5);
        assertNotEquals(num, "5");
    }

    @Test
    void testEqualsWithNull() {
        Number num = new Number(5);
        assertNotEquals(null, num);
    }
}
