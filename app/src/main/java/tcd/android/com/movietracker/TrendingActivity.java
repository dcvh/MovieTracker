package tcd.android.com.movietracker;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;

import tcd.android.com.movietracker.Entities.Movie;
import tcd.android.com.movietracker.Utils.TmdbUtils;

public class TrendingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trending);

        setUpToolbar();

        ViewPager movieSliderViewPager = findViewById(R.id.vp_movie_slider);
        final ArrayList<Movie> movies = new ArrayList<>();
        final TrendingMoviePagerAdapter adapter =
                new TrendingMoviePagerAdapter(getSupportFragmentManager(), movies);
        movieSliderViewPager.setAdapter(adapter);

        HandlerThread handlerThread = new HandlerThread(getPackageName());
        handlerThread.start();
        new Handler(handlerThread.getLooper()).post(new Runnable() {
            @Override
            public void run() {
                movies.addAll(TmdbUtils.findNowShowingMovies());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
