package com.demointerpreter.tool;


import com.demointerpreter.grammar.Expression;
import com.demointerpreter.lexical_analyzer.TokenType;

/**
 * Pretty Printer
 * given a syntax tree, it produces an unambiguous string representation of it
 */
public class AstPrinter implements Expression.Visitor<String> {

    /*public static void main(String[] args) {
        var expression = new Expression.Binary(
                new Expression.Binary(
                        new Expression.Literal(3),
                        new Token(TokenType.PLUS, "+", null, 1),
                        new Expression.Literal(1)
                ),
                new Token(TokenType.STAR, "*", null, 1),
                new Expression.Unary(
                        new Token(TokenType.MINUS, "-", null, 1),
                        new Expression.Literal(5)));

        System.out.println(new AstPrinter().print(expression));
    }
*/
    public String print(Expression expression) {
        return expression.accept(this);
    }

    @Override
    public String visitBinaryExpression(Expression.Binary expression) {
        return parenthesize(expression.operator.getText(), expression.left, expression.right);
    }

    @Override
    public String visitGroupingExpression(Expression.Grouping expression) {
        return parenthesize("group", expression.expression);
    }

    @Override
    public String visitLiteralExpression(Expression.Literal expression) {
        return expression.value == null ? "nil" : expression.value.toString();
    }

    @Override
    public String visitUnaryExpression(Expression.Unary expression) {
        return parenthesize(expression.operator.getText(), expression.right);
    }

    @Override
    public String visitLogicalExpression(Expression.Logical expression) {
        return parenthesize(expression.operator.getText(), expression.left, expression.right);
    }

    @Override
    public String visitAssignExpression(Expression.Assign expression) {
        return parenthesize(TokenType.EQ.toString() + expression.name.getText(), expression.value);
    }

    @Override
    public String visitThisExpression(Expression.This expression) {
        return expression.keyword.getText();
    }

    private String parenthesize(String name, Expression... expressions) {
        var builder = new StringBuilder();
        builder.append("(").append(name);
        for (var expression : expressions) {
            builder.append(" ");
            builder.append(expression.accept(this));
        }
        builder.append(")");
        return builder.toString();
    }
}
