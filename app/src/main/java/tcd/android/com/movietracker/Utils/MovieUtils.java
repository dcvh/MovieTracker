package tcd.android.com.movietracker.Utils;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import tcd.android.com.movietracker.Entities.Credit.Actor;

/**
 * Created by cpu10661 on 2/7/18.
 */

public class MovieUtils {

    @NonNull
    public static String getFirstBilledCast(Actor[] cast, int totalActors) {
        if (cast == null) {
            return "";
        }

        int length = Math.min(cast.length, totalActors);
        String[] actorNames = new String[length];
        for (int i = 0; i < length; i++) {
            actorNames[i] = cast[i].getName();
        }
        return TextUtils.join(", ", actorNames);
    }

}
