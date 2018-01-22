package tcd.android.com.movietracker.StarRating;

/**
 * Created by Miroslaw Stanek on 21.12.2015.
 */
class Utils {
    static double mapValueFromRangeToRange(double value, double fromLow, double fromHigh, double toLow, double toHigh) {
        return toLow + ((value - fromLow) / (fromHigh - fromLow) * (toHigh - toLow));
    }

    static double clamp(double value, double low, double high) {
        return Math.min(Math.max(value, low), high);
    }
}
