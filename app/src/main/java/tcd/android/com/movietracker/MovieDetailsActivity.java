package tcd.android.com.movietracker;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import tcd.android.com.movietracker.Entities.Credit.Actor;
import tcd.android.com.movietracker.Entities.Credit.CrewMember;
import tcd.android.com.movietracker.Entities.FullMovie;
import tcd.android.com.movietracker.Entities.MovieExtra;
import tcd.android.com.movietracker.StarRating.CastAdapter;
import tcd.android.com.movietracker.StarRating.CircularRatingsBar;
import tcd.android.com.movietracker.Entities.Movie;
import tcd.android.com.movietracker.Utils.MovieUtils;
import tcd.android.com.movietracker.Utils.TmdbUtils;
import tcd.android.com.movietracker.Utils.Utils;
import tcd.android.com.movietracker.Utils.Utils.TimeUtils;
import tcd.android.com.movietracker.Utils.Utils.FormatUtils;

public class MovieDetailsActivity extends AppCompatActivity {

    public static final String ARGS_MOVIE_DETAILS = "argsMovieDetails";
    private static final String TAG = MovieDetailsActivity.class.getSimpleName();
    private static final int DEFAULT_ANIMATION_DURATION = 300;
    private static final int UP_INDICATOR_BLINK_DURATION = (int) TimeUnit.SECONDS.toMillis(1);

    private LinearLayout mBottomSheetLayout;
    private BottomSheetBehavior mBottomSheetBehavior;
    private GradientImageView mPosterImageView;
    private ImageView mUpIndicatorImageView;
    private CarouselView mCarouselView;

    private int mPeekHeight = 300;
    private int mScreenHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        if (!getIntent().hasExtra(ARGS_MOVIE_DETAILS)) {
            throw new IllegalArgumentException("There is no data in the forwarded intent");
        }

