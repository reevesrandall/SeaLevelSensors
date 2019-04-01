package com.cs3312.team8327.floodar.Util;

/**
 * Class for formatting height for the purpose of use in AR
 */
public class HeightFormatter {

    private static final float METERS_PER_FOOT = 3.280839895f;

    public static String stringForHeight(float meters) {
        float feet = (meters * METERS_PER_FOOT);
        int inches = (int)Math.round(feet * 12.0f % 12.0f);
        int roundedFeet = (int) feet;
        String text;
        if (inches % 12 == 0) {
            text = String.format("%d'", (inches == 12) ? roundedFeet + 1 : roundedFeet);
        } else {
            text = String.format("%d' %d\"", roundedFeet, inches);
        }
        return text;
    }

}
