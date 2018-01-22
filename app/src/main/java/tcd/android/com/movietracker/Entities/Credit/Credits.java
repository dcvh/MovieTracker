package tcd.android.com.movietracker.Entities.Credit;

import android.support.annotation.NonNull;

import java.util.ArrayList;

/**
 * Created by cpu10661 on 1/19/18.
 */

public class Credits {
    private ArrayList<Actor> mCast;
    private ArrayList<Crew> mCrew;

    public Credits(@NonNull ArrayList<Actor> mCast, @NonNull ArrayList<Crew> mCrew) {
        this.mCast = mCast;
        this.mCrew = mCrew;
    }

    public ArrayList<Actor> getCast() {
        return mCast;
    }

    public ArrayList<Crew> getCrew() {
        return mCrew;
    }
}
