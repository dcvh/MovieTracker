package tcd.android.com.movietracker;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import tcd.android.com.movietracker.Entities.Credit.Actor;
import tcd.android.com.movietracker.Entities.Credit.Credits;
import tcd.android.com.movietracker.Entities.Credit.Crew;
import tcd.android.com.movietracker.Entities.MovieDetails;
import tcd.android.com.movietracker.StarRating.CastAdapter;
import tcd.android.com.movietracker.StarRating.CircularRatingsBar;
import tcd.android.com.movietracker.Entities.Movie;

public class MovieDetailsActivity extends AppCompatActivity {

    private static final String TAG = MovieDetailsActivity.class.getSimpleName();

    public static final String ARGS_MOVIE_DETAILS = "argsMovieDetails";
    private static final int DEFAULT_ANIMATION_DURATION = 300;

    private NestedScrollView mBottomSheetNSV;
    private BottomSheetBehavior mBottomSheetBehavior;
    private FloatingActionButton mWatchLaterFAB;
    private ImageView mPosterImageView;
    private TextView mFirstBilledCastTextView;
    private ImageView mCarouselImageView;    // TODO: 1/18/18 consider changing to trailer or carousel

    private int mPeekHeight = 300;
    private int mScreenHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        if (!getIntent().hasExtra(ARGS_MOVIE_DETAILS)) {
            throw new IllegalArgumentException("There is no data in the forwarded intent");
        }

