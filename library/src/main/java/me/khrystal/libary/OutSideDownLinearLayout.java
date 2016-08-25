package me.khrystal.libary;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.Scroller;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 16/8/26
 * update time:
 * email: 723526676@qq.com
 */
public class OutSideDownLinearLayout extends LinearLayout{

    private Scroller mScroller;
    private CommonHeaderLayout mScrollRootHeader;

    public OutSideDownLinearLayout(Context context) {
        super(context);
    }

    public OutSideDownLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OutSideDownLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public OutSideDownLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
