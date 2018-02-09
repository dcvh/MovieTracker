package tcd.android.com.movietracker;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import ee.subscribe.gooeyloader.GooeyLoaderView;
import tcd.android.com.movietracker.Entities.Movie;
import tcd.android.com.movietracker.Utils.TmdbUtils;

public class WatchLaterFragment extends Fragment {

    private static final String TAG = WatchLaterFragment.class.getSimpleName();

    public static WatchLaterFragment newInstance() {
        Bundle args = new Bundle();
        WatchLaterFragment fragment = new WatchLaterFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_watch_later, container, false);
        return view;
    }
}
