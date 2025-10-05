package ru.nsu.gaev;

import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DivTest {

    // Проверка вычисления выражения
    @Test
    void testEval() {
        Expression x = new Variable("x");
        Expression y = new Variable("y");
        Expression expr = new Div(new Add(x, new Number(2)), new Add(new Number(3), y));

        Map<String, Integer> vars = new HashMap<>();
        vars.put("x", 5);  // x = 5
        vars.put("y", 10); // y = 10

        // Печать переменных для отладки
        System.out.println("Vars: " + vars);

        int result = expr.eval(vars);

        // Печать результата для отладки
        System.out.println("Result: " + result);

        assertEquals(0, result);  // Ожидаемый результат: (5 + 2) / (3 + 10) = 7 / 13
    }

    // Проверка производной выражения
    @Test
    void testDerivative() {
        Expression x = new Variable("x");
        Expression y = new Variable("y");
        Expression expr = new Div(new Add(x, new ru.nsu.gaev.Number(2)), new Add(new ru.nsu.gaev.Number(3), y));

        // Производная от (x + 2) / (3 + y) по x
        Expression derivative = expr.derivative("x");

        // Присваиваем значение для переменной y
        Map<String, Integer> vars = new HashMap<>();
        vars.put("x", 5);  // Обязательно присваиваем x значение для производной
        vars.put("y", 10);  // Обязательно присваиваем y значение для производной

        // Печать переменных для отладки
        System.out.println("Vars: " + vars);

        int derivativeResult = derivative.eval(vars);
        // Производная от (x + 2) / (3 + y) по x: (1 * (3 + y) - (x + 2) * 0) / (3 + y)^2
        // (1 * (3 + 10)) / (13^2) = 13 / 169 = 1 / 13
        System.out.println("Derivative result: " + derivativeResult);
        assertEquals(1 / 169, derivativeResult);
    }

    // Проверка упрощения выражения
    @Test
    void testSimplify() {
        Expression expr = new Div(new ru.nsu.gaev.Number(6), new ru.nsu.gaev.Number(3));
        Expression simplifiedExpr = expr.simplify();

        // Ожидаем, что (6 / 3) будет упрощено до 2
        assertTrue(simplifiedExpr instanceof ru.nsu.gaev.Number);
        assertEquals(2, ((ru.nsu.gaev.Number) simplifiedExpr).getValue());
    }

    // Проверка строкового представления выражения
    @Test
    void testToExpressionString() {
        Expression x = new Variable("x");
        Expression expr = new Div(x, new ru.nsu.gaev.Number(2));

        String expressionString = expr.toExpressionString();
        assertEquals("(x/2)", expressionString);
    }

    // Проверка равенства выражений
    @Test
    void testEquals() {
        Expression x = new Variable("x");
        Expression expr1 = new Div(x, new ru.nsu.gaev.Number(2));
        Expression expr2 = new Div(x, new ru.nsu.gaev.Number(2));
        Expression expr3 = new Div(x, new ru.nsu.gaev.Number(3));

        assertEquals(expr1, expr2); // Они должны быть равны
        assertNotEquals(expr1, expr3); // Они не должны быть равны
    }

    // Проверка хеш-кодов
    @Test
    void testHashCode() {
        Expression x = new Variable("x");
        Expression expr1 = new Div(x, new ru.nsu.gaev.Number(2));
        Expression expr2 = new Div(x, new ru.nsu.gaev.Number(2));
        Expression expr3 = new Div(x, new ru.nsu.gaev.Number(3));

        assertEquals(expr1.hashCode(), expr2.hashCode()); // Хеш-коды должны быть одинаковыми
        assertNotEquals(expr1.hashCode(), expr3.hashCode()); // Хеш-коды должны быть разными
    }

    // Проверка упрощения, когда деление на 1
    @Test
    void testSimplifyDivByOne() {
        Expression expr = new Div(new ru.nsu.gaev.Number(5), new ru.nsu.gaev.Number(1));
        Expression simplifiedExpr = expr.simplify();

        // Ожидаем, что (5 / 1) будет упрощено до 5
        assertTrue(simplifiedExpr instanceof ru.nsu.gaev.Number);
        assertEquals(5, ((ru.nsu.gaev.Number) simplifiedExpr).getValue());
    }

    // Проверка упрощения, когда числитель равен 0
    @Test
    void testSimplifyDivZeroNumerator() {
        Expression expr = new Div(new ru.nsu.gaev.Number(0), new ru.nsu.gaev.Number(3));
        Expression simplifiedExpr = expr.simplify();

        // Ожидаем, что (0 / 3) будет упрощено до 0
        assertTrue(simplifiedExpr instanceof ru.nsu.gaev.Number);
        assertEquals(0, ((ru.nsu.gaev.Number) simplifiedExpr).getValue());
    }
}
