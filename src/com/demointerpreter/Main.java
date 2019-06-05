package com.demointerpreter;

import com.demointerpreter.lexical_analyzer.Scanner;
import com.demointerpreter.lexical_analyzer.Token;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Main {

    private static boolean hadError = false;

    public static void main(String[] args) throws IOException {
        if (args.length > 1) {
            System.out.println("Usage: jlox [script]");
            System.exit(64);
        } else if (args.length == 1) {
            runFile(args[0]);
        } else {
            runConsole();
        }
    }

    private static void runFile(String arg) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(arg));
        run(new String(bytes, Charset.defaultCharset()));
    }

    private static void runConsole() throws IOException {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);
        for (;;) {
            System.out.print("> ");
            run(reader.readLine()) ;
            hadError = false;
        }
    }

    private static void run(String src) {
        if (hadError) {
            System.exit(65);
        }
        Scanner scanner = new Scanner(src);
        List<Token> tokens = scanner.scanTokens();
        for (Token token: tokens) {
            System.out.println(token.toString());
        }
    }

    public static void error(int line, String message) {
        report(line, "", message);
    }

    private static void report(int line, String where, String message) {
        System.err.println(
                "[line " + line + "] Error" + where + ": " + message);
        hadError = true;
    }

}
