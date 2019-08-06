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
        R visitClassStatement(Class statement);
        R visitIfStatement(If statement);
        R visitFunctionStatement(Function statement);
        R visitWhileStatement(While statement);
        R visitReturnStatement(Return statement);
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

    class Class implements Statement {
        public Token name;
        public List<Statement.Function> methods;

        public Class(Token name, List<Statement.Function> methods) {
                this.name = name;
                this.methods = methods;
}

        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitClassStatement(this);
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

    class Function implements Statement {
        public Token name;
        public List<Token> params;
        public List<Statement> body;

        public Function(Token name, List<Token> params, List<Statement> body) {
                this.name = name;
                this.params = params;
                this.body = body;
}

        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitFunctionStatement(this);
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

    class Return implements Statement {
        public Token keyword;
        public com.demointerpreter.grammar.Expression expression;

        public Return(Token keyword, com.demointerpreter.grammar.Expression expression) {
                this.keyword = keyword;
                this.expression = expression;
}

        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitReturnStatement(this);
}
 }
 }
