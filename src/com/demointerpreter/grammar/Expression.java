package com.demointerpreter.grammar;

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
    }

    class Binary implements Expression {
        public Expression left;
        public Token operator;
        public Expression right;

        public Binary(Expression left, Token operator, Expression right) {
            this.left = left;
            this.operator = operator;
            this.right = right;
        }

        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitBinaryExpression(this);
        }
    }

    class Grouping implements Expression {
        public Expression expression;

        public Grouping(Expression expression) {
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
        public Expression right;

        public Unary(Token operator, Expression right) {
            this.operator = operator;
            this.right = right;
        }

        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitUnaryExpression(this);
        }
    }

    class Logical implements Expression {
        public Expression left;
        public Token operator;
        public Expression right;

        public Logical(Expression left, Token operator, Expression right) {
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
        public Expression value;

        public Assign(Token name, Expression value) {
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

    class Conditional implements Expression {
        public Expression expression, thenBranch, elseBranch;

        public Conditional(Expression expression, Expression thenBranch, Expression elseBranch) {
            this.expression = expression;
            this.thenBranch = thenBranch;
            this.elseBranch = elseBranch;
        }

        @Override
        public <R> R accept(Visitor<R> visitor) {
            return null;
        }
    }
}
