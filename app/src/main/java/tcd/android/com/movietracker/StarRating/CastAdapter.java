package tcd.android.com.movietracker.StarRating;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import tcd.android.com.movietracker.Entities.Credit.Actor;
import tcd.android.com.movietracker.R;
import tcd.android.com.movietracker.TmdbUtils;

/**
 * Created by cpu10661 on 1/18/18.
 */

public class CastAdapter extends RecyclerView.Adapter<CastAdapter.ActorHolder> {

    private ArrayList<Actor> mCast;
    private RequestManager mRequestManager;

    public CastAdapter(ArrayList<Actor> mCast, RequestManager manager) {
        this.mCast = mCast;

        RequestOptions options = new RequestOptions().placeholder(R.drawable.placeholder_actor);
        mRequestManager = manager.applyDefaultRequestOptions(options);
    }

    @Override
    public ActorHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.actor_row_item, parent, false);
        return new ActorHolder(view);
    }

    @Override
    public void onBindViewHolder(ActorHolder holder, int position) {
        Actor actor = mCast.get(position);

        String profilePictureUrl = TmdbUtils.getImageUrl(actor.getProfilePath());
        mRequestManager.load(profilePictureUrl).into(holder.mPictureImageView);

        holder.mNameTextView.setText(actor.getName());
        holder.mCharacterTextView.setText(actor.getCharacter());
    }

    @Override
    public int getItemCount() {
        return mCast.size();
    }

    @Override
    public long getItemId(int position) {
        return mCast.get(position).getId();
    }

    class ActorHolder extends RecyclerView.ViewHolder {

        ImageView mPictureImageView;
        TextView mNameTextView;
        TextView mCharacterTextView;

        ActorHolder(View itemView) {
            super(itemView);
            mPictureImageView = itemView.findViewById(R.id.iv_profile_picture);
            mNameTextView = itemView.findViewById(R.id.tv_name);
            mCharacterTextView = itemView.findViewById(R.id.tv_character);
        }
    }
}
