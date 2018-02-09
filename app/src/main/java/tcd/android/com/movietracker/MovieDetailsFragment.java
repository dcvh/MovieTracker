package tcd.android.com.movietracker;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
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

public class MovieDetailsFragment extends Fragment {

    public static final String ARGS_MOVIE_DETAILS = "argsMovieDetails";
    private static final String TAG = MovieDetailsFragment.class.getSimpleName();
    private static final int DEFAULT_ANIMATION_DURATION = 300;
    private static final int UP_INDICATOR_BLINK_DURATION = (int) TimeUnit.SECONDS.toMillis(1);

    private LinearLayout mBottomSheetLayout;
    private BottomSheetBehavior mBottomSheetBehavior;
    private GradientImageView mPosterImageView;
    private ImageView mUpIndicatorImageView;
    private CarouselView mCarouselView;

    private Movie mMovie;
    private int mPeekHeight = 300;
    private int mScreenHeight;

    public static MovieDetailsFragment newInstance(Movie movie) {
        Bundle args = new Bundle();
        args.putSerializable(ARGS_MOVIE_DETAILS, movie);
        MovieDetailsFragment fragment = new MovieDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mMovie = (Movie) getArguments().getSerializable(ARGS_MOVIE_DETAILS);
            if (mMovie == null) {
                // TODO: 2/8/18 come up with better solution instead of throwing exception
                throw new IllegalArgumentException("Movie cannot be null");
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movie_details, container, false);
    }

