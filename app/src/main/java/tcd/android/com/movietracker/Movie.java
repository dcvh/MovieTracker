package tcd.android.com.movietracker;

import java.util.ArrayList;

/**
 * Created by ADMIN on 27/10/2017.
 */

public class Movie {
    private int mId;
    private String mTitle;
    private float mAverageVote;
    private int mVoteCount;
    private String mPosterPath;
    private int[] mGenreIds;
    private String mOverview;
    private String mReleaseDate;
    private Cast[] mCasts;

    public Movie(int mId, String mTitle, float mAverageVote, int mVoteCount, String mPosterPath, int[] mGenreIds,
                 String mOverview, String mReleaseDate, Cast[] mCasts) {
        this.mId = mId;
        this.mTitle = mTitle;
        this.mAverageVote = mAverageVote;
        this.mVoteCount = mVoteCount;
        this.mPosterPath = mPosterPath;
        this.mGenreIds = mGenreIds;
        this.mOverview = mOverview;
        this.mReleaseDate = mReleaseDate;
        this.mCasts = mCasts;
    }

    public Movie(String mTitle, String posterPath, Cast[] mCasts) {
        this.mTitle = mTitle;
        this.mPosterPath = posterPath;
        this.mCasts = mCasts;
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

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public Cast[] getCasts() {
        return mCasts;
    }
}
