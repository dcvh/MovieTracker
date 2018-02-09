package tcd.android.com.movietracker;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.RawRes;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class GradientImageView extends FrameLayout {

    private static final String TAG = GradientImageView.class.getSimpleName();

    private int mColorStart, mColorEnd;
    private ImageView mImageView;
    private View mGradientCover;

    public GradientImageView(Context context) {
        super(context);
        init(null, 0);
    }

    public GradientImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public GradientImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    private void init(@Nullable AttributeSet attrs, int defStyleAttr) {
        TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.GradientImageView, defStyleAttr, 0);
        try {
            // TODO: 2/7/18 make this works
            mColorStart = a.getInteger(R.styleable.GradientImageView_colorStart, 0);
            mColorEnd = a.getInteger(R.styleable.GradientImageView_colorEnd, 0);
        } finally {
            a.recycle();
        }

        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        mImageView = new ImageView(getContext());
        mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        mImageView.setLayoutParams(layoutParams);
        addView(mImageView);

        mGradientCover = new View(getContext());
        mGradientCover.setLayoutParams(layoutParams);
        addView(mGradientCover);
    }

    public void setGradientCover(@RawRes @DrawableRes int resId) {
        mGradientCover.setBackgroundResource(resId);
    }

    public void setImageBitmap(final Bitmap bitmap) {
        mImageView.setImageBitmap(bitmap);
    }
}
