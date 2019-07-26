package com.demointerpreter.interpreter;

import com.demointerpreter.grammar.Statement;

import java.util.List;

public class LoxFunction implements LoxCallable {

    private final Statement.Function declaration;

    private final Envirenment closure;

    public LoxFunction(Statement.Function declaration, Envirenment closure) {
        this.declaration = declaration;
        this.closure = closure;
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> args) {
        Envirenment envirenment = new Envirenment(closure);
        for (int i = 0; i < declaration.params.size(); i++) {
            envirenment.define(declaration.params.get(i), args.get(i));
        }
        try {
            interpreter.executeBlock(declaration.body, envirenment);
        } catch (Return returnValue) {
            return returnValue.getValue();
        }
        return null;
    }

    @Override
    public int arity() {
        return declaration.params.size();
    }

    @Override
    public String toString() {
        return "<Function: " + declaration.name.getText() +">";
    }
}
