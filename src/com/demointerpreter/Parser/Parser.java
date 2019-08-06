package com.demointerpreter.Parser;

import com.demointerpreter.Main;
import com.demointerpreter.grammar.Expression;
import com.demointerpreter.grammar.Statement;
import com.demointerpreter.lexical_analyzer.Token;
import com.demointerpreter.lexical_analyzer.TokenType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.demointerpreter.lexical_analyzer.TokenType.*;

public class Parser {

    private int loopDepth = 0;

    //used to unwind the parser
    private static class ParserError extends RuntimeException {
    }

    private final List<Token> tokens;
    private int current = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public List<Statement> parse() {
        List<Statement> statements = new ArrayList<>();
        while (!isAtEnd()) {
            statements.add(declaration());
        }
        return statements;
    }

    private Statement declaration() {
        try {
            if (match(VAR)) return varDeclaration();
            return statement();
        } catch (ParserError error) {
            synchronize();
            return null;
        }
    }

    private Statement varDeclaration() {
        Token name = consume(IDF, "Expect a variable name.");
        Expression initializer = null;
        if (match(EQ)) {
            initializer = expression();
        }
        consume(SEMICOLON, "Expect ';' after variable declaration.");
        return new Statement.Var(name, initializer);
    }

    private Statement statement() {
        if (match(RETURN)) return returnStatement();
        if (match(CLASS)) return classDeclaration();
        if (match(FUN)) return function("function");
        if (match(FOR)) return forStatement();
        if (match(WHILE)) return whileStatement();
        if (match(BREAK)) return breakStatement();
        if (match(IF)) return ifStatement();
        if (match(PRINT)) return printStatement();
        if (match(LEFT_BRACE)) return new Statement.Block(blockStatement());
        return expressionStatement();
    }

    private Statement returnStatement() {
        var keyword = previous();
        Expression value = null;
        if (!check(SEMICOLON) && !isAtEnd()) {
            value = expression();
        }
        consume(SEMICOLON, "Expect ';' after return statement.");
        return new Statement.Return(keyword, value);
    }

    private Statement classDeclaration() {
        var name = consume(IDF, "Expect a class name.");
        consume(LEFT_BRACE, "Expect '{' after class name.");
        List<Statement.Function> methods = new ArrayList<>();
        while (!check(RIGHT_BRACE)) {
            methods.add(function("method"));
        }
        consume(RIGHT_BRACE, "Expect '}' after class declaration.");

        return new Statement.Class(name, methods);
    }

    private Statement.Function function(String kind) {
        var name = consume(IDF, "Expect " + kind + " name.");
        consume(LEFT_PAR, "Expect '(' after " + kind + " name.");
        List<Token> params = new ArrayList<>();
        if (!check(RIGHT_PAR)) {
            do {
                params.add(consume(IDF, "Expect parameter name."));
            } while (match(COMMA));
        }
        consume(RIGHT_PAR, "Expect ')' after parameters.");
        consume(LEFT_BRACE, "Expect '{' before " + kind + " body.");
        var body = blockStatement();
        return new Statement.Function(name, params, body);
    }

    private Statement forStatement() {
        loopDepth++;
        try {
            consume(LEFT_PAR, "Expect '(' after 'for'.");
            Statement initializer;
            if (match(SEMICOLON)) {
                initializer = null;
            } else if (match(VAR)) {
                initializer = varDeclaration();
            } else {
                initializer = expressionStatement();
            }
            Expression condition = null;
            if (!check(SEMICOLON)) {
                condition = expression();
            }
            consume(SEMICOLON, "Expect ';' after loop condition.");
            Expression increment = null;
            if (!check(RIGHT_PAR)) {
                increment = expression();
            }
            consume(RIGHT_PAR, "Expect ')' after for clauses.");
            var body = statement();
            if (increment != null) {
                body = new Statement.Block(Arrays.asList(
                        body,
                        new Statement.Expression(increment)
                ));
            }
            if (condition == null) condition = new Expression.Literal(true);
            body = new Statement.While(condition, body);
            if (initializer != null) {
                body = new Statement.Block(Arrays.asList(initializer, body));
            }
            return body;
        } finally {
            loopDepth--;
        }
    }

    private Statement whileStatement() {
        loopDepth++;
        try {
            consume(LEFT_PAR, "Expect '(' after 'while'.");
            var condition = expression();
            consume(RIGHT_PAR, "Expect ')' after while condition.");
            var body = statement();
            return new Statement.While(condition, body);
        } finally {
            loopDepth--;
        }
    }

    private Statement breakStatement() {
        if (loopDepth == 0) {
            error(previous(), "Break outside a loop.");
        }
        consume(SEMICOLON, "Expect ';' after break.");
        return null;
    }

    private Statement ifStatement() {
        consume(LEFT_PAR, "Expect '(' after 'if'.");
        var condition = expression();
        consume(RIGHT_PAR, "Expect ')' after if condition.");
        var thenBranch = statement();
        Statement elseStatement = null;
        if (match(ELSE)) {
            elseStatement = statement();
        }
        return new Statement.If(condition, thenBranch, elseStatement);
    }

    private Statement printStatement() {
        var value = expression();
        consume(SEMICOLON, "Expect ';' after value.");
        return new Statement.Print(value);
    }

    private List<Statement> blockStatement() {
        List<Statement> statements = new ArrayList<>();
        while (!check(RIGHT_BRACE) && !isAtEnd()) {
            statements.add(declaration());
        }
        consume(RIGHT_BRACE, "Expect '}' after block.");
        return statements;
    }

