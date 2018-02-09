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
    private String mBackdropPath;
    private int[] mGenreIds;
    private String mOverview;
    private long mReleaseDate;
    private Actor[] mCast;

    public Movie() {}

    public Movie(int id, String title) {
        mId = id;
        mTitle = title;
    }

    public Movie addVote(float averageVote, int count) {
        mAverageVote = averageVote;
        mVoteCount = count;
        return this;
    }

    public Movie addImages(String poster, String backdrop) {
        mPosterPath = poster;
        mBackdropPath = backdrop;
        return this;
    }

    public Movie addGenreIds(int[] genreIds) {
        mGenreIds = genreIds;
        return this;
    }

    public Movie addOverview(String overview) {
        mOverview = overview;
        return this;
    }

    public Movie addReleaseDate(long releaseDate) {
        mReleaseDate = releaseDate;
        return this;
    }

    public Movie addCast(Actor[] cast) {
        mCast = cast;
        return this;
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

    public String getBackdropPath() { return mBackdropPath; }

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
