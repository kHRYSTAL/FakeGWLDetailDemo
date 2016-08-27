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
    private OutsideScrollView mScorllView;


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

    public void setScrollView(OutsideScrollView scrollView) {
        mScorllView = scrollView;
    }

    public OutsideScrollView getScrollView() {
        return mScorllView;
    }

    public void showWithAnim() {
        if (!mHide) {
            return;
        }

        mScorllView.scrollTo(0, 0);
//      scroll y offset is 0 - (-getMeasuredHeight)
        scrollTo(0, getMeasuredHeight());
//      use Scroller delegate view's scrollTo() method callback computeScroll
        mScroller.startScroll(0, getMeasuredHeight(), 0, -getMeasuredHeight(), 1000);
        invalidate();
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
    }
}
