package me.khrystal.libary;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 16/8/26
 * update time:
 * email: 723526676@qq.com
 */
public class OutsideScrollView extends ScrollView{

    private int mDownY;
    private int mMoveY;
    private CommonHeaderLayout mHeaderLayout;
    private OutsideDownLinearLayout mBodyLayout;

    public OutsideScrollView(Context context) {
        this(context, null);
    }

    public OutsideScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OutsideScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public OutsideScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        if (t >= mHeaderLayout.getMeasuredHeight() * 0.7
                && mBodyLayout.getCurrentState() == OutsideDownLinearLayout.DRAG_STATE_HIDE) {
            mHeaderLayout.setIsHide(true);
        } else {
            mHeaderLayout.setIsHide(false);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownY = (int) ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                mMoveY = (int) ev.getRawY();
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mDownY = 0;
                mMoveY = 0;
                break;
        }

        int offset = mDownY - mMoveY;
//        scroll up
        if (offset > 0)
            return super.onInterceptTouchEvent(ev);

        if (getScrollY() == 0 && (mBodyLayout.getCurrentState() == OutsideDownLinearLayout.DRAG_STATE_SHOW
                || mBodyLayout.getCurrentState() == OutsideDownLinearLayout.DRAG_STATE_MOVE)) {
            return false;
        }

        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mBodyLayout.getCurrentState() == OutsideDownLinearLayout.DRAG_STATE_HIDE)
            return false;
        return super.onTouchEvent(ev);
    }

    public void setOutSideDownLinearLayout(OutsideDownLinearLayout bodyLayout) {
        mBodyLayout = bodyLayout;
    }

    public void setCommonHeaderLayout(CommonHeaderLayout header) {
        mHeaderLayout = header;
    }
}
