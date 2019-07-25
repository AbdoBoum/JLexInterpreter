package com.demointerpreter.interpreter;

import com.demointerpreter.Main;
import com.demointerpreter.grammar.Expression;
import com.demointerpreter.grammar.Statement;
import com.demointerpreter.lexical_analyzer.Token;
import com.demointerpreter.lexical_analyzer.TokenType;

import java.util.List;

public class Interpreter implements Expression.Visitor<Object>, Statement.Visitor<Void> {

    private static Object uninitialized = new Object();

    private Envirenment envirenment = new Envirenment();

    public void interpret(List<Statement> statements) {
        try {
            for (Statement statement: statements) {
                execute(statement);
            }
        }catch (RuntimeError error) {
            Main.runTimeError(error);
        }
    }

    private void execute(Statement statement) {
        statement.accept(this);
    }

    @Override
    public Void visitExpressionStatement(Statement.Expression statement) {
        evaluate(statement.expression);
        return null;
    }

    @Override
    public Void visitPrintStatement(Statement.Print statement) {
        Object value = evaluate(statement.expression);
        System.out.println(stringify(value));
        return null;
    }

    @Override
    public Void visitVarStatement(Statement.Var statement) {
        Object value = uninitialized;
        if (statement.initializer != null) {
            value = evaluate(statement.initializer);
        }
        envirenment.define(statement.name.getText(), value, statement.name.getLine());
        return null;
    }

    @Override
    public Void visitBlockStatement(Statement.Block statement) {
        executeBlock(statement.statements, new Envirenment(envirenment));
        return null;
    }

    @Override
    public Void visitIfStatement(Statement.If statement) {
        if (isTruthy(evaluate(statement.condition))) {
            execute(statement.thenBranch);
        } else if (statement.elseBranch != null){
            execute(statement.elseBranch);
        }
        return null;
    }

    @Override
    public Void visitWhileStatement(Statement.While statement) {
        while (isTruthy(evaluate(statement.condition))) {
            execute(statement.statement);
        }
        return null;
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
                } else if ((left instanceof String) || (right instanceof String)) {
                    return stringify(left) + stringify(right);
                }
                //throw new RuntimeError(expression.operator, "Operands must be two numbers or two strings.");
            case STAR:
                checkNumberOperands(expression.operator, left, right);
                return (double) left * (double) right;
            case SLASH:
                checkNumberOperands(expression.operator, left, right);
                if (stringify(right).equals(String.valueOf(0))) {
                    throw new RuntimeError(expression.operator, "ArithmeticException: / by zero");
                }
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
        Object left = evaluate(expression.left);
        if (expression.operator.getType() == TokenType.OR) {
            if (isTruthy(left)) return left;
        } else if (expression.operator.getType() == TokenType.AND) {
            if (!isTruthy(left)) return left;
        }
        return evaluate(expression.right);
    }

    @Override
    public Object visitAssignExpression(Expression.Assign expression) {
        Object value = evaluate(expression.value);
        envirenment.assign(expression.name, value);
        return value;
    }

    @Override
    public Object visitThisExpression(Expression.This expression) {
        return null;
    }

    @Override
    public Object visitVariableExpression(Expression.Variable expression) {
        Object value = envirenment.get(expression.name);
        if (value == uninitialized) {
            throw new RuntimeError(expression.name, "Variable must be initialized before use.");
        }
        return value;
    }

    private void executeBlock(List<Statement> statements, Envirenment envirenment) {
        Envirenment previous = this.envirenment;
        try {
            this.envirenment = envirenment;
            for (Statement statement: statements) {
                execute(statement);
            }
        } finally {
            this.envirenment = previous;
        }
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
