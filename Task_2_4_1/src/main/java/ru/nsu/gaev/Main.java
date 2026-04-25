package ru.nsu.gaev;

import ru.nsu.gaev.oopchecker.OopCheckerApplication;

public final class Main {
    private Main() {
    }

    public static void main(String[] args) throws Exception {
        new OopCheckerApplication().run(args, System.out);
    }
}
