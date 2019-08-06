package com.demointerpreter.interpreter;

import java.util.List;

public class LoxClass implements LoxCallable{

    private final String name;

    public LoxClass(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> args) {
        LoxInstance instance = new LoxInstance(this);
        return instance;
    }

    @Override
    public int arity() {
        return 0;
    }
}
