package com.craftinginterpreters.lox;

import java.util.List;

class Parser {
    private static class ParseError extends RuntimeException {}
    private final List<Token> tokens;
    private int current = 0;

    Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    Expr parse() {
        try {
            return expression();
        } catch (ParseError error) {
            System.out.println("Caught ParseError.");
            return null;
        }
    }

    private Expr expression() {
        System.out.println("Calling comma()");
        return comma();
    }

    /* Match a comma OR ANYTHING OF HIGHER PRECEDENCE. */
    private Expr comma() {
        System.out.println("Calling equality()");
        Expr expr = equality();

        while (match(TokenType.COMMA)) {
            Token operator = previous();
            Expr right = equality();
            expr = new Expr.Binary(expr, operator, right);
        }

        return expr;
    }

    /* Match an equality OR ANYTHING OF HIGHER PRECEDENCE. */
    private Expr equality() {
        System.out.println("Calling comparison()");
        Expr expr = comparison();

        /* Will not enter if we don't have a proper signature. */
        while (match(TokenType.BANG_EQUAL, TokenType.EQUAL_EQUAL)) {
            Token operator = previous();
            Expr right = comparison();
            expr = new Expr.Binary(expr, operator, right);
        }

        return expr;
    }

    /* Match a comparison OR ANYTHING OF HIGHER PRECEDENCE. */
    private Expr comparison() {
        System.out.println("Calling term()");
        Expr expr = term();

        /* Will not enter if we don't have a proper signature. */
        while (match(TokenType.GREATER, TokenType.GREATER_EQUAL, TokenType.LESS, TokenType.LESS_EQUAL)) {
            Token operator = previous();
            Expr right = term();
            expr = new Expr.Binary(expr, operator, right);
        }

        return expr;
    }

    /* Addition, Subtraction */
    private Expr term() {
        System.out.println("Calling factor()");
        Expr expr = factor();

        while (match(TokenType.MINUS, TokenType.PLUS)) {
            Token operator = previous();
            Expr right = factor();
            expr = new Expr.Binary(expr, operator, right);
        }

        return expr;
    }

    /* Multiplication, Division */
    private Expr factor() {
        System.out.println("Calling unary()");
        Expr expr = unary();

        while (match(TokenType.STAR, TokenType.SLASH)) {
            Token operator = previous();
            Expr right = unary();
            expr = new Expr.Binary(expr, operator, right);
        }

        return expr;
    }

    /* Recursively enumerate unary expressions until resolved by primary(). */
    private Expr unary() {
        if (match(TokenType.BANG, TokenType.MINUS)) {
            Token operator = previous();
            Expr right = unary();
            return new Expr.Unary(operator, right);
        }

        System.out.println("Calling primary()");
        return primary();
    }

    private Expr primary() {
        System.out.println("IN primary()");
        /* Explicit new Literals. */
        if (match(TokenType.FALSE)) return new Expr.Literal(false);
        if (match(TokenType.TRUE)) return new Expr.Literal(true);
        if (match(TokenType.NIL)) return new Expr.Literal(null);

        /* Any literal. */
        if (match(TokenType.NUMBER, TokenType.STRING)) {
            System.out.println("This is a NUMBER so it should be here?");
            return new Expr.Literal(previous().literal);
        }

        /* We must have a closing parenthetical after any parenthetical expression. */
        if (match(TokenType.LEFT_PAREN)) {
            Expr expr = expression();
            consume(TokenType.RIGHT_PAREN, "Expect ')' after expression.");
            return new Expr.Grouping(expr);
        }

        System.out.println("Let's throw an error...");
        throw error(peek(), "Expect expression.");
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

    private Token consume(TokenType type, String message) {
        if (check(type)) return advance();

        throw error(peek(), message);
    }

    private boolean check(TokenType type) {
        if (isAtEnd()) return false;
        return peek().type == type;
    }

    private Token advance() {
        if (!isAtEnd()) current++;
        return previous();
    }

    private boolean isAtEnd() {
        return peek().type == TokenType.EOF;
    }
    
    private Token peek() {
        return tokens.get(current);
    }

    private Token previous() {
        return tokens.get(current - 1);
    }

    private ParseError error(Token token, String message) {
        Lox.error(token, message);
        return new ParseError();
    }

    private void synchronize() {
        advance();

        while (!isAtEnd()) {
            if (previous().type == TokenType.SEMICOLON) return;

            switch (peek().type) {
                case TokenType.CLASS:
                case TokenType.FUN:
                case TokenType.VAR:
                case TokenType.FOR:
                case TokenType.IF:
                case TokenType.WHILE:
                case TokenType.PRINT:
                case TokenType.RETURN:
                    return;
            }

            advance();
        }
    }
}
