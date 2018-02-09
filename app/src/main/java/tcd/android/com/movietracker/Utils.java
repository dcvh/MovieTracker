package tcd.android.com.movietracker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.text.format.DateUtils;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import tcd.android.com.movietracker.Entities.Credit.Actor;

/**
 * Created by cpu10661 on 1/19/18.
 */

@SuppressWarnings("WeakerAccess")
public class Utils {

    public static String getFirstBilledCast(Actor[] cast, int totalActors) {
        int length = Math.min(cast.length, totalActors);
        String[] actorNames = new String[length];
        for (int i = 0; i < length; i++) {
            actorNames[i] = cast[i].getName();
        }
        return TextUtils.join(", ", actorNames);
    }

    public static String join(String delimiter, Object[] tokens, int limit) {
        Object[] limitedTokens = Arrays.copyOf(tokens, limit);
        return TextUtils.join(delimiter, limitedTokens);
    }

    public static String formatNumber(int number, String pattern) {
        DecimalFormat myFormatter = new DecimalFormat(pattern);
        return myFormatter.format(number);
    }

    public static long getMillis(String dateString, String format) throws ParseException {
        Date date = new SimpleDateFormat(format, Locale.getDefault()).parse(dateString);
        return date.getTime();
    }

    @NonNull
    public static String getDate(@NonNull Context context, long millis) {
        return DateUtils.formatDateTime(context, millis, DateUtils.FORMAT_ABBREV_ALL);
    }

    @NonNull
    public static String getDuration(int runtime) {
        if (runtime < 60) {
            return String.valueOf(runtime);
        } else {
            return String.format(Locale.getDefault(),
                    "%dh%dm", runtime / 60, runtime % 60);
        }
    }


    static Bitmap darkenBitmap(@NonNull Bitmap src) {
        Bitmap result = src.copy(src.getConfig(), true);
        Canvas canvas = new Canvas(result);
        Paint p = new Paint(Color.RED);

        ColorFilter filter = new LightingColorFilter(0xFF666666, 0x00000000);    // darken
        p.setColorFilter(filter);
        canvas.drawBitmap(result, new Matrix(), p);

        return result;
    }
}
