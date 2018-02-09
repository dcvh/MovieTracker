package tcd.android.com.movietracker;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.ArrayList;

import tcd.android.com.movietracker.Entities.Movie;
import tcd.android.com.movietracker.Utils.MovieUtils;
import tcd.android.com.movietracker.Utils.TmdbUtils;
import tcd.android.com.movietracker.Utils.Utils;

/**
 * Created by ADMIN on 27/10/2017.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private static final String TAG = MovieAdapter.class.getSimpleName();

    private static final int TOTAL_FIRST_BILLED_ACTORS = 2;

    private ArrayList<Movie> mMovies;
    private Context mContext;

    public MovieAdapter(Context mContext, ArrayList<Movie> mMovies) {
        this.mMovies = mMovies;
        this.mContext = mContext;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list_item, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MovieViewHolder holder, int position) {
        final Movie movie = mMovies.get(position);

        String backdropUrl = TmdbUtils.getImageUrl(movie.getBackdropPath());
        Glide.with(mContext).asBitmap().load(backdropUrl).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    Bitmap bitmap = Utils.darkenBitmap(resource);
                    holder.mBackdropImageView.setImageBitmap(bitmap);
                }
            }
        });

        holder.mTitleTextView.setText(movie.getTitle());
        holder.mAverageVoteTextView.setText(String.valueOf(movie.getAverageVote()));
        String firstBilledCast = MovieUtils.getFirstBilledCast(movie.getCast(), TOTAL_FIRST_BILLED_ACTORS);
        holder.mCastTextView.setText(firstBilledCast);

        holder.mItemCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 2/9/18 launch movie details screen
            }
        });
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    @Override
    public long getItemId(int position) {
        return mMovies.get(position).getId();
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {

        private CardView mItemCardView;
        private TextView mTitleTextView;
        private TextView mCastTextView;
        private TextView mAverageVoteTextView;
        private ImageView mBackdropImageView;

        MovieViewHolder(View itemView) {
            super(itemView);
            mItemCardView = itemView.findViewById(R.id.cv_movie_item);
            mTitleTextView = itemView.findViewById(R.id.tv_title);
            mCastTextView = itemView.findViewById(R.id.tv_cast);
            mAverageVoteTextView = itemView.findViewById(R.id.tv_average_vote);
            mBackdropImageView = itemView.findViewById(R.id.iv_backdrop);
        }
    }
}
