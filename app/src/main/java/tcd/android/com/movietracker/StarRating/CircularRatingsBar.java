package tcd.android.com.movietracker.StarRating;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import tcd.android.com.movietracker.R;

/**
 * Created by cpu10661 on 1/16/18.
 * https://github.com/ylyc/circular_progress_bar/blob/master/library/src/main/java/com/lylc/widget/circularprogressbar/CircularProgressBar.java
 */

public class CircularRatingsBar extends View {

    private static final int STROKE_WIDTH = 10;

    private final RectF mCircleBounds = new RectF();
    private final Paint mBackgroundColorPaint = new Paint();
    private final Paint mArcPaint = new Paint();
    private final Paint mTextPaint = new Paint();

    private float mRatings;

    public CircularRatingsBar(Context context) {
        super(context);
        init(context, null, 0);
    }

    public CircularRatingsBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public CircularRatingsBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {

        int accentColor = ContextCompat.getColor(context, R.color.darkPrimaryColorAccent);

        TypedArray a = getContext().obtainStyledAttributes(attrs,
                R.styleable.CircularRatingsBar, defStyleAttr, 0);

        int progressColor = a.getColor(R.styleable.CircularRatingsBar_progressColor, accentColor);
        mArcPaint.setColor(progressColor);
        mRatings = a.getInt(R.styleable.CircularRatingsBar_ratings, 0);
        int strokeWidth = a.getInt(R.styleable.CircularRatingsBar_strokeWidth, STROKE_WIDTH);
        a.recycle();

        mArcPaint.setAntiAlias(true);
        mArcPaint.setStyle(Paint.Style.STROKE);
        mArcPaint.setStrokeWidth(strokeWidth);
        mArcPaint.setColor(accentColor);

        mBackgroundColorPaint.setAntiAlias(true);
        mBackgroundColorPaint.setColor(Color.DKGRAY);
        mBackgroundColorPaint.setStyle(Paint.Style.STROKE);
        mBackgroundColorPaint.setStrokeWidth(strokeWidth);

        // TODO: 1/16/18 do something with these hard-coded attributes
        mTextPaint.setColor(progressColor);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTypeface(Typeface.create("Roboto-Thin", Typeface.NORMAL));
        mTextPaint.setShadowLayer(0.1f, 0, 1, Color.GRAY);
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        final int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        final int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        final int min = Math.min(width, height);
        setMeasuredDimension(min + 2 * STROKE_WIDTH, min + 2 * STROKE_WIDTH);

        mCircleBounds.set(STROKE_WIDTH, STROKE_WIDTH,
                min + STROKE_WIDTH, min + STROKE_WIDTH);
        mTextPaint.setTextSize(min / 3);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawArc(mCircleBounds, 0, 360, false, mBackgroundColorPaint);

        float scale = mRatings / 10f * 360;
        canvas.drawArc(mCircleBounds, 270, scale, false, mArcPaint);

        int xPos = (int) (getMeasuredWidth() / 2 -
                mTextPaint.measureText(String.valueOf(mRatings)) / 2);
        int yPos = (int) (getMeasuredHeight() / 2 -
                (mTextPaint.descent() + mTextPaint.ascent()) / 2);

        canvas.drawText(String.valueOf(mRatings), xPos, yPos, mTextPaint);

        super.onDraw(canvas);
    }

    public void setRatings(float ratings) {
        mRatings = ratings;
        invalidate();
    }

    public int getColor() {
        return mArcPaint.getColor();
    }
}
