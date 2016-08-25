package me.khrystal.libary;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.Scroller;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 16/8/26
 * update time:
 * email: 723526676@qq.com
 */
public class CommonHeaderLayout extends FrameLayout{

    private Scroller mScroller;
    private boolean mHide;
    private OutSideScrollView mScorllView;


    public CommonHeaderLayout(Context context) {
        this(context, null);
    }

    public CommonHeaderLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommonHeaderLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }



    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CommonHeaderLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        mScroller = new Scroller(getContext());
    }

    public boolean isHide() {
        return mHide;
    }

    public void setIsHide(boolean hide) {
        mHide = hide;
    }
}
