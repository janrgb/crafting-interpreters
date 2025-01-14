package com.craftinginterpreters.lox;

import static com.craftinginterpreters.lox.TokenType.LEFT_BRACE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.craftinginterpreters.lox.TokenType.*;

public class Scanner {
    private final String source;
    private final List<Token> tokens = new ArrayList<>();
    private int start = 0;
    private int current = 0;
    private int line = 1;

    Scanner(String source) {
        this.source = source;
    }

    List<Token> scanTokens() {
        while (!isAtEnd()) {
            /* We are at the beginning of the next lexeme. */
            start = current;
            scanToken();
        }

        tokens.add(new Token(EOF, "", null, line));
        return tokens;
    }

    private void scanToken() {
        char c = advance();
        switch (c) {
            case '(': addToken(TokenType.LEFT_PAREN); break;
            case ')': addToken(TokenType.RIGHT_PAREN); break;
            case '{': addToken(TokenType.LEFT_BRACE); break;
            case '}': addToken(TokenType.RIGHT_BRACE); break;
            case ',': addToken(TokenType.COMMA); break;
            case '.': addToken(TokenType.DOT); break;
            case '-': addToken(TokenType.MINUS); break;
            case '+': addToken(TokenType.PLUS); break;
            case '*': addToken(TokenType.STAR); break;
            case ';': addToken(TokenType.SEMICOLON); break;
            
            case '!':
                addToken(match('=') ? TokenType.BANG_EQUAL : TokenType.BANG);
                break;
            case '=':
                addToken(match('=') ? TokenType.EQUAL_EQUAL : TokenType.EQUAL);
                break;
            case '<':
                addToken(match('=') ? TokenType.LESS_EQUAL : TokenType.LESS);
                break;
            case '>':
                addToken(match('=') ? TokenType.GREATER_EQUAL : TokenType.GREATER);
                break;

            /* Semi-Meaningless Characters. */
            case '/':
                if (match('/')) {
                    /* A comment goes until the enf of the line. */
                    while (peek() != '\n' && !isAtEnd()) advance();
                } else {
                    addToken(TokenType.SLASH);
                }
                break;
            case ' ':
            case '\r':
            case '\t':
                break;
            case '\n':
                line++;
                break;

            /* The literals. */
            case '"': string(); break;

            /* Let's make sure we're sending errors where we see them. */
            /* We've also relegated digit finding to the default case. */
            default:
                if (isDigit(c)) {
                    number();
                } else {
                    Lox.error(line, "Unexpected character.");
                }
                break;
        }
    }

    /* Method for dealing with number literals. */
    private void number() {
        while (isDigit(peek())) advance();

        /* Look out for fractional part. */
        if (peek() == '.' && isDigit(peekNext())) {
            /* Consume the "." */
            advance();

            while (isDigit(peek())) advance();
        }

        addToken(TokenType.NUMBER,
            Double.parseDouble(source.substring(start, current)));
    }

    /* Method for dealing with string literals. */
    private void string() {
        while (peek() != '"' && !isAtEnd()) {
            if (peek() == '\n') line++;
            advance();
        }

        if (isAtEnd()) {
            Lox.error(line, "Unterminated string.");
            return;
        }

        /* The closing ". */
        advance();

        /* Trim the surrounding quotes. */
        String value = source.substring(start + 1, current - 1);
        addToken(TokenType.STRING, value);
    }

    /* Method for double-char whammies. */
    private boolean match (char expected) {
        if (isAtEnd()) return false;
        if (source.charAt(current) != expected) return false;
        
        current++;
        return true;
    }

    /* Method for advancing but not consuming. Called...lookahead. */
    private char peek() {
        if (isAtEnd()) return '\0';
        return source.charAt(current);
    }

    /* Lookahead +1. */
    private char peekNext() {
        if (current + 1  >= source.length()) return '\0';
        return source.charAt(current + 1);
    }

    /* Digit utility. */
    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    /* Helper. */
    private boolean isAtEnd() {
        return current >= source.length();
    }

    /* For input - advance to next character for reading... */
    private char advance() {
        return source.charAt(current++);
    }

    /* addToken overload 1 - for non-literals. */
    private void addToken(TokenType type) {
        addToken(type, null);
    }

    /* addToken overload 2 - for literals. */
    private void addToken(TokenType type, Object literal) {
        String text = source.substring(start, current);
        tokens.add(new Token(type, text, literal, line));
    }
}
