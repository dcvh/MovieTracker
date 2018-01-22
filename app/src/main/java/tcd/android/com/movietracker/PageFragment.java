package tcd.android.com.movietracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;

import java.text.ParseException;
import java.util.ArrayList;

import tcd.android.com.movietracker.Entities.Credit.Actor;
import tcd.android.com.movietracker.Entities.Movie;

import static tcd.android.com.movietracker.MovieDetailsActivity.ARGS_MOVIE_DETAILS;

/**
 * Created by ADMIN on 12/11/2017.
 */

public  class PageFragment extends Fragment {

    public PageFragment() {}

    public static PageFragment newInstance(int sectionNumber) {
        PageFragment fragment = new PageFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        AutoCompleteTextView autoCompleteTextView = rootView.findViewById(R.id.actv_add);
        autoCompleteTextView.setAdapter(new MovieAutoCompleteAdapter(getContext()));

        RecyclerView moviesListRecyclerView = rootView.findViewById(R.id.rv_movies_list);
        moviesListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        moviesListRecyclerView.setItemAnimator(new DefaultItemAnimator());

        ArrayList<Movie> movies = new ArrayList<>();
        try {
            movies.add(new Movie(
                    76341,
                    "Mad Max: Fury Road",
                    7.3f,
                    2661,
                    "/kqjL17yufvn9OVLyXYpvtyrFfak.jpg",
                    null,
                    "An apocalyptic story set in the furthest reaches of our planet, in a stark desert landscape where humanity is broken, and most everyone is crazed fighting for the necessities of life. Within this world exist two rebels on the run who just might be able to restore order. There's Max, a man of action and a man of few words, who seeks peace of mind following the loss of his wife and child in the aftermath of the chaos. And Furiosa, a woman of action and a woman who believes her path to survival may be achieved if she can make it across the desert back to her childhood homeland.",
                    Utils.getMillis("2015-05-13", "YYYY-MM-dd"),
                    new Actor[] {
                            new Actor(2524, "Tom Hardy", "Max Rockatansky", "/mHSmt9qu2JzEPqnVWCGViv9Stnn.jpg"),
                            new Actor(6885, "Charlie Theron", "Imperator Furiosa", "/k5Xt2mNlraX7yHYaPy9gvayCaKV.jpg")
                    }));
            movies.add(new Movie(
                    335984,
                    "Blade Runner 2049",
                    7.4f,
                    10486,
                    "/aMpyrCizvSdc0UIMblJ1srVgAEF.jpg",
                    new int[] {28, 12, 878, 53},
                    "Thirty years after the events of the first film, a new blade runner, LAPD Officer K, unearths a long-buried secret that has the potential to plunge what's left of society into chaos. K's discovery leads him on a quest to find Rick Deckard, a former LAPD blade runner who has been missing for 30 years.",
                    Utils.getMillis("2017-10-04", "YYYY-MM-dd"),
                    new Actor[] {
                            new Actor(30614, "Ryan Gosling", "K", "/5rOcicCrTCWye0O2S3dnbnWaCr1.jpg"),
                            new Actor(3, "Harrison Ford", "Rick Deckard", "/7CcoVFTogQgex2kJkXKMe8qHZrC.jpg")
                    }));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Intent detailsIntent = new Intent(getContext(), MovieDetailsActivity.class);
        detailsIntent.putExtra(ARGS_MOVIE_DETAILS, movies.get(1));
        getContext().startActivity(detailsIntent);

        MovieAdapter adapter = new MovieAdapter(movies, getContext());
        moviesListRecyclerView.setAdapter(adapter);
        return rootView;
    }
}