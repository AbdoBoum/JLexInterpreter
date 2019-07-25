package com.demointerpreter.grammar;

import java.util.List;

import com.demointerpreter.lexical_analyzer.Token;

public interface Statement {

    <R> R accept(Visitor<R> visitor);

    interface Visitor<R> {
        R visitExpressionStatement(Expression statement);
        R visitPrintStatement(Print statement);
        R visitVarStatement(Var statement);
        R visitBlockStatement(Block statement);
        R visitIfStatement(If statement);
        R visitWhileStatement(While statement);
    }

    class Expression implements Statement {
        public com.demointerpreter.grammar.Expression expression;

        public Expression(com.demointerpreter.grammar.Expression expression) {
                this.expression = expression;
}

        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitExpressionStatement(this);
}
 }

    class Print implements Statement {
        public com.demointerpreter.grammar.Expression expression;

        public Print(com.demointerpreter.grammar.Expression expression) {
                this.expression = expression;
}

        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitPrintStatement(this);
}
 }

    class Var implements Statement {
        public Token name;
        public com.demointerpreter.grammar.Expression initializer;

        public Var(Token name, com.demointerpreter.grammar.Expression initializer) {
                this.name = name;
                this.initializer = initializer;
}

        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitVarStatement(this);
}
 }

    class Block implements Statement {
        public List<Statement> statements;

        public Block(List<Statement> statements) {
                this.statements = statements;
}

        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitBlockStatement(this);
}
 }

    class If implements Statement {
        public com.demointerpreter.grammar.Expression condition;
        public Statement thenBranch;
        public Statement elseBranch;

        public If(com.demointerpreter.grammar.Expression condition, Statement thenBranch, Statement elseBranch) {
                this.condition = condition;
                this.thenBranch = thenBranch;
                this.elseBranch = elseBranch;
}

        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitIfStatement(this);
}
 }

    class While implements Statement {
        public com.demointerpreter.grammar.Expression condition;
        public Statement statement;

        public While(com.demointerpreter.grammar.Expression condition, Statement statement) {
                this.condition = condition;
                this.statement = statement;
}

        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitWhileStatement(this);
}
 }
 }
