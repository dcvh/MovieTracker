package tcd.android.com.movietracker.Entities;

import tcd.android.com.movietracker.Entities.Credit.Actor;
import tcd.android.com.movietracker.Entities.Credit.Credits;

/**
 * Created by cpu10661 on 1/19/18.
 */

public class MovieExtraInfo {

    private Credits mCredits;
    private MovieDetails mMovieDetails;

    public MovieExtraInfo(Credits credits, MovieDetails movieDetails) {
        mCredits = credits;
        mMovieDetails = movieDetails;
    }

    public Credits getCredits() {
        return mCredits;
    }

    public MovieDetails getMovieDetails() {
        return mMovieDetails;
    }
}