        initializeUiComponents();
    }

    @Override
    public void onBackPressed() {
        if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            // temporary workaround, calling twice to scroll to top
            mBottomSheetNSV.smoothScrollTo(0, 0);
            mBottomSheetNSV.smoothScrollTo(0, 0);
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            return;
        }
        super.onBackPressed();
    }

    private void initializeUiComponents() {

        initializeToolbar();

        mWatchLaterFAB = findViewById(R.id.fab_watch_later);
        mFirstBilledCastTextView = findViewById(R.id.tv_first_billed_cast);

        // bottom sheet
        initializeBottomSheet();

        // TODO: 1/19/18 implement real carousel or insert a trailer
        // carousel
        mCarouselImageView = findViewById(R.id.iv_carousel);
        Glide.with(this).load(R.drawable.carousel).into(mCarouselImageView);

        populateMovieInfo();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        mScreenHeight = mPosterImageView.getHeight();

        final LinearLayout peekLinearLayout = findViewById(R.id.ll_peek);
        mPeekHeight = peekLinearLayout.getHeight();
        mBottomSheetBehavior.setPeekHeight(mPeekHeight);
        mCarouselImageView.setTranslationY(mScreenHeight - mPeekHeight);
    }

    private void initializeToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    private void initializeBottomSheet() {

        mBottomSheetNSV = findViewById(R.id.bottom_sheet);
        mBottomSheetBehavior = BottomSheetBehavior.from(mBottomSheetNSV);
        // TODO: 1/18/18 find a way to hide bottom sheet
//        mBottomSheetBehavior.setHideable(true);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_DRAGGING:
                        mWatchLaterFAB.animate().scaleX(0).scaleY(0)
                                .setDuration(DEFAULT_ANIMATION_DURATION).start();
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        mWatchLaterFAB.animate().scaleX(1).scaleY(1)
                                .setDuration(DEFAULT_ANIMATION_DURATION).start();
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                mFirstBilledCastTextView.setAlpha(1 - slideOffset);
                mCarouselImageView.setTranslationY((1 - slideOffset) * (mScreenHeight - mPeekHeight));
            }
        });
    }

    private void populateMovieInfo() {
        // TODO: 03/12/2017 convert to parcelable
        Movie movie = (Movie) getIntent().getSerializableExtra(ARGS_MOVIE_DETAILS);

        mPosterImageView = findViewById(R.id.iv_poster);
        String posterUrl = TmdbUtils.getImageUrl(movie.getPosterPath());
        Glide.with(this).load(posterUrl).into(mPosterImageView);

        TextView titleTextView = findViewById(R.id.tv_title);
        titleTextView.setText(movie.getTitle());

        mFirstBilledCastTextView.setText(movie.getFirstBilledActors(2));

        TextView releaseDateTextView = findViewById(R.id.tv_release_date);
        releaseDateTextView.setText(Utils.getDate(this, movie.getReleaseDate()));

        TextView overviewTextView = findViewById(R.id.tv_overview);
        overviewTextView.setText(movie.getOverview());

        initializeAverageVoteIndicator(movie.getAverageVote(), movie.getVoteCount());
        initializeCredits(movie.getId());
        initializeOtherDetails(movie.getId());
    }

    private void initializeAverageVoteIndicator(float averageVote, int voteCount) {

        CircularRatingsBar averageVoteCRB = findViewById(R.id.crb_average_vote);

        final TextView voteCountTextView = findViewById(R.id.tv_vote_count);
        voteCountTextView.setText(String.valueOf(Utils.formatNumber(voteCount, "###,###,###")));
        voteCountTextView.setTextColor(averageVoteCRB.getColor());

        averageVoteCRB.setRatings(averageVote);
        averageVoteCRB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                voteCountTextView.animate().alpha(1);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        voteCountTextView.animate().alpha(0);
                    }
                }, 2000);
            }
        });
    }

    private void initializeCredits(int movieId) {
        new PopulateCreditTask(this)
                .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, movieId);
    }

    private void initializeOtherDetails(int movieId) {
        new PopulateDetailsTask(this)
                .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, movieId);
    }

    private static class PopulateCreditTask extends AsyncTask<Integer, Void, Credits> {

        private WeakReference<Context> mContext;

        PopulateCreditTask(Context context) {
            this.mContext = new WeakReference<>(context);
        }

        @Override
        protected Credits doInBackground(Integer... integers) {
            int movieId = integers[0];
            return TmdbUtils.findCreditsById(movieId);
        }

        @Override
        protected void onPostExecute(Credits credits) {
            super.onPostExecute(credits);

            Context context = mContext.get();
            if (context != null && credits != null) {
                populateCastInfo(credits.getCast(), context);
                populateCrewInfo(credits.getCrew(), context);
            }
        }

        private void populateCastInfo(@NonNull ArrayList<Actor> cast, @NonNull Context context) {
            RecyclerView castRecyclerView = ((Activity)context).findViewById(R.id.rv_cast_crew);
            castRecyclerView.setItemAnimator(new DefaultItemAnimator());
            castRecyclerView.setHasFixedSize(true);

            RecyclerView.LayoutManager lm = new LinearLayoutManager(context,
                    LinearLayoutManager.HORIZONTAL, false);
            castRecyclerView.setLayoutManager(lm);

            CastAdapter castAdapter = new CastAdapter(cast, Glide.with(context));
            castAdapter.setHasStableIds(true);
            castRecyclerView.setAdapter(castAdapter);
        }

        private void populateCrewInfo(@NonNull ArrayList<Crew> crew, @NonNull Context context) {
            // TODO: 1/19/18 fix this mess
            StringBuilder directors = new StringBuilder(), writers = new StringBuilder();
            for (Crew person : crew) {
                if (person.getJob().equals("Director")) {
                    directors.append(person.getName()).append(", ");
                } else if (person.getDepartment().equals("Writing")) {
                    writers.append(person.getName()).append(", ");
                }
            }

            directors.setLength(directors.length() - 2);
            TextView directorTextView = ((Activity)context).findViewById(R.id.tv_director);
            directorTextView.setText(directors);
            writers.setLength(directors.length() - 2);
            TextView writerTextView = ((Activity)context).findViewById(R.id.tv_writer);
            writerTextView.setText(writers);
        }
    }

    private static class PopulateDetailsTask extends AsyncTask<Integer, Void, MovieDetails> {

        private WeakReference<Context> mContext;

        PopulateDetailsTask(Context context) {
            this.mContext = new WeakReference<>(context);
        }

        @Override
        protected MovieDetails doInBackground(Integer... integers) {
            int movieId = integers[0];
            return TmdbUtils.findDetailsById(movieId);
        }

        @Override
        protected void onPostExecute(MovieDetails details) {
            super.onPostExecute(details);

            Context context = mContext.get();
            if (context != null && details != null) {
                Activity activity = (Activity) mContext.get();

                // general info
                TextView generalInfoTextView = activity.findViewById(R.id.tv_general_info);
                String info = "R  " + Utils.getDuration(details.getRuntime()) + "  "
                        + Utils.join(", ", details.getGenres());
                generalInfoTextView.setText(info);

                // tagline
                ((TextView)activity.findViewById(R.id.tv_tagline)).setText(details.getTagline());

                // production countries
                TextView prodCountriesTextView = activity.findViewById(R.id.tv_production_country);
                prodCountriesTextView.setText(Utils.join("\n", details.getCountries()));

                // spoken languages
                TextView languagesTextView = activity.findViewById(R.id.tv_spoken_languages);
                languagesTextView.setText(Utils.join(", ", details.getSpokeLanguages()));
            }
        }
    }
}
