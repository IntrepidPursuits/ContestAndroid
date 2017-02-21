package io.intrepid.contest.screens.contestcreation;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class NonSwipingViewPager extends ViewPager {
    private static final boolean isPagingEnabled = false;

    public NonSwipingViewPager(Context context) {
        super(context);
    }

    public NonSwipingViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return isPagingEnabled;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return isPagingEnabled;
    }
}
