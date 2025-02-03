package mouse.univ.app;

import mouse.univ.common.Messages;
import mouse.univ.common.Numbers;
import mouse.univ.geometry.GenericLine;
import mouse.univ.geometry.InvalidLineException;
import mouse.univ.geometry.Point;

import java.util.List;

public class IntersectionCalculator {
    private final UserIO userIO;
    public IntersectionCalculator(UserIO userIO) {
        this.userIO = userIO;
    }
    public String calculate() {
        boolean restarted;
        String result = null;
        do {
            try {
                result = calculateOrRestart();
                restarted = false;
            } catch (RestartException r) {
                restarted = true;
            }
        } while (restarted);
        return result;
    }

    private String calculateOrRestart() {
        List<GenericLine> arguments;
        try {
            arguments = defineArguments();
        } catch (TerminatedException e) {
            return Messages.terminated();
        }
        String result = getIntersections(arguments.get(0), arguments.get(1), arguments.get(2));
        userIO.println(result);
        return result;
    }

    private List<GenericLine> defineArguments() {
        GenericLine line1 = null;
        GenericLine line2 = null;
        GenericLine line3 = null;
        userIO.println("Визначіть ПРЯМУ 1 за двома точками (X1; Y1), (X2; Y2)");
        while (line1 == null) {
            try {
                LineByPointArgs line1Args = getLine1Arguments();
                line1 = line1Args.toLine();
            } catch (InvalidLineException e) {
                onInvalidLineEntered(e.getMessage());
            }
        }
        userIO.println("Визначіть ПРЯМУ 2 за двома відрізкамм. A1 - перетин з віссю OX, B1 - перетин з віссю OY");
        LineBySegmentsArgs line2Args = null;
        while (line2 == null) {
            try {
                line2Args = getLine2Arguments();
                line2 = line2Args.toLine();
            } catch (InvalidLineException e) {
                onInvalidLineEntered(e.getMessage());
            }
        }
        userIO.println("Визначіть ПРЯМУ 3 за двома відрізками. A2 - перетин з віссю OX, B2 - перетин з віссю OY");
        while (line3 == null) {
            try {
                LineBySegmentsArgs line3Args = getLine3Arguments();
                if (line3Args.equals(line2Args)) {
                    onInvalidLineEntered("ПРЯМА 3 не може співпадати з ПРЯМОЮ 2.");
                    continue;
                }
                line3 = line3Args.toLine();
            } catch (InvalidLineException e) {
                onInvalidLineEntered(e.getMessage());
            }
        }
        return List.of(line1, line2, line3);
    }

    private record LineByPointArgs(int x1, int y1, int x2, int y2) {
        GenericLine toLine() throws InvalidLineException {
            return GenericLine.fromTwoPoints(new Point(x1, y1), new Point(x2, y2));
        }
    }
    private record LineBySegmentsArgs(int a, int b) {
        GenericLine toLine() throws InvalidLineException {
            return GenericLine.fromTwoSegments(a, b);
        }
    }
    private LineByPointArgs getLine1Arguments() {
        int x1 = provideRangedIntOrTerminate("X1");
        int y1 = provideRangedIntOrTerminate("Y1");
        int x2 = provideRangedIntOrTerminate("X2");
        int y2 = provideRangedIntOrTerminate("Y2");
        return new LineByPointArgs(x1, y1, x2, y2);
    }
    private LineBySegmentsArgs getLine2Arguments() {
        int a = provideRangedIntOrTerminate("A1");
        int b = provideRangedIntOrTerminate("B1");
        return new LineBySegmentsArgs(a, b);
    }
    private LineBySegmentsArgs getLine3Arguments() {
        int a = provideRangedIntOrTerminate("A2");
        int b = provideRangedIntOrTerminate("B2");
        return new LineBySegmentsArgs(a, b);
    }
    private void onInvalidLineEntered(String message) {
        userIO.println(message);
        userIO.println("Введіть параметри прямої ще раз, будь ласка.");
    }

    private int provideRangedIntOrTerminate(String prompt) {
        String howToFix = String.format(" Введіть ціле десяткове число з проміжу [-%d; %d], будь ласка.", Numbers.BOX_SIZE, Numbers.BOX_SIZE);
        while (true) {
            String string = userIO.getString(prompt);
            if (string.isEmpty()) {
                userIO.println("Відсутнє вхідне значення;" + howToFix);
                continue;
            }
            String asCommand = string.trim().toLowerCase();
            if (asCommand.equals("e") || asCommand.equals("е")) {
                userIO.println("Зупинення...");
                throw new TerminatedException();
            }
            if (asCommand.equals("r") || asCommand.equals("к")) {
                userIO.println("Відновлення...");
                throw new RestartException();
            }
            try {
                int result = Integer.parseInt(string);
                if (Numbers.isOutOfRange(result)) {
                    String err = String.format("Дане вхідне число не входить до проміжку [-%d, %d];", Numbers.BOX_SIZE, Numbers.BOX_SIZE) + howToFix;
                    userIO.println(err);
                } else {
                    return result;
                }
            } catch (Exception e) {
                userIO.println("Вхідне значення не є цілим числом;" + howToFix);
            }
        }
    }

