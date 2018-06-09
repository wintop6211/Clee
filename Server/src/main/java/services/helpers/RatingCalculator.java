package main.java.services.helpers;

/**
 * The calculator for calculating the rate
 */
public class RatingCalculator {
    /**
     * Round to .5 precision for the rate score
     *
     * @param d The number needs to be rounded
     * @return The rounded number
     */
    public static double roundToHalf(double d) {
        return Math.round(d * 2) / 2.0;
    }
}
