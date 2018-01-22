package tcd.android.com.movietracker.StarRating;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by cpu10661 on 1/16/18.
 */

public class StarRatingsBar extends LinearLayout {

    private static final int NUMBER_OF_STARS = 10;

    public static final int NOT_RATED = 0;
    public static final int RATED = 1;
    @Retention(RetentionPolicy.SOURCE)
    @IntDef ({NOT_RATED, RATED})
    public @interface RATING_STATE {}

    private Context mContext;

    public StarRatingsBar(Context context) {
        super(context);
        init(context, null, 0);
    }

    public StarRatingsBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public StarRatingsBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        setOrientation(HORIZONTAL);
        mContext = context;

        setWeightSum(15);
        LayoutParams lp = new LayoutParams(0, WRAP_CONTENT, 1);
        for (int i = 0; i < NUMBER_OF_STARS; i++) {
            ImageView starButton = new ImageView(mContext);
            starButton.setLayoutParams(lp);
            addView(starButton);
        }
    }
}
