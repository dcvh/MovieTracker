package tcd.android.com.movietracker.Utils;

import android.text.TextUtils;

import tcd.android.com.movietracker.Entities.Credit.Actor;

/**
 * Created by cpu10661 on 2/7/18.
 */

public class MovieUtils {

    public static String getFirstBilledCast(Actor[] cast, int totalActors) {
        int length = Math.min(cast.length, totalActors);
        String[] actorNames = new String[length];
        for (int i = 0; i < length; i++) {
            actorNames[i] = cast[i].getName();
        }
        return TextUtils.join(", ", actorNames);
    }

}
