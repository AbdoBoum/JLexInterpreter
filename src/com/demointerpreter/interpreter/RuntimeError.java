package com.demointerpreter.interpreter;

import com.demointerpreter.lexical_analyzer.Token;

public class RuntimeError extends RuntimeException {
    private Token token;

    public RuntimeError(Token token, String message) {
        super(message);
        this.token = token;
    }

    public Token getToken() {
        return this.token;
    }

    @Override
    public String toString() {
        return String.format("[line %d] RuntimeError: at '%s' %s", token.getLine(), token.getText(), getMessage());
    }

}
