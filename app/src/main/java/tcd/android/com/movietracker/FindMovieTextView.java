package tcd.android.com.movietracker;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;

import java.util.concurrent.TimeUnit;

/**
 * Created by ADMIN on 05/11/2017.
 */

public class FindMovieTextView extends android.support.v7.widget.AppCompatAutoCompleteTextView {

    private static final int MESSAGE_TEXT_CHANGED = 100;
    private static final int AUTO_COMPLETE_DELAYED_TIME = (int) TimeUnit.SECONDS.toMillis(1);

    private final Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            FindMovieTextView.super.performFiltering((CharSequence)message.obj, message.arg1);
            return true;
        }
    });

    public FindMovieTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void performFiltering(CharSequence text, int keyCode) {
        mHandler.removeMessages(MESSAGE_TEXT_CHANGED);
        mHandler.sendMessageDelayed(mHandler.obtainMessage(MESSAGE_TEXT_CHANGED, text), AUTO_COMPLETE_DELAYED_TIME);
    }

    @Override
    public void onFilterComplete(int count) {
        super.onFilterComplete(count);
    }
}
