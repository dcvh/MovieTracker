package tcd.android.com.movietracker.Entities;

/**
 * Created by cpu10661 on 1/19/18.
 */

public class MovieExtra {

    private int mRuntime;
    private String mTagline;
    private String[] mGenres;
    private String[] mCountries;
    private String[] mSpokeLanguages;
    private String mClassification;

    public MovieExtra(int runtime, String tagline,
                      String[] genres, String[] countries, String[] spokeLanguages) {
        mRuntime = runtime;
        mTagline = tagline;
        mGenres = genres;
        mCountries = countries;
        mSpokeLanguages = spokeLanguages;
    }

    public int getRuntime() {
        return mRuntime;
    }

    public String getTagline() {
        return mTagline;
    }

    public String[] getGenres() {
        return mGenres;
    }

    public String[] getCountries() {
        return mCountries;
    }

    public String[] getSpokeLanguages() {
        return mSpokeLanguages;
    }

    public String getClassification() { return mClassification; }

    public void setClassification(String classification) {
        mClassification = classification;
    }
}