    @SuppressWarnings("SimplifiableIfStatement")
    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initUiComponents(view);

        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }

                mScreenHeight = mPosterImageView.getHeight();
                setUpPeekArea(view);
                setUpCarousel();
            }
        });

        // override on back pressed
        if (getView() != null) {
            getView().setFocusableInTouchMode(true);
            getView().requestFocus();
            getView().setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                        return onBackPressed();
                    }
                    return false;
                }
            });
        }

        setUpMovieInfo(view);
    }

    public boolean onBackPressed() {
        if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            return true;
        }
        return false;
    }

    private void initUiComponents(View view) {

        mBottomSheetLayout = view.findViewById(R.id.bottom_sheet);
        mPosterImageView = view.findViewById(R.id.giv_poster);
        mUpIndicatorImageView = view.findViewById(R.id.iv_up_indicator);
        mCarouselView = view.findViewById(R.id.cv_movie_images);

        setUpBottomSheet();
        setUpUpIndicator();

        populateMovieInfo(view);
    }

    private void setUpPeekArea(View view) {
        final LinearLayout peekLinearLayout = view.findViewById(R.id.ll_peek);
        mPeekHeight = peekLinearLayout.getHeight();
        mBottomSheetBehavior.setPeekHeight(mPeekHeight);
    }

    private void setUpCarousel() {
        mCarouselView.getLayoutParams().height =
                mScreenHeight - mBottomSheetLayout.getHeight() + mPeekHeight;
        mCarouselView.setTranslationY(mScreenHeight);
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

    private void populateMovieInfo(View view) {
        setUpMovieInfo(view);
        setUpAverageVoteIndicator(view);
        setUpFullMovieInfo(view);
    }

    private void setUpMovieInfo(View view) {
        // TODO: 05/02/2018 custom view should be ImageView, instead of FrameLayout
        // poster
        String posterUrl = TmdbUtils.getImageUrl(mMovie.getPosterPath());
        mPosterImageView.setGradientCover(R.drawable.gradient_black_bottom);
        Glide.with(this).asBitmap().load(posterUrl).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, Transition<? super Bitmap> transition) {
                mPosterImageView.setImageBitmap(resource);
            }
        });

        TextView titleTextView = view.findViewById(R.id.tv_title);
        titleTextView.setText(mMovie.getTitle());

        TextView generalInfoTextView = view.findViewById(R.id.tv_general_info);
        generalInfoTextView.setText(MovieUtils.getFirstBilledCast(mMovie.getCast(), 2));

        TextView releaseDateTextView = view.findViewById(R.id.tv_release_date);
        releaseDateTextView.setText(TimeUtils.getDate(view.getContext(), mMovie.getReleaseDate()));

        TextView overviewTextView = view.findViewById(R.id.tv_overview);
        overviewTextView.setText(mMovie.getOverview());

        // TODO: 2/7/18 what if user doesn't have internet access at the meantime
        // backdrop
        mCarouselView.setImageListener(new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                String url = TmdbUtils.getImageUrl(mMovie.getBackdropPath());
                Glide.with(MovieDetailsFragment.this).load(url).into(imageView);
            }
        });
        mCarouselView.setPageCount(1);
    }

    private void setUpAverageVoteIndicator(View view) {

        CircularRatingsBar averageVoteCRB = view.findViewById(R.id.crb_average_vote);

        final TextView voteCountTextView = view.findViewById(R.id.tv_vote_count);
        voteCountTextView.setText(String.valueOf(
                FormatUtils.formatNumber(mMovie.getVoteCount(), "###,###,###")));
        voteCountTextView.setTextColor(averageVoteCRB.getColor());

        averageVoteCRB.setRatings(mMovie.getAverageVote());
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

    private void setUpFullMovieInfo(View view) {
        new PopulateMovieInfoTask(view)
                .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mMovie.getId());
    }

    private static class PopulateMovieInfoTask extends AsyncTask<Integer, Void, FullMovie> {

        private WeakReference<View> mView;

        PopulateMovieInfoTask(View view) {
            mView = new WeakReference<>(view);
        }

        @Override
        protected FullMovie doInBackground(Integer... integers) {
            int movieId = integers[0];
            return TmdbUtils.findFullMovieById(movieId);
        }

        @Override
        protected void onPostExecute(FullMovie fullMovie) {
            super.onPostExecute(fullMovie);

            View view = mView.get();
            if (view != null && fullMovie != null) {
                populateBackdropsCarousel(view, fullMovie.getBackdrops());
                populateCastInfo(view, fullMovie.getCast());
                populateCrewInfo(view, fullMovie.getCrew());
                populateExtraInfo(view, fullMovie.getMovieExtra());
            }
        }

        private void populateBackdropsCarousel(@NonNull final View view, final String[] paths) {
            final CarouselView carouselView = view.findViewById(R.id.cv_movie_images);
            final Bitmap[] backdrops = new Bitmap[paths.length];

            HandlerThread handlerThread = new HandlerThread(TAG);
            handlerThread.start();
            new Handler(handlerThread.getLooper()).post(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < paths.length; i++) {
                        String url = TmdbUtils.getImageUrl(paths[i]);
                        try {
                            Bitmap bitmap = Glide.with(view).asBitmap().load(url).submit().get();
                            backdrops[i] = Utils.darkenBitmap(bitmap);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                    }
                    view.post(new Runnable() {
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

        private void populateCastInfo(@NonNull View view, @NonNull ArrayList<Actor> cast) {
            RecyclerView castRecyclerView = view.findViewById(R.id.rv_cast_crew);
            castRecyclerView.setItemAnimator(new DefaultItemAnimator());
            castRecyclerView.setHasFixedSize(true);

            RecyclerView.LayoutManager lm = new LinearLayoutManager(view.getContext(),
                    LinearLayoutManager.HORIZONTAL, false);
            castRecyclerView.setLayoutManager(lm);

            CastAdapter castAdapter = new CastAdapter(cast, Glide.with(view));
            castAdapter.setHasStableIds(true);
            castRecyclerView.setAdapter(castAdapter);
        }

        private void populateCrewInfo(@NonNull View view, @NonNull ArrayList<CrewMember> crew) {
            ArrayList<String> directors = new ArrayList<>(),
                    writers = new ArrayList<>();
            for (CrewMember person : crew) {
                if (person.getJob().equals("Director")) {
                    directors.add(person.getName());
                } else if (person.getDepartment().equals("Writing")) {
                    writers.add(person.getName());
                }
            }

            TextView directorTextView = view.findViewById(R.id.tv_director);
            directorTextView.setText(FormatUtils.commaJoin(directors));

            TextView writerTextView = view.findViewById(R.id.tv_writer);
            writerTextView.setText(FormatUtils.commaJoin(writers));
        }

        private void populateExtraInfo(@NonNull View view, @NonNull MovieExtra extra) {
            // general info
            TextView generalInfoTextView = view.findViewById(R.id.tv_general_info);
            String info = String.format("%s  %s  %s",
                    extra.getClassification(),
                    TimeUtils.getDuration(extra.getRuntime()),
                    FormatUtils.commaJoin(extra.getGenres())
            );
            generalInfoTextView.setText(info);

            // tagline
            TextView taglineTextView = view.findViewById(R.id.tv_tagline);
            taglineTextView.setText(extra.getTagline());

            // production countries
            TextView prodCountriesTextView = view.findViewById(R.id.tv_production_country);
            prodCountriesTextView.setText(TextUtils.join("\n", extra.getCountries()));

            // spoken languages
            TextView languagesTextView = view.findViewById(R.id.tv_spoken_languages);
            languagesTextView.setText(FormatUtils.commaJoin(extra.getSpokeLanguages()));
        }
    }
}
