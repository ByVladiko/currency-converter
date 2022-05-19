package com.company;

import com.company.exception.CalculateExpressionException;
import com.company.expression.ExpressionResolver;

import javax.xml.parsers.ParserConfigurationException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
        System.setErr(new PrintStream(System.err, true, StandardCharsets.UTF_8));
        ExpressionResolver expressionResolver = new ExpressionResolver();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Input your expression");
            String input = scanner.nextLine();
            System.out.println();

            if (input.isEmpty())
                return;

            try {
                System.out.println(expressionResolver.resolve(input));
            } catch (CalculateExpressionException | ParserConfigurationException e) {
                System.err.println(e.getMessage());
            }

            System.out.println();
        }
    }
}
