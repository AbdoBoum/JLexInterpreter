package com.demointerpreter.Parser;

import com.demointerpreter.Main;
import com.demointerpreter.grammar.Expression;
import com.demointerpreter.lexical_analyzer.Token;
import com.demointerpreter.lexical_analyzer.TokenType;

import java.util.List;

import static com.demointerpreter.lexical_analyzer.TokenType.*;

public class Parser {

    //used to unwind the parser
    private static class ParserError extends RuntimeException {
    }

    private final List<Token> tokens;
    private int current = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public Expression parse() {
        try {
            return expression();
        } catch (ParserError error) {
            return null;
        }
    }

    private Expression expression() {
        return equality();
    }

    private Expression equality() {
        var expression = comparaison();
        while (match(BANG_EQ, EQ_EQ)) {
            var operator = previous();
            var right = comparaison();
            expression = new Expression.Binary(expression, operator, right);
        }
        return expression;
    }

    private Expression comparaison() {
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
        return primary();
    }

    private Expression primary() {
        if (match(FALSE)) return new Expression.Literal(false);
        if (match(TRUE)) return new Expression.Literal(true);
        if (match(NIL)) return new Expression.Literal(null);
        if (match(NUMBER, STRING)) {
            return new Expression.Literal(previous().getLiteral());
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

    private boolean match(TokenType... types) {
        for (TokenType type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }
        return false;
    }

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
