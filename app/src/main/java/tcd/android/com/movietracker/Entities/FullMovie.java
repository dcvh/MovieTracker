package tcd.android.com.movietracker.Entities;

import java.util.ArrayList;

import tcd.android.com.movietracker.Entities.Credit.Actor;
import tcd.android.com.movietracker.Entities.Credit.CrewMember;

/**
 * Created by cpu10661 on 1/19/18.
 */

public class FullMovie {

    private Movie mMovie;
    private ArrayList<Actor> mCast;
    private ArrayList<CrewMember> mCrew;
    private MovieExtra mMovieExtra;
    private String[] mBackdrops;

    public FullMovie() {}

    public FullMovie(Movie movie) {
        mMovie = movie;
    }

    // TODO: 2/7/18 wrong builder pattern
    public FullMovie addMovie(Movie movie) {
        mMovie = movie;
        return this;
    }

    public FullMovie addCast(ArrayList<Actor> cast) {
        mCast = cast;
        return this;
    }

    public FullMovie addCrew(ArrayList<CrewMember> crew) {
        mCrew = crew;
        return this;
    }

    public FullMovie addExtra(MovieExtra extra) {
        mMovieExtra = extra;
        return this;
    }

    public FullMovie addBackdrops(String[] backdrops) {
        mBackdrops = backdrops;
        return this;
    }

    public Movie getMovie() { return mMovie; }

    public ArrayList<Actor> getCast() {
        return new ArrayList<>(mCast);
    }

    public ArrayList<CrewMember> getCrew() {
        return new ArrayList<>(mCrew);
    }

    public MovieExtra getMovieExtra() {
        return mMovieExtra;
    }

    public String[] getBackdrops() { return mBackdrops; }
}
