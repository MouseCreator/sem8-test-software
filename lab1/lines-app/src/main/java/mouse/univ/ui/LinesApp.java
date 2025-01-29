package mouse.univ.ui;

import mouse.univ.geometry.IntersectionCalculator;

import java.util.Scanner;

public class LinesApp {
    public static void main(String[] args) {
        ConsoleUserInput consoleUserInput = new ConsoleUserInput(System.out, new Scanner(System.in));
        UIEngine uiEngine = new UIEngine(consoleUserInput, new IntersectionCalculator());
        uiEngine.start();
    }
}
