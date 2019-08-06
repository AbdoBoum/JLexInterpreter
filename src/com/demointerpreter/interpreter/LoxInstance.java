package com.demointerpreter.interpreter;

import com.demointerpreter.lexical_analyzer.Token;

import java.util.HashMap;
import java.util.Map;

public class LoxInstance {
    private LoxClass _class;
    private final Map<String, Object> fields = new HashMap<>();


    public LoxInstance(LoxClass _class) {
        this._class = _class;
    }

    public Object get(Token name) {
        if (fields.containsKey(name.getText())) {
            return fields.get(name.getText());
        }
        throw new RuntimeError(name, "Undefined property '" + name.getText() + "'.");
    }

    public void set(Token token, Object value) {
        fields.put(token.getText(), value);
    }

    @Override
    public String toString() {
        return "[" + _class.getName() + "] instance.";
    }
}
