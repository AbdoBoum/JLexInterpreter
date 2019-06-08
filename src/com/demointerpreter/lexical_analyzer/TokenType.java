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
                text = "LEFT_BRACE";
                break;
            case RIGHT_BRACE:
                text = "RIGHT_BRACE";
                break;
            case LEFT_PAR:
                text = "LEFT_PAR";
                break;
            case RIGHT_PAR:
                text = "RIGHT_PAR";
                break;
            case EQ:
                text = "=";
                break;
            case EQ_EQ:
                text = "==";
                break;
            case COMMA:
                text = "COMMA";
                break;
            case DOT:
                text = "DOT";
                break;
            case SEMICOLON:
                text = "SEMICOLON";
                break;
            case PLUS:
                text = "PLUS";
                break;
            case MINUS:
                text = "MINUS";
                break;
            case SLASH:
                text = "SLASH";
                break;
            case IF:
                text = "IF";
                break;
            case AND:
                text = "AND";
                break;
            case OR:
                text = "OR";
                break;
            case EOF:
                text = "EOF";
                break;
            case FOR:
                text = "FOR";
                break;
            case FUN:
                text = "FUN";
                break;
            case IDF:
                text = "IDF";
                break;
            case NIL:
                text = "NIL";
                break;
            case VAR:
                text = "VAR";
                break;
            case BANG:
                text = "BANG";
                break;
            case ELSE:
                text = "ELSE";
                break;
            case LESS:
                text = "LESS";
                break;
            case STAR:
                text = "STAR";
                break;
            case THIS:
                text = "THIS";
                break;
            case TRUE:
                text = "TRUE";
                break;
            case CLASS:
                text = "CLASS";
                break;
            case FALSE:
                text = "FALSE";
                break;
            case PRINT:
                text = "PRINT";
                break;
            case SUPER:
                text = "SUPER";
                break;
            case WHILE:
                text = "while";
                break;
            case RETURN:
                text = "RETURN";
                break;
            case BANG_EQ:
                text = "BANG_EQ";
                break;
            case GREATER:
                text = "GREATER";
                break;
            case GREATER_EQ:
                text = "GREATER_EQ";
                break;
            case LESS_EQ:
                text = "LESS_EQ";
                break;
            case NUMBER:
                text = "NUMBER";
                break;
            case STRING:
                text = "STRING";
                break;
        }
        return text;
    }
}
