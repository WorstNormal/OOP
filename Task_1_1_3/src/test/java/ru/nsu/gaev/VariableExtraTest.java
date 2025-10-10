package ru.nsu.gaev;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

class VariableExtraTest {

    @Test
    void evalReturnsAssignedValueFromMap() {
        Variable v = new Variable("x");
        Map<String, Integer> m = new HashMap<>();
        m.put("x", 42);
        assertEquals(42, v.eval(m));
    }

    @Test
    void evalThrowsWhenVariableNotPresent() {
        Variable v = new Variable("y");
        Map<String, Integer> m = new HashMap<>();
        // no entry for 'y' -> should throw
        assertThrows(RuntimeException.class, () -> v.eval(m));
    }

    @Test
    void derivativeIsOneForSameVariableAndZeroForOther() {
        Variable v = new Variable("x");
        Expression dSame = v.derivative("x");
        Expression dOther = v.derivative("z");
        assertEquals(new Number(1), dSame);
        assertEquals(new Number(0), dOther);
    }

    @Test
    void simplifyReturnsSelf() {
        Variable v = new Variable("alpha");
        assertEquals(v, v.simplify());
    }

    @Test
    void toExpressionStringAndEqualsAndHashCode() {
        Variable a = new Variable("a");
        Variable a2 = new Variable("a");
        Variable b = new Variable("b");

        assertEquals("a", a.toExpressionString());
        assertEquals(a, a2);
        assertTrue(a.hashCode() == a2.hashCode());
        assertTrue(!a.equals(b));
    }

    @Test
    void expressionEvalStringAssignmentWorks() {
        // Expression.eval(String) should parse assignments like "x=5"
        Expression e = new Variable("x");
        int value = e.eval("x=5");
        assertEquals(5, value);
    }

    @Test
    void getNameAndEqualsNullAndOtherType() {
        Variable v = new Variable("name");
        assertEquals("name", v.getName());

        // equals with null should return false
        assertFalse(v.equals(null));

        // equals with object of different type should return false
        Object other = "name";
        assertFalse(v.equals(other));
    }
}
