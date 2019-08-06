package com.demointerpreter.grammar;

import java.util.List;

import com.demointerpreter.lexical_analyzer.Token;

public interface Expression {

    <R> R accept(Visitor<R> visitor);

    interface Visitor<R> {
        R visitBinaryExpression(Binary expression);
        R visitGroupingExpression(Grouping expression);
        R visitLiteralExpression(Literal expression);
        R visitUnaryExpression(Unary expression);
        R visitLogicalExpression(Logical expression);
        R visitAssignExpression(Assign expression);
        R visitThisExpression(This expression);
        R visitCallExpression(Call expression);
        R visitGetExpression(Get expression);
        R visitSetExpression(Set expression);
        R visitVariableExpression(Variable expression);
    }

    class Binary implements Expression {
        public com.demointerpreter.grammar.Expression left;
        public Token operator;
        public com.demointerpreter.grammar.Expression right;

        public Binary(com.demointerpreter.grammar.Expression left, Token operator, com.demointerpreter.grammar.Expression right) {
                this.left = left;
                this.operator = operator;
                this.right = right;
}

        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitBinaryExpression(this);
}
 }

    class Grouping implements Expression {
        public com.demointerpreter.grammar.Expression expression;

        public Grouping(com.demointerpreter.grammar.Expression expression) {
                this.expression = expression;
}

        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitGroupingExpression(this);
}
 }

    class Literal implements Expression {
        public Object value;

        public Literal(Object value) {
                this.value = value;
}

        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitLiteralExpression(this);
}
 }

    class Unary implements Expression {
        public Token operator;
        public com.demointerpreter.grammar.Expression right;

        public Unary(Token operator, com.demointerpreter.grammar.Expression right) {
                this.operator = operator;
                this.right = right;
}

        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitUnaryExpression(this);
}
 }

    class Logical implements Expression {
        public com.demointerpreter.grammar.Expression left;
        public Token operator;
        public com.demointerpreter.grammar.Expression right;

        public Logical(com.demointerpreter.grammar.Expression left, Token operator, com.demointerpreter.grammar.Expression right) {
                this.left = left;
                this.operator = operator;
                this.right = right;
}

        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitLogicalExpression(this);
}
 }

    class Assign implements Expression {
        public Token name;
        public com.demointerpreter.grammar.Expression value;

        public Assign(Token name, com.demointerpreter.grammar.Expression value) {
                this.name = name;
                this.value = value;
}

        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitAssignExpression(this);
}
 }

    class This implements Expression {
        public Token keyword;

        public This(Token keyword) {
                this.keyword = keyword;
}

        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitThisExpression(this);
}
 }

    class Call implements Expression {
        public com.demointerpreter.grammar.Expression callee;
        public Token paren;
        public List<com.demointerpreter.grammar.Expression> arguments;

        public Call(com.demointerpreter.grammar.Expression callee, Token paren, List<com.demointerpreter.grammar.Expression> arguments) {
                this.callee = callee;
                this.paren = paren;
                this.arguments = arguments;
}

        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitCallExpression(this);
}
 }

    class Get implements Expression {
        public com.demointerpreter.grammar.Expression object;
        public Token name;

        public Get(com.demointerpreter.grammar.Expression object, Token name) {
                this.object = object;
                this.name = name;
}

        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitGetExpression(this);
}
 }

    class Set implements Expression {
        public com.demointerpreter.grammar.Expression object;
        public Token name;
        public com.demointerpreter.grammar.Expression value;

        public Set(com.demointerpreter.grammar.Expression object, Token name, com.demointerpreter.grammar.Expression value) {
                this.object = object;
                this.name = name;
                this.value = value;
}

        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitSetExpression(this);
}
 }

    class Variable implements Expression {
        public Token name;

        public Variable(Token name) {
                this.name = name;
}

        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitVariableExpression(this);
}
 }
 }
