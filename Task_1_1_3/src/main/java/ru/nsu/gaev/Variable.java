package ru.nsu.gaev;

import java.util.Map;

/**
 * Класс {@code Variable} представляет переменную в математическом выражении.
 * Переменная имеет имя и может быть вычислена при подстановке значений.
 */
public class Variable extends Expression {

    /** Имя переменной. */
    private final String name;

    /**
     * Создаёт переменную с указанным именем.
     *
     * @param name имя переменной
     */
    public Variable(String name) {
        this.name = name;
    }

    /**
     * Возвращает имя переменной.
     *
     * @return имя переменной
     */
    public String getName() {
        return name;
    }

    /**
     * Вычисляет значение переменной на основе переданных значений.
     *
     * @param vars отображение имён переменных и их значений
     * @return значение переменной
     * @throws RuntimeException если переменная не имеет присвоенного значения
     */
    @Override
    public int eval(Map<String, Integer> vars) {
        if (!vars.containsKey(name)) {
            throw new RuntimeException("Переменная '" + name + "' не имеет значения");
        }
        return vars.get(name);
    }

    /**
     * Вычисляет производную переменной по указанной переменной.
     * Производная равна 1, если имя совпадает с переменной, и 0 — в противном случае.
     *
     * @param var имя переменной, по которой берётся производная
     * @return {@code Number(1)} если это та же переменная, иначе {@code Number(0)}
     */
    @Override
    public Expression derivative(String var) {
        return new Number(name.equals(var) ? 1 : 0);
    }

    /**
     * Возвращает упрощённое выражение (для переменной — это она сама).
     *
     * @return текущий объект {@code Variable}
     */
    @Override
    public Expression simplify() {
        return this;
    }

    /**
     * Возвращает строковое представление переменной.
     *
     * @return имя переменной в виде строки
     */
    @Override
    protected String toExpressionString() {
        return name;
    }

    /**
     * Проверяет равенство двух переменных.
     *
     * @param o объект для сравнения
     * @return {@code true}, если имена переменных совпадают
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Variable)) {
            return false;
        }
        Variable variable = (Variable) o;
        return name.equals(variable.name);
    }

    /**
     * Вычисляет хеш-код переменной.
     *
     * @return хеш-код имени переменной
     */
    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
