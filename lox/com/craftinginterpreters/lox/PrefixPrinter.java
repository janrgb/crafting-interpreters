package com.craftinginterpreters.lox;

import java.util.Stack;
import java.util.HashMap;
import java.util.Map;
class PrefixPrinter implements Expr.Visitor<String> {
    private String toRPN(String infixExpression) {
        Stack<Character> rpnStack = new Stack<Character>();
        Map<Character, Integer> precedenceMap = new HashMap<Character, Integer>();
        precedenceMap.put('(', 0);
        precedenceMap.put('+', 1);
        precedenceMap.put('-', 1);
        precedenceMap.put('*', 2);
        precedenceMap.put('/', 2);

        StringBuilder prefixExpression = new StringBuilder();

        /* Loop through the infix expression and build the string.  */
        for (char c : infixExpression.toCharArray()) {
            /* We just append non-operator values. */
            if (c != ')' && !precedenceMap.containsKey(c)) {
                prefixExpression.append(c);
                continue;
            }

            /* Conditionals for adding to stack. We must consider precedence. */
            if (c == ')') {
                while (!rpnStack.empty() && rpnStack.peek() != '(') {
                    prefixExpression.append(rpnStack.pop());
                }
                
                if (!rpnStack.empty()) rpnStack.pop();
            }
            else if (c == '(' || rpnStack.empty() || precedenceMap.get(c) > precedenceMap.get(rpnStack.peek())) {
                rpnStack.push(c);
            }
            else {
                while (!rpnStack.empty() && precedenceMap.get(c) < precedenceMap.get(rpnStack.peek())) {
                    prefixExpression.append(rpnStack.pop());
                }
                rpnStack.push(c);
            }
        }

        /* Dump the stack. */
        for (Character item : rpnStack) {
            prefixExpression.append(item);
        }

        /* Return the completed string. */
        return prefixExpression.toString();
    }
    
    public String print(Expr expr) {
        String rpnString = expr.accept(this);
        return toRPN(rpnString);        
    }
    
    @Override
    public String visitBinaryExpr(Expr.Binary expr) {
        return stringHelper(expr.left) + expr.operator.lexeme + stringHelper(expr.right);
    }

    @Override
    public String visitGroupingExpr(Expr.Grouping expr) {
        return "(" + stringHelper(expr.expression) + ")";
    }

    @Override
    public String visitLiteralExpr(Expr.Literal expr) {
        if (expr.value == null) return "";
        return expr.value.toString();
    }

    @Override
    public String visitUnaryExpr(Expr.Unary expr) {
        return expr.operator.lexeme + stringHelper(expr.right);
    }

    /* As opposed to Literals, the other expressions have subexpressions. */
    private String stringHelper(Expr expr) {
        return expr.accept(this);
    }

    public static void main(String[] args) {
        /*Expr expression = new Expr.Binary(
            new Expr.Grouping(
                new Expr.Binary(
                    new Expr.Literal(1),
                    new Token(TokenType.PLUS, "+", null, 1),
                    new Expr.Literal(2)
                )
            ),
            new Token(TokenType.STAR, "*", null, 1),
            new Expr.Grouping(
                new Expr.Binary(
                    new Expr.Literal(4),
                    new Token(TokenType.MINUS, "-", null, 1),
                    new Expr.Literal(3)
                )
            )
        );*/
        Expr expression = new Expr.Binary(
            new Expr.Unary(
                new Token(TokenType.MINUS, "-", null, 1),
                new Expr.Literal(123)
            ),
            new Token(TokenType.STAR, "*", null, 1),
            new Expr.Grouping(
                new Expr.Literal(45.67)
            )
        );

        System.out.println(new PrefixPrinter().print(expression));
    }
}
