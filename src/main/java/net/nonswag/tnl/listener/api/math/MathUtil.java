package net.nonswag.tnl.listener.api.math;

import java.util.Random;

public class MathUtil {

    public static boolean isInLine(int line, int i) {
        return i % line == 0;
    }

    public static boolean isInLine(double line, double i) {
        return i % line == 0;
    }

    public static boolean isInLine(float line, float i) {
        return i % line == 0;
    }

    public static boolean isInLine(long line, long i) {
        return i % line == 0;
    }

    public static int randomInteger(int from, int to) {
        return (new Random().nextInt(to + 1 - from) + from);
    }

    public static double randomDouble(double from, double to) {
        return ((from + (to - from)) * (new Random().nextDouble()));
    }

    public static double randomFloat(float from, float to) {
        return ((from + (to - from)) * (new Random().nextFloat()));
    }

    public static long randomLong(long from, long to) {
        return ((from + (to - from)) * (new Random().nextLong()));
    }

    public static double validateDouble(double min, double max, double value) {
        return value < min ? min : min(value, max);
    }

    public static float validateFloat(float min, float max, float value) {
        return value < min ? min : min(value, max);
    }

    public static int validateInteger(int min, int max, int value) {
        return value < min ? min : min(value, max);
    }

    public static long validateLong(long min, long max, long value) {
        return value < min ? min : min(value, max);
    }

    public static long min(long min, long value) {
        return Math.min(min, value);
    }

    public static int min(int min, int value) {
        return Math.min(min, value);
    }

    public static float min(float min, float value) {
        return Math.min(min, value);
    }

    public static double min(double min, double value) {
        return Math.min(min, value);
    }

    public static long max(long max, long value) {
        return Math.max(max, value);
    }

    public static int max(int max, int value) {
        return Math.max(max, value);
    }

    public static float max(float max, float value) {
        return Math.max(max, value);
    }

    public static double max(double max, double value) {
        return Math.max(max, value);
    }

    public static int round(double value) {
        return ((int) value);
    }

    public static int round(float value) {
        return ((int) value);
    }

    public static int round(long value) {
        return ((int) value);
    }

    public static double toDouble(int value) {
        return value;
    }

    public static double toDouble(float value) {
        return value;
    }

    public static double toDouble(long value) {
        return ((double) value);
    }

    public static float toFloat(int value) {
        return ((float) value);
    }

    public static float toFloat(double value) {
        return ((float) value);
    }

    public static float toFloat(long value) {
        return ((float) value);
    }

    public static long toLong(int value) {
        return value;
    }

    public static long toLong(double value) {
        return ((long) value);
    }

    public static long toLong(float value) {
        return ((long) value);
    }

    public static double threeLettersOfDouble(double number) {
        return (Math.round(number * 100d) / 100d);
    }

    public static float threeLettersOfFloat(float number) {
        return (Math.round(number * 100f) / 100f);
    }

    public static long threeLettersOfLong(long number) {
        return (Math.round(number * 100L) / 100L);
    }

    public static int threeLettersOfInteger(int number) {
        return (Math.round(number * 100) / 100);
    }
}
