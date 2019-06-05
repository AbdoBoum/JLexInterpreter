package com.demointerpreter.lexical_analyzer;

public enum TokenType {
    LEFT_PAR, RIGHT_PAR, LEFT_BRACE, RIGHT_BRACE, COMMA,
    DOT, SEMICOLON, MINUS, PLUS, SLASH, STAR, GREATER,
    GREATER_EQ, LESS, LESS_EQ, BANG, BANG_EQ, EQ, EQ_EQ,

    //Literal
    IDF, STRING, NUMBER,

    // Reserved Words
    CLASS, AND, OR, IF, ELSE, FALSE, TRUE, VAR, FOR, NIL,
    WHILE, PRINT, RETURN, SUPER, THIS, EOF, FUN;

    @Override
    public String toString() {
        String text = "";
        switch (this) {
            case LEFT_BRACE:
                text = "{";
                break;
            case RIGHT_BRACE:
                text = "}";
                break;
            case LEFT_PAR:
                text = "(";
                break;
            case RIGHT_PAR:
                text = ")";
                break;
            case EQ:
                text = "=";
                break;
            case EQ_EQ:
                text = "==";
                break;
            case COMMA:
                text = ",";
                break;
            case DOT:
                text = ".";
                break;
            case SEMICOLON:
                text = ";";
                break;
            case PLUS:
                text = "+";
                break;
            case MINUS:
                text = "-";
                break;
            case SLASH:
                text = "/";
                break;
            case IF:
                text = "if";
                break;
            case AND:
                text = "and";
                break;
            case OR:
                text = "or";
                break;
            case EOF:
                text = "EOF";
                break;
            case FOR:
                text = "for";
                break;
            case FUN:
                text = "fun";
                break;
            case IDF:
                text = "idf";
                break;
            case NIL:
                text = "nil";
                break;
            case VAR:
                text = "var";
                break;
            case BANG:
                text = "!";
                break;
            case ELSE:
                text = "else";
                break;
            case LESS:
                text = "<";
                break;
            case STAR:
                text = "*";
                break;
            case THIS:
                text = "this";
                break;
            case TRUE:
                text = "true";
                break;
            case CLASS:
                text = "class";
                break;
            case FALSE:
                text = "false";
                break;
            case PRINT:
                text = "print";
                break;
            case SUPER:
                text = "super";
                break;
            case WHILE:
                text = "while";
                break;
            case RETURN:
                text = "return";
                break;
            case BANG_EQ:
                text = "!=";
                break;
            case GREATER:
                text = ">";
                break;
            case GREATER_EQ:
                text = ">=";
                break;
            case LESS_EQ:
                text = "<=";
                break;
            case NUMBER:
                text = "number";
                break;
            case STRING:
                text = "string";
                break;
        }
        return text;
    }
}
