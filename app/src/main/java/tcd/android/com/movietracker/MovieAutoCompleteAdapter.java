package tcd.android.com.movietracker;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import static java.io.File.separator;

/**
 * Created by ADMIN on 27/10/2017.
 */

public class MovieAutoCompleteAdapter extends RecyclerView.Adapter<MovieAutoCompleteAdapter.MovieViewHolder>
        implements Filterable {

    private static final String TAG = MovieAutoCompleteAdapter.class.getSimpleName();

    private static final String IMAGE_URL = "https://image.tmdb.org/t/p/";

    private ArrayList<Movie> mMovies;
    private Context mContext;

    public MovieAutoCompleteAdapter(ArrayList<Movie> mMovies, Context mContext) {
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
        Movie movie = mMovies.get(position);

        Glide.with(mContext).load(getImageUrl(movie.getPosterPath())).into(holder.mPosterImageView);
        holder.mTitleTextView.setText(movie.getTitle());
        holder.mCastTextView.setText(getMajorCasts(movie.getCasts()));
    }

    private String getImageUrl(String path) {
        return IMAGE_URL + "w500" + path;
    }

    private String getMajorCasts(Cast[] cast) {
        StringBuilder majorCasts = new StringBuilder();
        if (cast != null) {
            for (int i = 0; i < 2; i++) {
                majorCasts.append("");
            }
        }
        return majorCasts.toString();
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    @NonNull
    @Override
    public Filter getFilter() {
        // TODO: 27/10/2017 implement filter
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                return null;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

            }
        };
    }

    private ArrayList<Movie> findMovies(String movieTitle) {
        throw new UnsupportedOperationException("Not implemented");
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {

        private ImageView mPosterImageView;
        private TextView mTitleTextView;
        private TextView mCastTextView;

        MovieViewHolder(View itemView) {
            super(itemView);
            mPosterImageView = itemView.findViewById(R.id.iv_poster);
            mTitleTextView = itemView.findViewById(R.id.tv_title);
            mCastTextView = itemView.findViewById(R.id.tv_cast);
        }
    }
}
