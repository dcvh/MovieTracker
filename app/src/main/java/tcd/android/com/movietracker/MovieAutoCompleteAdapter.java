package tcd.android.com.movietracker;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import tcd.android.com.movietracker.Entities.Movie;
import tcd.android.com.movietracker.Utils.MovieUtils;
import tcd.android.com.movietracker.Utils.TmdbUtils;

/**
 * Created by ADMIN on 30/10/2017.
 */

public class MovieAutoCompleteAdapter extends BaseAdapter implements Filterable {

    private static final String TAG = MovieAutoCompleteAdapter.class.getSimpleName();

    @NonNull
    private ArrayList<Movie> mMovies;
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    MovieAutoCompleteAdapter(@NonNull Context context) {
        mMovies = new ArrayList<>();
        mContext = context;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mMovies.size();
    }

    @Override
    public Movie getItem(int position) {
        return mMovies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mMovies.get(position).getId();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.movie_list_item, parent, false);
        }

        Movie movie = getItem(position);
        if (movie != null) {
            ImageView posterImageView = convertView.findViewById(R.id.iv_poster);
            String posterUrl = TmdbUtils.getImageUrl(movie.getPosterPath());
            Glide.with(parent.getContext()).load(posterUrl).into(posterImageView);

            ((TextView) convertView.findViewById(R.id.tv_title)).setText(movie.getTitle());
            ((TextView) convertView.findViewById(R.id.tv_cast))
                    .setText(MovieUtils.getFirstBilledCast(movie.getCast(), 2));
        }

        return convertView;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null && constraint.length() > 3) {
                    Log.d(TAG, "performFiltering: " + constraint.toString());
                    ArrayList<Movie> movies =
                            TmdbUtils.findMoviesByTitle(constraint.toString());
                    filterResults.values = movies;
                    filterResults.count = movies.size();
                }
                return filterResults;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                if (filterResults != null && filterResults.count > 0) {
                    mMovies = (ArrayList<Movie>) filterResults.values;
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
    }


}
