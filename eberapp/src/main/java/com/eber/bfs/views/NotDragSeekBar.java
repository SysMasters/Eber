package com.eber.bfs.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.SeekBar;

/**
 * Created by Administrator on 2017/4/27.
 */
@SuppressLint("AppCompatCustomView")
public class NotDragSeekBar extends SeekBar {
    public NotDragSeekBar(Context context) {
        super(context);
    }

    public NotDragSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NotDragSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public synchronized void setProgress(int progress) {
        super.setProgress(progress);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }


}
