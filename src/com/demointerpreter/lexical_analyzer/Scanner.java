package com.demointerpreter.lexical_analyzer;

import com.demointerpreter.Main;
import com.sun.xml.internal.bind.v2.TODO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.demointerpreter.lexical_analyzer.TokenType.*;

public class Scanner {
    private String source;
    private List<Token> tokens;
    private int start;
    private int current;
    private int line;
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
        this.source = source;
        tokens = new ArrayList<>();
        start = 0;
        current = 0;
        line = 1;
    }

    public List<Token> scanTokens() {
        while (!isEnd()) {
            start = current;
            scantToken();
        }
        tokens.add(new Token(EOF, "", null, line));
        return tokens;
    }

    //tells us if we’ve consumed all the characters
    public boolean isEnd() {
        return current >= source.length();
    }

    public void scantToken() {
        char c = advance();
        switch (c) {
            case '(':
                addToken(LEFT_PAR);
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

    //It’s sort of like advance(), but doesn’t consume the character
    private char peek() {
        if (isEnd()) return '\0';
        return source.charAt(current);
    }

    private void string() {
        while (peek() != '"' && !isEnd()) {
            if (peek() == '\n') line++;
            advance();
        }
        if (isEnd()) {
            Main.error(line, "Unterminated String");
        }
        advance();
        String lexem = source.substring(start + 1, current - 1);
        addToken(STRING, lexem);
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

    private char peekNext() {
        if (current + 1 >= source.length()) return '\0';
        return source.charAt(current + 1);
    }

    private void identifier() {
        while (isAlphaNumeric(peek())) {
            advance();
        }
        String lexem = source.substring(start, current);
        TokenType type = keywords.get(lexem);
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

    //TODO: adding nested block comments
    private void skipBlockComment() {
        while ((peek() != '*' && peekNext() != '/') && !isEnd()) {
            if (peek() == '\n') {
                line++;
            }
            advance();
        }
        if (isEnd()) {
            Main.error(line, "Unterminated block comment.");
        } else {
            advance();
            advance(); //To consume */
        }
    }

    public void addToken(TokenType type) {
        addToken(type, null);
    }

    public void addToken(TokenType type, Object literal) {
        String lexem = source.substring(start, current);
        tokens.add(new Token(type, lexem, literal, line));
    }

}
