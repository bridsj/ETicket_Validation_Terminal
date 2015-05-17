package com.ticket.validation.terminal.util;

import android.content.Context;
import android.graphics.PointF;
import android.os.Handler;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;


public class MutilClickUtil {

    private int mTargetTimes;
    private int mCurRepeatTimes;
    private ClickEvent mEvent;
    private ClickEvent mSingleClickEvent;
    private ClickEvent mLongClickEvent;

    private int mRepeatDelay;
    private int mRepeatTimeout;
    private int mLongPressTimeout;
    private float mTouchSlop;

    private Handler mHandler;

    private boolean mCanPerformClick;
    private boolean mCanPerformSingleClick;
    private boolean mCanPerformMutilClick;

    private PointF mCurPoint;
    private PointF mDownPoint;
    private PointF mLastClickPotint;
    private View mView;

    public MutilClickUtil(Context context, ClickEvent event, int targetTimes) {
        mEvent = event;
        mTargetTimes = targetTimes;
        mRepeatDelay = ViewConfiguration.getKeyRepeatDelay();
        mRepeatTimeout = ViewConfiguration.getDoubleTapTimeout();
        mLongPressTimeout = ViewConfiguration.getLongPressTimeout();
        mTouchSlop = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 25, context.getResources().getDisplayMetrics());

        mHandler = new Handler();
    }

    public void setOnSingleClickEvent(ClickEvent event) {
        mSingleClickEvent = event;
    }

    public void setOnLongClickEvent(ClickEvent event) {
        mLongClickEvent = event;
    }

    public void onEvent(View v, MotionEvent event) {
        mView = v;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                mDownPoint = new PointF(event.getX(), event.getY());
                mCanPerformClick = true;
                mCanPerformSingleClick = true;
                mHandler.postDelayed(mLongPressTask, mLongPressTimeout);
                break;

            case MotionEvent.ACTION_CANCEL:
                mCanPerformClick = false;
                mCanPerformSingleClick = false;
                mHandler.removeCallbacks(mLongPressTask);
                break;

            case MotionEvent.ACTION_UP:
                mHandler.removeCallbacks(mLongPressTask);
                if (mCanPerformSingleClick) {
                    mCurPoint = new PointF(event.getX(), event.getY());
                    if (samePoint(mDownPoint, mCurPoint)) {
                        onClick();
                    }
                }

                break;

            case MotionEvent.ACTION_MOVE:
                if (mCanPerformClick && !samePoint(mDownPoint.x, mDownPoint.y, event.getX(), event.getY())) {
                    mCanPerformClick = false;
                }

                break;

            default:
                break;
        }

    }


    private void onClick() {
        if (mCurRepeatTimes == 0) {
            mHandler.removeCallbacks(mDelayTask);
            mHandler.removeCallbacks(mTimeOutTask);

            mHandler.postDelayed(mDelayTask, mRepeatDelay);
            mHandler.postDelayed(mTimeOutTask, mRepeatTimeout);

            mLastClickPotint = mCurPoint;

        }
        mCurRepeatTimes++;
        if (mCurRepeatTimes == mTargetTimes && mCanPerformMutilClick) {
            boolean samePoint = true;
            if (mLastClickPotint != null && mCurPoint != null) {
                samePoint = samePoint(mCurPoint, mLastClickPotint);
            }
            if (samePoint) {
                mCurRepeatTimes = 0;
                mCurPoint = null;
                if (mEvent != null) {
                    mEvent.performClickEvent(mView, mDownPoint);
                }

            }

        }
    }

    private boolean samePoint(PointF f1, PointF f2) {
        double distance = Math.sqrt(Math.pow(f2.x - f1.x, 2) + Math.pow(f2.y - f1.y, 2));
        return distance < mTouchSlop;
    }

    private boolean samePoint(float x1, float y1, float x2, float y2) {
        double distance = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
        return distance < mTouchSlop;
    }

    private Runnable mLongPressTask = new Runnable() {

        @Override
        public void run() {
            mCanPerformSingleClick = false;
            if (mCanPerformClick && mLongClickEvent != null) {
                mLongClickEvent.performClickEvent(mView, mDownPoint);
            }
        }
    };

    private Runnable mDelayTask = new Runnable() {

        @Override
        public void run() {
            mCanPerformMutilClick = true;
        }
    };

    private Runnable mTimeOutTask = new Runnable() {

        @Override
        public void run() {
            mCanPerformMutilClick = false;
            boolean performSingleClick = mCurRepeatTimes == 1;
            mCurRepeatTimes = 0;
            mCurPoint = null;

            if (performSingleClick && mSingleClickEvent != null) {
                mSingleClickEvent.performClickEvent(mView, mDownPoint);
            }
        }
    };

    public interface ClickEvent {
        public void performClickEvent(View v, PointF eventPoint);
    }

}
