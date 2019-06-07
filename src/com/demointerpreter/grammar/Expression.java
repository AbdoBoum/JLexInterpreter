package com.demointerpreter.grammar;

import com.demointerpreter.lexical_analyzer.Token;

abstract class Expression {

    static class Binary extends Expression {
        public Expression left;
        public Token operator;
        public Expression right;

        public Binary(Expression left, Token operator, Expression right) {
            this.left = left;
            this.operator = operator;
            this.right = right;
        }

    }

    static class Grouping extends Expression {
        public Expression expression;

        public Grouping(Expression expression) {
            this.expression = expression;
        }

    }

    static class Literal extends Expression {
        public Object value;

        public Literal(Object value) {
            this.value = value;
        }

    }

    static class Unary extends Expression {
        public Token operator;
        public Expression right;

        public Unary(Token operator, Expression right) {
            this.operator = operator;
            this.right = right;
        }

    }
}
