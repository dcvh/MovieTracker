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
}
