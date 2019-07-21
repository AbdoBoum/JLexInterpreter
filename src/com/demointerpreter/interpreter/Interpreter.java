package com.demointerpreter.interpreter;

import com.demointerpreter.Main;
import com.demointerpreter.grammar.Expression;
import com.demointerpreter.lexical_analyzer.Token;

public class Interpreter implements Expression.Visitor<Object> {

    public void interpret(Expression expression) {
        try {
            Object value = evaluate(expression);
            System.out.println(stringify(value));
        }catch (RuntimeError error) {
            Main.runTimeError(error);
        }
    }

    @Override
    public Object visitBinaryExpression(Expression.Binary expression) {
        Object left = evaluate(expression.left);
        Object right = evaluate(expression.right);
        switch (expression.operator.getType()) {
            case EQ_EQ:
                return isEqual(left, right);
            case BANG_EQ:
                return !isEqual(left, right);
            case GREATER:
                checkNumberOperands(expression.operator, left, right);
                return (double) left > (double) right;
            case GREATER_EQ:
                checkNumberOperands(expression.operator, left, right);
                return (double) left >= (double) right;
            case LESS:
                checkNumberOperands(expression.operator, left, right);
                return (double) left < (double) right;
            case LESS_EQ:
                checkNumberOperands(expression.operator, left, right);
                return (double) left <= (double) right;
            case MINUS:
                checkNumberOperands(expression.operator, left, right);
                return (double) left - (double) right;
            case PLUS:
                if ((left instanceof Double) && (right instanceof Double)) {
                    return (double) left + (double) right;
                } else if ((left instanceof String) && (right instanceof String)) {
                    return String.valueOf(left) + String.valueOf(right);
                }
                throw new RuntimeError(expression.operator,
                        "Operands must be two numbers or two strings.");
            case STAR:
                checkNumberOperands(expression.operator, left, right);
                return (double) left * (double) right;
            case SLASH:
                checkNumberOperands(expression.operator, left, right);
                return (double) left / (double) right;
        }
        return null;
    }

    @Override
    public Object visitGroupingExpression(Expression.Grouping expression) {
        return evaluate(expression.expression);
    }

    @Override
    public Object visitLiteralExpression(Expression.Literal expression) {
        return expression.value;
    }

    @Override
    public Object visitUnaryExpression(Expression.Unary expression) {
        Object right = evaluate(expression.right);
        switch (expression.operator.getType()) {
            case MINUS:
                checkNumberOperand(expression.operator, right);
                return -(double) right;
            case BANG:
                return !isTruthy(right);
        }
        return null;
    }

    @Override
    public Object visitLogicalExpression(Expression.Logical expression) {
        return null;
    }

    @Override
    public Object visitAssignExpression(Expression.Assign expression) {
        return null;
    }

    @Override
    public Object visitThisExpression(Expression.This expression) {
        return null;
    }

    private Object evaluate(Expression expression) {
        return expression.accept(this);
    }

    private boolean isEqual(Object a, Object b) {
        // we don’t throw a NullPointerException if we try to call equals()
        if (a == null && b == null) return true;
        if (a == null) return false;
        return a.equals(b);
    }

    private boolean isTruthy(Object object) {
        if (object == null) return false;
        if (object instanceof Boolean) return (boolean) object;
        return true;
    }

    private void checkNumberOperand(Token operator, Object operand) {
        if (operand instanceof Double) return;
        throw new RuntimeError(operator, "Operand must be a number.");
    }

    private void checkNumberOperands(Token operator, Object left, Object right) {
        if (left instanceof Double && right instanceof Double) return;
        throw new RuntimeError(operator, "Operands must be numbers.");
    }

    private String stringify (Object object) {
        if (object == null) return "nil";
        if (object instanceof Double) {
            String text = String.valueOf(object);
            if (text.endsWith(".0")) {
                text = text.substring(0,text.length() - 2);
            }
            return text;
        }
        return String.valueOf(object);
    }
}