    private String getIntersections(GenericLine line1, GenericLine line2, GenericLine line3) {
        LineIntersectionResult lines1_2Pos = checkLinesIntersect(line1, line2);
        LineIntersectionResult lines1_3Pos = checkLinesIntersect(line1, line3);
        if (lines1_2Pos.isSameLine()) {
            if (lines1_3Pos.isParallelLines()) {
                return Messages.parallel();
            } else {
                return Messages.intersections(List.of(lines1_3Pos.intersection()));
            }
        } else if (lines1_2Pos.isParallelLines()) {
            if (lines1_3Pos.isParallelLines() || lines1_3Pos.isSameLine()) {
                return Messages.parallel();
            }
            LineIntersectionResult lines2_3Pos = checkLinesIntersect(line2, line3);
            if (lines2_3Pos.isParallelLines()) {
                return Messages.parallel();
            }
            return Messages.intersections(List.of(lines1_3Pos.intersection(), lines2_3Pos.intersection()));
        } else if (lines1_2Pos.isIntersectingLines()) {
            LineIntersectionResult lines2_3Pos = checkLinesIntersect(line2, line3);
            if (lines1_3Pos.isSameLine()) {
                return Messages.intersections(List.of(lines1_2Pos.intersection()));
            }
            if (lines1_3Pos.isParallelLines()) {
                return Messages.intersections(List.of(lines1_2Pos.intersection(), lines2_3Pos.intersection()));
            }
            if (lines2_3Pos.isParallelLines()) {
                return Messages.intersections(List.of(lines1_2Pos.intersection(), lines1_3Pos.intersection()));
            }
            Point point1 = lines1_2Pos.intersection();
            Point point2 = lines1_3Pos.intersection();
            Point point3 = lines2_3Pos.intersection();
            if (point1.isCloseTo(point2)) {
                return Messages.intersections(List.of(point1));
            }
            return Messages.intersections(List.of(point1, point2, point3));
        } else {
            throw new IllegalStateException("Неможливо встановити взаємне положення прямих ПРЯМА 1 та ПРЯМА 2.");
        }
    }
    private enum LinesRelativePosition {
        SAME_LINE, PARALLEL, INTERSECT
    }

    private record LineIntersectionResult(LinesRelativePosition relative, Point intersection) {
        public Point intersection() {
            if (relative.equals(LinesRelativePosition.INTERSECT) && intersection == null) {
                throw new IllegalStateException("Положення прямих позначено як INTERSECT, але не задано точки перетину.");
            }
            if (relative.equals(LinesRelativePosition.PARALLEL) || relative.equals(LinesRelativePosition.SAME_LINE)) {
                throw new IllegalStateException("Неможливо отримати точку перетину для прямих, що не перетинаються.");
            }
            return intersection;
        }

        public boolean isSameLine() {
            return relative.equals(LinesRelativePosition.SAME_LINE);
        }
        public boolean isParallelLines() {
            return relative.equals(LinesRelativePosition.PARALLEL);
        }
        public boolean isIntersectingLines() {
            return relative.equals(LinesRelativePosition.INTERSECT);
        }
    }
    private LineIntersectionResult checkLinesIntersect(GenericLine line1, GenericLine line2) {
        double denominator = line1.a() * line2.b() - line2.a() * line1.b();
        if (Numbers.isZero(denominator)) {
            return processZeroDenominatorCase(line1, line2);
        }
        double intersectionX = (line2.c() * line1.b() - line1.c() * line2.b()) / denominator;
        double intersectionY = (line2.a() * line1.c() - line1.a() * line2.c()) / denominator;
        return new LineIntersectionResult(LinesRelativePosition.INTERSECT, new Point(intersectionX, intersectionY));
    }

    private LineIntersectionResult processZeroDenominatorCase(GenericLine line1, GenericLine line2) {
        boolean unmatchedZero = false;
        if (Numbers.isZero(line1.a()) && !Numbers.isZero(line2.a())) {
            unmatchedZero = true;
        }
        if (Numbers.isZero(line1.b()) && !Numbers.isZero(line2.b())) {
            unmatchedZero = true;
        }
        if (Numbers.isZero(line1.c()) && !Numbers.isZero(line2.c())) {
            unmatchedZero = true;
        }
        if (unmatchedZero) {
            return new LineIntersectionResult(LinesRelativePosition.PARALLEL, null);
        }
        double aRatio = line1.a() / line2.a();
        double bRatio = line1.b() / line2.b();
        double cRatio = line1.c() / line2.c();
        boolean isSameLine = Numbers.equals(aRatio, bRatio) && Numbers.equals(bRatio, cRatio);
        return isSameLine
                ? new LineIntersectionResult(LinesRelativePosition.SAME_LINE, null)
                : new LineIntersectionResult(LinesRelativePosition.PARALLEL, null);
    }

}
