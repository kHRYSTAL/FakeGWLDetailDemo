package me.khrystal.libary;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.Scroller;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 16/8/26
 * update time:
 * email: 723526676@qq.com
 */
public class OutsideDownLinearLayout extends LinearLayout{

    private Scroller mScroller;
    private CommonHeaderLayout mHeaderLayout;
    private RecyclerView mInsideRecyclerView;

    private float mLastY;
    private int mMoveY;
    private static int DRAG_Y_MAX = 220;

    public static final int DRAG_STATE_SHOW = 1;
    public static final int DRAG_STATE_HIDE = 2;
    public static final int DRAG_STATE_MOVE = 3;

    public int mCurrentState = DRAG_STATE_SHOW;

    public OutsideDownLinearLayout(Context context) {
        this(context, null);
    }

    public OutsideDownLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OutsideDownLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public OutsideDownLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        mScroller = new Scroller(getContext());
    }

    public void setCurrentState(int currentState) {
        mCurrentState = currentState;
    }

    public int getCurrentState() {
        return mCurrentState;
    }

    public void setCommonHeaderLayout(CommonHeaderLayout headerLayout) {
        mHeaderLayout = headerLayout;
    }

    public void setInsideRecyclerView(RecyclerView recyclerView) {
        mInsideRecyclerView = recyclerView;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mCurrentState == DRAG_STATE_HIDE) {
            return false;
        }
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                int moveY = (int) (mLastY - y);
//                from top to bottom  e.g. 0 - 100 getScrollY is -100
                if (getScrollY() <= 0) {
                    mCurrentState = DRAG_STATE_MOVE;
                    //step by step
                    this.scrollBy(0, moveY / 2);
                }
                mLastY = y;
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                process();
                break;
        }
        return true;
    }

    private void process() {
//        hide this layout
        if (-getScrollY() > DRAG_Y_MAX) {
            mCurrentState = DRAG_STATE_HIDE;
            mHeaderLayout.setVisibility(INVISIBLE);
//            this layout need move offset in order to hide
            mMoveY = Math.abs(-getMeasuredHeight() - getScrollY());
            mScroller.startScroll(0, getScrollY(), 0, -mMoveY, 1000);
        } else { //reset
            mCurrentState = DRAG_STATE_SHOW;
            mScroller.startScroll(0, getScrollY(), 0, -getScrollY(), Math.abs(getScrollY()) / 2);
        }

        invalidate();
    }

    public void showWithAnim() {
        mCurrentState = DRAG_STATE_SHOW;
        mHeaderLayout.setVisibility(VISIBLE);
        mScroller.startScroll(0, -mMoveY, 0, mMoveY, 1000);
//        when outsidescrollview is close recyclerview scroll to top
        postDelayed(new Runnable() {
            @Override public void run() {
                LinearLayoutManager manager = (LinearLayoutManager) mInsideRecyclerView.getLayoutManager();
                manager.scrollToPosition(0);
            }
        },1000);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            Log.d("Scroller", mScroller.getCurrY() + "");
            postInvalidate();
        }
    }
}
