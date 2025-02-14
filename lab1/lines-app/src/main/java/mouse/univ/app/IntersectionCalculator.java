package mouse.univ.app;

import mouse.univ.common.Messages;
import mouse.univ.common.Numbers;
import mouse.univ.geometry.InputArgs;
import mouse.univ.geometry.InvalidLineException;
import mouse.univ.geometry.Point;

import java.util.List;

/**
 * Calculates intersections of three given lines
 * Handles reading input parameters, calculation of the intersection points and printing the results
 */
public class IntersectionCalculator {
    private final UserIO userIO;

    /**
     * Creates new instance of IntersectionCalculator with given communication channel
     * @param userIO - communication channel
     */
    public IntersectionCalculator(UserIO userIO) {
        this.userIO = userIO;
    }

    /**
     * Starts the parameter retrieving and calculation process
     * @return result of the last calculation
     */
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
        InputArgs arguments;
        try {
            arguments = defineArguments();
        } catch (TerminatedException e) {
            return Messages.terminated();
        }
        String result = new IntersectionDeterminer(arguments).getIntersections();
        userIO.println(result);
        return result;
    }

    /**
     *
     * @return list of three generic lines, defined by the provided arguments from User IO
     */
    private InputArgs defineArguments() {
        userIO.println("Визначіть ПРЯМУ 1 за двома точками (X1; Y1), (X2; Y2)");
        LineByPointArgs line1Args;
        while (true) {
            try {
                line1Args = getLine1Arguments();
                line1Args.validate();
                break;
            } catch (InvalidLineException e) {
                onInvalidLineEntered(e.getMessage());
            }
        }
        userIO.println("Визначіть ПРЯМУ 2 за двома відрізкамм. A1 - перетин з віссю OX, B1 - перетин з віссю OY");
        LineBySegmentsArgs line2Args;
        while (true) {
            try {
                line2Args = getLine2Arguments();
                line2Args.validate();
                break;
            } catch (InvalidLineException e) {
                onInvalidLineEntered(e.getMessage());
            }
        }
        userIO.println("Визначіть ПРЯМУ 3 за двома відрізками. A2 - перетин з віссю OX, B2 - перетин з віссю OY");
        LineBySegmentsArgs line3Args;
        while (true) {
            try {
                line3Args = getLine3Arguments();
                if (line3Args.equals(line2Args)) {
                    onInvalidLineEntered("ПРЯМА 3 не може співпадати з ПРЯМОЮ 2.");
                    continue;
                }
                line3Args.validate();
                break;
            } catch (InvalidLineException e) {
                onInvalidLineEntered(e.getMessage());
            }
        }

        return toInputArguments(line1Args, line2Args, line3Args);
    }

    private InputArgs toInputArguments(LineByPointArgs line1Args, LineBySegmentsArgs line2Args, LineBySegmentsArgs line3Args) {
        InputArgs inputArgs = new InputArgs();
        inputArgs.setX1(line1Args.x1());
        inputArgs.setY1(line1Args.y1());
        inputArgs.setX2(line1Args.x2());
        inputArgs.setY2(line1Args.y2());
        inputArgs.setA2(line2Args.a());
        inputArgs.setB2(line2Args.b());
        inputArgs.setA3(line3Args.a());
        inputArgs.setB3(line3Args.b());
        inputArgs.setB3(line3Args.b());
        return inputArgs;
    }

    private record LineByPointArgs(int x1, int y1, int x2, int y2) {
        private void validate() throws InvalidLineException {
            if (new Point(x1, y1).isCloseTo(new Point(x2, y2))) {
                throw new InvalidLineException("Некоректно задана пряма: пряма не може бути задана двома співпадаючими точками; Спробуйте ввести дві різні точки, щоб побудувати пряму.");
            }
        }
    }
    private record LineBySegmentsArgs(int a, int b) {
        private void validate() throws InvalidLineException {
            if (Numbers.isZero(a) && Numbers.isZero(b)) {
                throw new InvalidLineException("Некоректно задана пряма: пряма не може бути задана двома нулевими відрізками; Надайте ненулеві значення параметрам A та B.");
            }
            if (Numbers.isZero(a)) {
                throw new InvalidLineException("Некоректно задана пряма: пряма не може відтинати нулевий відрізок на осі OX; Спробуйте ввести ненулеве значення параметра A.");
            }
            if (Numbers.isZero(b)) {
                throw new InvalidLineException("Некоректно задана пряма: пряма не може відтинати нулевий відрізок на осі OY; Спробуйте ввести ненулеве значення параметра B.");
            }
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
        String howToFix = String.format(" Введіть ціле десяткове число з проміжку [-%d; %d], будь ласка.", Numbers.BOX_SIZE, Numbers.BOX_SIZE);
        while (true) {
            String string = userIO.getString(prompt);
            if (string == null || string.isEmpty()) {
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
    private static class IntersectionDeterminer {
        private final double x1;
        private final double x2;
        private final double y1;
        private final double y2;
        private final double a2;
        private final double a3;
        private final double b2;
        private final double b3;

        public IntersectionDeterminer(InputArgs inputArgs) {
            this.x1 = inputArgs.getX1();
            this.x2 = inputArgs.getX2();
            this.y1 = inputArgs.getY1();
            this.y2 = inputArgs.getY2();
            this.a2 = inputArgs.getA2();
            this.a3 = inputArgs.getA3();
            this.b2 = inputArgs.getB2();
            this.b3 = inputArgs.getB3();
        }

        /**
         * Calculates intersections
         *
         * @return line intersection message
         */
        public String getIntersections() {
            if (isAllParallelLines()) {
                return Messages.parallel();
            }
            if (isOneIntersection()) {
                return findOneIntersection();
            }
            if (isTwoIntersections()) {
                return findTwoIntersections();
            }
            return findThreeIntersections();
        }
        private String findTwoIntersections() {
            if (isLines2And3Parallel()) { // find where line 1 intersect parallel lines 2 and 3
                return Messages.intersections(List.of(lines1And2Intersection(), lines1And3Intersection()));
            }
            if (isLines1And3Parallel()) { // find where line 2 intersect parallel lines 1 and 3
                return Messages.intersections(List.of(lines1And2Intersection(), lines2And3Intersection()));
            }
            if (isLines1And2Parallel()) { // find where line 3 intersect parallel lines 1 and 2
                return Messages.intersections(List.of(lines1And3Intersection(), lines2And3Intersection()));
            }
            throw new IllegalStateException("Не вдалося знайти дві паралельні прямі з даного набору вхідних значень: " + argsList());
        }

        private String argsList() {
            return List.of(x1, y1, x2, y2, a2, b2, a3, b3).toString();
        }

        private String findThreeIntersections() {
            return Messages.intersections(List.of(
                    lines1And2Intersection(),
                    lines1And3Intersection(),
                    lines2And3Intersection()
            ));
        }

        private boolean isTwoIntersections() {
            if (isLines2And3Parallel()) {
                return true;
            }
            if (isLines1And3Parallel()) {
                return true;
            }
            return isLines1And2Parallel();
        }

        private boolean isLines1And2Parallel() {
            if (Numbers.equals(x1, x2)) {
                return false;
            }
            return Numbers.equals((y2 - y1) / (x1 - x2), b2 / a2);
        }

        private boolean isLines1And3Parallel() {
            if (Numbers.equals(x1, x2)) {
                return false;
            }
            return Numbers.equals((y2 - y1) / (x1 - x2), b3 / a3);
        }

        private boolean isLines2And3Parallel() {
            return Numbers.equals(b2 / a2, b3 / a3);
        }

        private String findOneIntersection() {
            if (lines1And2Match()) {
                return Messages.intersection(lines1And3Intersection());
            }
            if (lines1And3Match()) {
                return Messages.intersection(lines1And2Intersection());
            }
            return Messages.intersection(lines2And3Intersection());

        }

        private Point lines1And2Intersection() {
            double denominator = (y2 - y1) * a2 - b2 * (x1 - x2);
            if (Numbers.isZero(denominator)) {
                throw new IllegalStateException("Прямі 1 та 2 не перетинаються, неможливо знайти точку перетину даних прямих");
            }
            return new Point(
                    -a2 * ( x2 * y1 - x1 * y2 + b2 * x1 - b2 * x2 ) / denominator
                    , b2 * ( y2 * a2 - y1 * a2 - x1 * y2 + x2 * y1 ) / denominator
            );
        }

        private Point lines2And3Intersection() {
            double denominator = b2 * a3 - b3 * a2;
            if (Numbers.isZero(denominator)) {
                throw new IllegalStateException("Прямі 2 та 3 не перетинаються, неможливо знайти точку перетину даних прямих");
            }
            return new Point(
                    a2 * a3 * (b2 - b3) / denominator,
                    b2 * b3 * (a3 - a2) / denominator
            );
        }

        private Point lines1And3Intersection() {
            double denominator = (y2 - y1) * a3 - b3 * (x1 - x2);
            if (Numbers.isZero(denominator)) {
                throw new IllegalStateException("Прямі 1 та 3 не перетинаються, неможливо знайти точку перетину даних прямих");
            }
            return new Point(
                    -a3 * ( x2 * y1 - x1 * y2 + b3 * x1 - b3 * x2 ) / denominator
                    , b3 * ( y2 * a3 - y1 * a3 - x1 * y2 + x2 * y1 ) / denominator
            );
        }

        private boolean lines1And2Match() {
            return Numbers.allEquals(List.of((y2 - y1) / b2, (x1 - x2) / a2, (x1 * y2 - x2 * y1) / (b2 * a2)));
        }
        private boolean lines1And3Match() {
            return Numbers.allEquals(List.of((y2 - y1) / b3, (x1 - x2) / a3, (x1 * y2 - x2 * y1) / (b3 * a3)));
        }

        private boolean isOneIntersection() {
            return Numbers.equals(
                    (y2 - y1) * a2 * a3 * (b2 - b3) + (x1 - x2) * b2 * b3 * (a3 - a2),
                    (x1 * y2 - x2 * y1) * (b2 * a3 - b3 * a2)
                    );
        }

        private boolean isAllParallelLines() {
            if (Numbers.isZero(x1 - x2)) {
                return false;
            }
            return Numbers.allEquals(List.of(
                    (y2 - y1) / (x1 - x2), b3/a3, b2/a2
            ));
        }
    }

}
