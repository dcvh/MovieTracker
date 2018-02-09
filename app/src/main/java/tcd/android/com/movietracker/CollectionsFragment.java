package tcd.android.com.movietracker;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import java.util.ArrayList;

import tcd.android.com.movietracker.Entities.Movie;
import tcd.android.com.movietracker.Utils.DataUtils;

public class CollectionsFragment extends Fragment {

    private static final String TAG = CollectionsFragment.class.getSimpleName();

    public static CollectionsFragment newInstance() {
        Bundle args = new Bundle();
        CollectionsFragment fragment = new CollectionsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_collections, container, false);

        setUpMovieList(view);

        return view;
    }

    private void setUpMovieList(View view) {
        RecyclerView movieListRecyclerView = view.findViewById(R.id.rv_movie_list);
        movieListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        movieListRecyclerView.setItemAnimator(new DefaultItemAnimator());
        movieListRecyclerView.setHasFixedSize(true);

        ArrayList<Movie> movies = DataUtils.getDebugMovieList();
        MovieAdapter adapter = new MovieAdapter(getContext(), movies);
        adapter.setHasStableIds(true);
        movieListRecyclerView.setAdapter(adapter);
    }
}