    private Statement expressionStatement() {
        var expression = expression();
        consume(SEMICOLON, "Expect ';' after expression.");
        return new Statement.Expression(expression);
    }

    private Expression expression() {
        return assignment();
    }

    /*private Expression conditional() {
        var expression = equality();
        if (match(QUESTION)) {
            var thenBranch = expression();
            consume(COLON, "Expect ':' after then branch of conditional expression.");
            var elseBranch = conditional();
            expression = new Expression.Conditional(expression, thenBranch, elseBranch);
        }

        return expression;
    }*/

    //comma → equality ( "," equality )* ;
    /*private Expression comma() {
        var expression = equality();
        while (match(COMMA)) {
            var operator = previous();
            var right = equality();
            expression = new Expression.Binary(expression, operator, right);
        }
        return expression;
    }*/

    private Expression assignment() {
        var expression = or();
        if (match(EQ)) {
            Token equals = previous();
            Expression value = assignment();

            if (expression instanceof Expression.Variable) {
                Token name = ((Expression.Variable) expression).name;
                return new Expression.Assign(name, value);
            } else if (expression instanceof Expression.Get) {
                var get = (Expression.Get) expression;
                return new Expression.Set(get.object, get.name, value);
            }
            error(equals, "Invalid assignment target.");
        }
        return expression;
    }

    private Expression or() {
        var expression = and();
        while (match(OR)) {
            var operation = previous();
            var right = and();
            expression = new Expression.Logical(expression, operation, right);
        }
        return expression;
    }

    private Expression and() {
        var expression = equality();
        while (match(AND)) {
            var operatior = previous();
            var right = equality();
            expression = new Expression.Logical(expression, operatior, right);
        }
        return expression;
    }

    private Expression equality() {
        var expression = comparison();
        while (match(BANG_EQ, EQ_EQ)) {
            var operator = previous();
            var right = comparison();
            expression = new Expression.Binary(expression, operator, right);
        }
        return expression;
    }

    private Expression comparison() {
        var expression = addition();
        while (match(GREATER, GREATER_EQ, LESS, LESS_EQ)) {
            var operator = previous();
            var right = addition();
            expression = new Expression.Binary(expression, operator, right);
        }
        return expression;
    }

    private Expression addition() {
        var expression = multiplication();
        while (match(MINUS, PLUS)) {
            var operator = previous();
            var right = multiplication();
            expression = new Expression.Binary(expression, operator, right);
        }
        return expression;
    }

    private Expression multiplication() {
        var expression = unary();

        while (match(SLASH, STAR)) {
            var operator = previous();
            var right = unary();
            expression = new Expression.Binary(expression, operator, right);
        }
        return expression;
    }

    private Expression unary() {
        while (match(BANG, MINUS)) {
            var operator = previous();
            var right = unary();
            return new Expression.Unary(operator, right);
        }
        return call();
    }

    private Expression call() {
        var callee = primary();
        while (true) {
            if (match(LEFT_PAR)) {
                callee = finishCall(callee);
            } else if (match(DOT)) {
                var name = consume(IDF, "Expect property name after '.'.");
                callee = new Expression.Get(callee, name);
            } else {
                break;
            }
        }
        return callee;
    }

    private Expression finishCall(Expression callee) {
        List<Expression> args = new ArrayList<>();
        if (!check(RIGHT_PAR)) {
            do {
                if (args.size() >= 255) {
                    error(peek(), "Cannot have more than 255 arguments.");
                }
                args.add(expression());
            } while (match(COMMA));
        }
        Token paren = consume(RIGHT_PAR, "Expect ')' after arguments.");
        return new Expression.Call(callee, paren, args);
    }

    private Expression primary() {
        if (match(FALSE)) return new Expression.Literal(false);
        if (match(TRUE)) return new Expression.Literal(true);
        if (match(NIL)) return new Expression.Literal(null);
        if (match(NUMBER, STRING)) {
            return new Expression.Literal(previous().getLiteral());
        }
        if (match(IDF)) {
            return new Expression.Variable(previous());
        }
        if (match(LEFT_PAR)) {
            Expression expression = expression();
            consume(RIGHT_PAR, "Expect ')' after expression.");
            return new Expression.Grouping(expression);
        }
        throw error(peek(), "Expect expression.");
    }

    private Token consume(TokenType type, String message) {
        if (check(type)) {
            return advance();
        }
        throw error(peek(), message);
    }

    private ParserError error(Token token, String message) {
        Main.error(token, message);
        return new ParserError();
    }

    // Discards tokens until it thinks it found a statement boundary
    private void synchronize() {
        advance();
        while (!isAtEnd()) {
            if (previous().getType() == SEMICOLON) return;
            switch (peek().getType()) {
                case CLASS:
                case FUN:
                case VAR:
                case FOR:
                case WHILE:
                case PRINT:
                case RETURN:
                    return;
            }
            advance();
        }
    }

    // consume the token
    private boolean match(TokenType... types) {
        for (TokenType type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }
        return false;
    }

    // doesn't consume the token
    private boolean check(TokenType type) {
        if (isAtEnd()) return false;
        return peek().getType() == type;
    }

    private Token advance() {
        if (!isAtEnd()) current++;
        return previous();
    }

    private boolean isAtEnd() {
        return peek().getType() == EOF;
    }

    private Token peek() {
        return tokens.get(current);
    }

    private Token previous() {
        return tokens.get(current - 1);
    }
}
