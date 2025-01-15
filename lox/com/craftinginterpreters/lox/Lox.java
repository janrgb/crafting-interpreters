package com.craftinginterpreters.lox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Lox {
    static boolean hadError = false;
    public static void main(String[] args) throws IOException {
        /* This is how we use the `jlox` command. */
        if (args.length > 1) {
            System.out.println("Usage: jlox [script]");
            System.exit(64);
        } else if (args.length == 1) {
            runFile(args[0]);
        } else {
            runPrompt();
        }
    }

    /* Run the FILE. */
    private static void runFile(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        run(new String(bytes, Charset.defaultCharset()));
        if (hadError) System.exit(65);
    }

    /* Run the REPL. */
    private static void runPrompt() throws IOException {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);

        /* Drop the user into an infinite REPL. */
        for (;;) {
            System.out.print("> ");
            String line = reader.readLine();
            if (line == null) break;
            run(line);
            hadError = false;
        }
    }

    /* Error handling. */
    static void error (int line, String message) {
        report(line, "", message);
    }

    private static void report (int line, String where, String message) {
        System.err.println(
            "[line " + line + "] Error" + where + ": " + message
        );
        hadError = true;
    }

    /* Parse error handling. */
    static void error (Token token, String message) {
        if (token.type == TokenType.EOF) {
            report(token.line, " at end", message);
        } else {
            report(token.line, " at '" + token.lexeme + "'", message);
        }
    }

    /* Our ACTUAL entrypoint. */
    private static void run (String source) {
        Scanner scanner = new Scanner(source);
        List<Token> tokens = scanner.scanTokens();

        /* Just print the tokens for now.
        for (Token token : tokens) {
            System.out.println(token);
        } */

        Parser parser = new Parser(tokens);
        Expr expression = parser.parse();

        /* Stop if there's a syntax error. */
        if (hadError) return;
        System.out.println("Printing...");
        System.out.println(new AstPrinter().print(expression));
    }
}