        initUiComponents();
    }

    @Override
    public void onBackPressed() {
        if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        mScreenHeight = mPosterImageView.getHeight();
        setUpPeekArea();
        mCarouselView.getLayoutParams().height = mScreenHeight - mBottomSheetLayout.getHeight() + mPeekHeight;
        mCarouselView.setTranslationY(mScreenHeight);
    }

    private void initUiComponents() {

        mBottomSheetLayout = findViewById(R.id.bottom_sheet);
        mPosterImageView = findViewById(R.id.giv_poster);
        mUpIndicatorImageView = findViewById(R.id.iv_up_indicator);
        mCarouselView = findViewById(R.id.cv_movie_images);

        setUpToolbar();
        setUpBottomSheet();
        setUpUpIndicator();

        populateMovieInfo();
    }

    private void setUpPeekArea() {
        final LinearLayout peekLinearLayout = findViewById(R.id.ll_peek);
        mPeekHeight = peekLinearLayout.getHeight();
        mBottomSheetBehavior.setPeekHeight(mPeekHeight);
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

    private void setUpBottomSheet() {

        mBottomSheetBehavior = BottomSheetBehavior.from(mBottomSheetLayout);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_DRAGGING:
                        mUpIndicatorImageView.animate().scaleX(0).scaleY(0)
                                .setDuration(DEFAULT_ANIMATION_DURATION).start();
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        mUpIndicatorImageView.animate().scaleX(1).scaleY(1)
                                .setDuration(DEFAULT_ANIMATION_DURATION).start();
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                mCarouselView.setTranslationY((1 - slideOffset) * mScreenHeight);
            }
        });
    }

    private void setUpUpIndicator() {
        final Animation animation = new AlphaAnimation(1, 0);
        animation.setDuration(UP_INDICATOR_BLINK_DURATION);
        animation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
        animation.setRepeatCount(Animation.INFINITE);
        animation.setRepeatMode(Animation.REVERSE);
        mUpIndicatorImageView.startAnimation(animation);
    }

    private void populateMovieInfo() {
        // TODO: 03/12/2017 convert to parcelable
        Movie movie = (Movie) getIntent().getSerializableExtra(ARGS_MOVIE_DETAILS);
        if (movie == null) {
            // TODO: 05/02/2018 show the error and finish, instead of throwing exception
            throw new IllegalArgumentException("Movie cannot be null");
        }

        setUpMovieInfo(movie);
        setUpAverageVoteIndicator(movie.getAverageVote(), movie.getVoteCount());
        setUpFullMovieInfo(movie.getId());
    }

    private void setUpMovieInfo(final Movie movie) {
        // TODO: 05/02/2018 custom view should be ImageView, instead of FrameLayout
        // poster
        String posterUrl = TmdbUtils.getImageUrl(movie.getPosterPath());
        mPosterImageView.setGradientCover(R.drawable.gradient_black_bottom);
        Glide.with(this).asBitmap().load(posterUrl).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                mPosterImageView.setImageBitmap(resource);
            }
        });

        TextView titleTextView = findViewById(R.id.tv_title);
        titleTextView.setText(movie.getTitle());

        TextView generalInfoTextView = findViewById(R.id.tv_general_info);
        generalInfoTextView.setText(MovieUtils.getFirstBilledCast(movie.getCast(), 2));

        TextView releaseDateTextView = findViewById(R.id.tv_release_date);
        releaseDateTextView.setText(TimeUtils.getDate(this, movie.getReleaseDate()));

        TextView overviewTextView = findViewById(R.id.tv_overview);
        overviewTextView.setText(movie.getOverview());

        // TODO: 2/7/18 what if user doesn't have internet access at the meantime
        // backdrop
        mCarouselView.setImageListener(new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                String url = TmdbUtils.getImageUrl(movie.getBackdropPath());
                Glide.with(MovieDetailsActivity.this).load(url).into(imageView);
            }
        });
        mCarouselView.setPageCount(1);
    }

    private void setUpAverageVoteIndicator(float averageVote, int voteCount) {

        CircularRatingsBar averageVoteCRB = findViewById(R.id.crb_average_vote);

        final TextView voteCountTextView = findViewById(R.id.tv_vote_count);
        voteCountTextView.setText(String.valueOf(FormatUtils.formatNumber(voteCount, "###,###,###")));
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

    private void setUpFullMovieInfo(int movieId) {
        new PopulateMovieInfoTask(this)
                .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, movieId);
    }

    private static class PopulateMovieInfoTask extends AsyncTask<Integer, Void, FullMovie> {

        private WeakReference<Context> mContext;

        PopulateMovieInfoTask(Context context) {
            this.mContext = new WeakReference<>(context);
        }

        @Override
        protected FullMovie doInBackground(Integer... integers) {
            int movieId = integers[0];
            return TmdbUtils.findFullMovieById(movieId);
        }

        @Override
        protected void onPostExecute(FullMovie fullMovie) {
            super.onPostExecute(fullMovie);

            Context context = mContext.get();
            if (context != null && fullMovie != null) {
                populateBackdropsCarousel(context, fullMovie.getBackdrops());
                populateCastInfo(context, fullMovie.getCast());
                populateCrewInfo(context, fullMovie.getCrew());
                populateExtraInfo(context, fullMovie.getMovieExtra());
            }
        }

        private void populateBackdropsCarousel(@NonNull final Context context, final String[] paths) {
            final Activity activity = (Activity) context;
            final CarouselView carouselView = activity.findViewById(R.id.cv_movie_images);
            final Bitmap[] backdrops = new Bitmap[paths.length];

            HandlerThread handlerThread = new HandlerThread(TAG);
            handlerThread.start();
            new Handler(handlerThread.getLooper()).post(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < paths.length; i++) {
                        String url = TmdbUtils.getImageUrl(paths[i]);
                        try {
                            Bitmap bitmap = Glide.with(activity).asBitmap().load(url).submit().get();
                            backdrops[i] = Utils.darkenBitmap(bitmap);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                    }
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            carouselView.setImageListener(new ImageListener() {
                                @Override
                                public void setImageForPosition(int position, ImageView imageView) {
                                    imageView.setImageBitmap(backdrops[position]);
                                }
                            });
                            carouselView.setPageCount(backdrops.length);
                        }
                    });
                }
            });
        }

        private void populateCastInfo(@NonNull Context context, @NonNull ArrayList<Actor> cast) {
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

        private void populateCrewInfo(@NonNull Context context, @NonNull ArrayList<CrewMember> crew) {
            // TODO: 1/19/18 fix this mess
            StringBuilder directors = new StringBuilder(), writers = new StringBuilder();
            for (CrewMember person : crew) {
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

        private void populateExtraInfo(@NonNull Context context, @NonNull MovieExtra extra) {
            Activity activity = (Activity) context;

            // general info
            TextView generalInfoTextView = activity.findViewById(R.id.tv_general_info);
            String info = String.format("%s  %s  %s",
                    extra.getClassification(),
                    TimeUtils.getDuration(extra.getRuntime()),
                    TextUtils.join(", ", extra.getGenres())
            );
            generalInfoTextView.setText(info);

            // tagline
            TextView taglineTextView = activity.findViewById(R.id.tv_tagline);
            taglineTextView.setText(extra.getTagline());

            // production countries
            TextView prodCountriesTextView = activity.findViewById(R.id.tv_production_country);
            prodCountriesTextView.setText(TextUtils.join("\n", extra.getCountries()));

            // spoken languages
            TextView languagesTextView = activity.findViewById(R.id.tv_spoken_languages);
            languagesTextView.setText(TextUtils.join(", ", extra.getSpokeLanguages()));
        }
    }
}
