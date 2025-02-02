package mouse.univ.app;

import mouse.univ.common.Messages;

import java.util.Scanner;

public class LinesApp {
    public static void main(String[] args) {
        ConsoleUserIO consoleUserInput = new ConsoleUserIO(System.out, new Scanner(System.in));
        IntersectionCalculator intersectionCalculator = new IntersectionCalculator(consoleUserInput);
        printHelp(consoleUserInput);
        while (true) {
            try {
                String result = intersectionCalculator.calculate();
                if (result.equals(Messages.terminated())) {
                    break;
                }
            } catch (Exception e) {
                consoleUserInput.println("ERROR: " + e.getMessage());
            }
        }
    }

    private static void printHelp(UserIO userIO) {
        String help = """
            === КАЛЬКУЛЯТОР ПЕРЕТИНІВ ПРЯМИХ ===
            * Дана програма обраховує точки перетину трьох заданих прямих;
            * На кожен запит введіть ціле число з проміжку [-122, 122];
            * Якщо введено некоректне значення, Вас попросять ввести нове значення замість попереднього;
            * Ви можете ввести значення 'e', щоб зупинити виконання програмни;
            * Ви можете ввести значення 'r', щоб почати заново виконання програми.
            ====================================
            """;
        userIO.println(help);
    }
}
