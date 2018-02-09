package tcd.android.com.movietracker;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

import tcd.android.com.movietracker.Entities.Movie;

/**
 * Created by cpu10661 on 2/8/18.
 */

public class TrendingMoviePagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Movie> mMovies;

    public TrendingMoviePagerAdapter(FragmentManager fm, ArrayList<Movie> movies) {
        super(fm);
        mMovies = movies;
    }

    @Override
    public Fragment getItem(int position) {
        Movie movie = mMovies.get(position);
        return MovieDetailsFragment.newInstance(movie);
    }

    @Override
    public int getCount() {
        return mMovies.size();
    }

    @Override
    public long getItemId(int position) {
        return mMovies.get(position).getId();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mMovies.get(position).getTitle();
    }
}
