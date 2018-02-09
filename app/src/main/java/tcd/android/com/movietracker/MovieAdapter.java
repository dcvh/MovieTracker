package tcd.android.com.movietracker;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import tcd.android.com.movietracker.Entities.Movie;

import static tcd.android.com.movietracker.MovieDetailsActivity.ARGS_MOVIE_DETAILS;

/**
 * Created by ADMIN on 27/10/2017.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private static final String TAG = MovieAdapter.class.getSimpleName();

    private static final int TOTAL_FIRST_BILLED_ACTORS = 2;

    private ArrayList<Movie> mMovies;
    private Context mContext;

    public MovieAdapter(ArrayList<Movie> mMovies, Context mContext) {
        this.mMovies = mMovies;
        this.mContext = mContext;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list_item, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        final Movie movie = mMovies.get(position);

        String posterUrl = TmdbUtils.getImageUrl(movie.getPosterPath());
        Glide.with(mContext).load(posterUrl).into(holder.mPosterImageView);

        holder.mTitleTextView.setText(movie.getTitle());
        holder.mCastTextView.setText(Utils.getFirstBilledCast(movie.getCast(), TOTAL_FIRST_BILLED_ACTORS));

        holder.mItemCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent detailsIntent = new Intent(mContext, MovieDetailsActivity.class);
                detailsIntent.putExtra(ARGS_MOVIE_DETAILS, movie);
                mContext.startActivity(detailsIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {

        private CardView mItemCardView;
        private ImageView mPosterImageView;
        private TextView mTitleTextView;
        private TextView mCastTextView;

        MovieViewHolder(View itemView) {
            super(itemView);
            mItemCardView = itemView.findViewById(R.id.cv_movie_item);
            mPosterImageView = itemView.findViewById(R.id.iv_poster);
            mTitleTextView = itemView.findViewById(R.id.tv_title);
            mCastTextView = itemView.findViewById(R.id.tv_cast);
        }
    }
}
