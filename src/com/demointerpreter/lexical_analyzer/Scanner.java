package com.demointerpreter.lexical_analyzer;

import com.demointerpreter.Main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.demointerpreter.lexical_analyzer.TokenType.*;

public class Scanner {
    private String source;
    private List<Token> tokens = new ArrayList<>();

    private int start = 0;
    private int current = 0;
    private int line = 1;

    private static final Map<String, TokenType> keywords;

    static {
        keywords = new HashMap<>();
        keywords.put("and", AND);
        keywords.put("class", CLASS);
        keywords.put("else", ELSE);
        keywords.put("false", FALSE);
        keywords.put("for", FOR);
        keywords.put("fun", FUN);
        keywords.put("if", IF);
        keywords.put("nil", NIL);
        keywords.put("or", OR);
        keywords.put("print", PRINT);
        keywords.put("return", RETURN);
        keywords.put("super", SUPER);
        keywords.put("this", THIS);
        keywords.put("true", TRUE);
        keywords.put("var", VAR);
        keywords.put("while", WHILE);
    }

    public Scanner(String source) {
        if (source == null) throw new IllegalArgumentException("Source code should not be null!");
        this.source = source;
    }

    public List<Token> scanTokens() {
        while (!isEnd()) {
            // We are at the beginning of the next lexeme.
            start = current;
            scantToken();
        }
        tokens.add(new Token(EOF, "", null, line));
        return tokens;
    }

    //tells us if we’ve consumed all the characters
    private boolean isEnd() {
        return current >= source.length();
    }

    private void scantToken() {
        var c = advance();
        switch (c) {
            case '(':
                addToken(LEFT_PAR);
                break;
            case ':':
                addToken(COLON);
                break;
            case '*':
                addToken(STAR);
                break;
            case ')':
                addToken(RIGHT_PAR);
                break;
            case '{':
                addToken(LEFT_BRACE);
                break;
            case '}':
                addToken(RIGHT_BRACE);
                break;
            case '.':
                addToken(DOT);
                break;
            case ';':
                addToken(SEMICOLON);
                break;
            case ',':
                addToken(COMMA);
                break;
            case '+':
                addToken(PLUS);
                break;
            case '-':
                addToken(MINUS);
                break;
            case '>':
                addToken(match('=') ? GREATER_EQ : GREATER);
                break;
            case '<':
                addToken(match('=') ? LESS_EQ : LESS);
                break;
            case '=':
                addToken(match('=') ? EQ_EQ : EQ);
                break;
            case '?':
                addToken(QUESTION);
                break;
            case '!':
                addToken(match('=') ? BANG_EQ : BANG);
                break;
            case '/':
                if (match('/')) {
                    skipOneLineComment();
                } else if (match('*')) {
                    skipBlockComment();
                } else {
                    addToken(SLASH);
                }
                break;
            case '\n':
                line++;
                break;
            case ' ':
            case '\r':
            case '\t':
                break;
            case '"':
                string();
                break;
            default:
                if (isDigit(c)) {
                    number();
                } else if (isAlpha(c)) {
                    identifier();
                } else {
                    Main.error(line, "Unexpected character.");
                }
                break;
        }
    }

    //Consumes the next character in the source file and returns it
    private char advance() {
        current++;
        return source.charAt(current - 1);
    }

    //Consumes the current character if it’s what we’re looking for.
    private boolean match(char expected) {
        if (!isEnd() && source.charAt(current) == expected) {
            current++;
            return true;
        }
        return false;
    }

    //It’s sort of like advance(), but does not consume the character
    private char peek() {
        if (isEnd()) return '\0';
        return source.charAt(current);
    }

    private char peekNext() {
        if (current + 1 >= source.length()) return '\0';
        return source.charAt(current + 1);
    }

    private void string() {
        while (peek() != '"' && !isEnd()) {
            if (peek() == '\n') line++;
            advance();
        }
        if (isEnd()) {
            Main.error(line, "Unterminated String.");
        } else {
            advance();     // The closing "
            var text = source.substring(start + 1, current - 1); // Trim the surrounding quotes
            addToken(STRING, text);
        }
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private void number() {
        while (isDigit(peek())) {
            advance();
        }
        if (peek() == '.' && isDigit(peekNext())) {
            advance();
            while (isDigit(peek())) {
                advance();
            }
        }
        addToken(NUMBER, Double.parseDouble(source.substring(start, current)));
    }

    private void identifier() {
        while (isAlphaNumeric(peek())) {
            advance();
        }
        var text = source.substring(start, current);
        var type = keywords.get(text);
        addToken(type == null ? IDF : type);
    }

    private boolean isAlpha(char c) {
        return (c >= 'A' && c <= 'Z') ||
                (c >= 'a' && c <= 'z') ||
                c == '_';
    }

    private boolean isAlphaNumeric(char c) {
        return isDigit(c) || isAlpha(c);
    }

    private void skipOneLineComment() {
        while (peek() != '\n' && !isEnd()) {
            advance();
        }
    }

    private void skipBlockComment() {
        var nested = 1;
        while (nested > 0) {
            if (isEnd()) {
                Main.error(line, "Unterminated comment block.");
                return;
            }
            if (peek() == '\n') line++;
            if (peek() == '/' && peekNext() == '*') {
                nested++;
                advance();
                advance();
                continue;
            }
            if (peek() == '*' && peekNext() == '/') {
                nested--;
                advance();
                advance();
                continue;
            }
            advance();
        }
    }

    private void addToken(TokenType type) {
        addToken(type, null);
    }

    // Overload for addToken to handle token with literal values
    private void addToken(TokenType type, Object literal) {
        var text = source.substring(start, current);
        tokens.add(new Token(type, text, literal, line));
    }

}
