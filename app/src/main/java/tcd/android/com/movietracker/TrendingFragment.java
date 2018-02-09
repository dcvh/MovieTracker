package tcd.android.com.movietracker;

import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import ee.subscribe.gooeyloader.GooeyLoaderView;
import tcd.android.com.movietracker.Entities.Movie;
import tcd.android.com.movietracker.Utils.TmdbUtils;

public class TrendingFragment extends Fragment {

    private static final String TAG = TrendingFragment.class.getSimpleName();

    private static final int DEFAULT_OFFSCREEN_PAGE_LIMIT = 10;

    public static TrendingFragment newInstance() {
        Bundle args = new Bundle();
        TrendingFragment fragment = new TrendingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trending, container, false);

        initMovieSlider(view);

        return view;
    }

    private void initMovieSlider(View view) {

        final GooeyLoaderView loadingView = view.findViewById(R.id.glv_downloading);

        // init view pager
        ViewPager movieSliderViewPager = view.findViewById(R.id.vp_movie_slider);
        movieSliderViewPager.setOffscreenPageLimit(DEFAULT_OFFSCREEN_PAGE_LIMIT);
        movieSliderViewPager.setPageTransformer(false, new ParralaxPageTransformer());
        final ArrayList<Movie> movies = new ArrayList<>();
        final TrendingMoviePagerAdapter adapter =
                new TrendingMoviePagerAdapter(getChildFragmentManager(), movies);
        movieSliderViewPager.setAdapter(adapter);

        // retrieve movies async
        HandlerThread handlerThread = new HandlerThread(TAG);
        handlerThread.start();
        new Handler(handlerThread.getLooper()).post(new Runnable() {
            @Override
            public void run() {
                movies.addAll(TmdbUtils.findNowShowingMovies());
                loadingView.post(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                        loadingView.setVisibility(View.GONE);
                    }
                });
            }
        });
    }

    class TrendingMoviePagerAdapter extends FragmentPagerAdapter {

        private ArrayList<Movie> mMovies;

        TrendingMoviePagerAdapter(FragmentManager fm, ArrayList<Movie> movies) {
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
    }

    class ParralaxPageTransformer implements ViewPager.PageTransformer {
        @Override
        public void transformPage(@NonNull View page, float position) {
            int pageWidth = page.getWidth();
            GradientImageView posterImageView = page.findViewById(R.id.giv_poster);
            posterImageView.setTranslationX(-position * (pageWidth / 2));
        }
    }
}
