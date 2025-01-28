package mouse.univ.geometry;

import mouse.univ.common.Messages;
import mouse.univ.common.Numbers;

import java.util.List;

public class IntersectionCalculator {
    public String getIntersections(GenericLine line1, GenericLine line2, GenericLine line3) {
        LineIntersectionResult lines1_2Pos = checkLinesIntersect(line1, line2);
        LineIntersectionResult lines1_3Pos = checkLinesIntersect(line1, line3);
        if (lines1_2Pos.isSameLine()) {
            if (lines1_3Pos.isSameLine()) {
                return Messages.sameLine();
            } else if (lines1_3Pos.isParallelLines()) {
                return Messages.parallel();
            } else {
                return Messages.intersections(List.of(lines1_3Pos.intersection()));
            }
        } else if (lines1_2Pos.isParallelLines()) {
            if (lines1_3Pos.isParallelLines() || lines1_3Pos.isSameLine()) {
                return Messages.parallel();
            }
            LineIntersectionResult lines2_3Pos = checkLinesIntersect(line2, line3);
            if (lines2_3Pos.isParallelLines() || lines2_3Pos.isSameLine()) {
                return Messages.parallel();
            }
            return Messages.intersections(List.of(lines1_3Pos.intersection(), lines2_3Pos.intersection()));
        } else if (lines1_2Pos.isIntersectingLines()) {
            LineIntersectionResult lines2_3Pos = checkLinesIntersect(line2, line3);
            if (lines1_3Pos.isSameLine() || lines2_3Pos.isSameLine()) {
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
            throw new IllegalStateException("Cannot define relative position of lines 1 and 2.");
        }
    }
    private enum LinesRelativePosition {
        SAME_LINE, PARALLEL, INTERSECT
    }

    private record LineIntersectionResult(LinesRelativePosition relative, Point intersection) {
        public Point intersection() {
            if (relative.equals(LinesRelativePosition.INTERSECT) && intersection == null) {
                throw new IllegalStateException("Line intersection result mark as INTERSECT, but doesnt have the intersection point");
            }
            if (relative.equals(LinesRelativePosition.INTERSECT) || relative.equals(LinesRelativePosition.SAME_LINE)) {
                throw new IllegalStateException("Trying to get intersection point for lines that do not intersect");
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
