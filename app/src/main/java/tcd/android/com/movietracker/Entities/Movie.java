package tcd.android.com.movietracker.Entities;

import java.io.Serializable;

import tcd.android.com.movietracker.Entities.Credit.Actor;

/**
 * Created by ADMIN on 27/10/2017.
 */

public class Movie implements Serializable {

    private int mId;
    private String mTitle;
    private float mAverageVote;
    private int mVoteCount;
    private String mPosterPath;
    private int[] mGenreIds;
    private String mOverview;
    private long mReleaseDate;
    private Actor[] mCast;

    public Movie() {}

    public Movie(int id, String title, float averageVote, int voteCount, String posterPath,
                 int[] genreIds, String overview, long releaseDate, Actor[] cast) {
        mId = id;
        mTitle = title;
        mAverageVote = averageVote;
        mVoteCount = voteCount;
        mPosterPath = posterPath;
        mGenreIds = genreIds;
        mOverview = overview;
        mReleaseDate = releaseDate;
        mCast = cast;
    }

    public int getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public float getAverageVote() {
        return mAverageVote;
    }

    public int getVoteCount() {
        return mVoteCount;
    }

    public String getPosterPath() {
        return mPosterPath;
    }

    public int[] getGenreIds() {
        return mGenreIds;
    }

    public String getOverview() {
        return mOverview;
    }

    public long getReleaseDate() {
        return mReleaseDate;
    }

    public Actor[] getCast() {
        return mCast;
    }

    public String getFirstBilledActors(int actorCount) {
        // TODO: 1/19/18 should be a simple POJO without complicated methods
        StringBuilder majorCasts = new StringBuilder();
        if (mCast != null) {
            int limit = Math.min(actorCount, mCast.length);
            for (int i = 0; i < limit; i++) {
                majorCasts.append(mCast[i].getName());
                majorCasts.append(", ");
            }
        }
//        majorCasts.setCharAt(majorCasts.length() - 1, '\u2026');         // ellipsis
        majorCasts.setLength(majorCasts.length() - 2);
        return majorCasts.toString();
    }
}
