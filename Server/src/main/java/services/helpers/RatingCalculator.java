package main.java.services.helpers;

public class RatingCalculator {
    public static double roundToHalf(double d) {
        return Math.round(d * 2) / 2.0;
    }
}
