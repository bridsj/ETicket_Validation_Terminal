package com.ticket.validation.terminal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.FragmentActivity;

/**
 * Created by dengshengjin on 15/5/15.
 */
public abstract class BaseActivity extends FragmentActivity {
    private Context mContext;
    private Handler mHandler;

    public Context getContext() {
        return mContext;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;

        initData();
        initWidgets();
        initWidgetsActions();
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.push_left_in, R.anim.no_anim);
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.push_left_in, R.anim.no_anim);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (isFinishAnim()) {
            overridePendingTransition(R.anim.no_anim, R.anim.push_right_out);
        }
    }

    @Override
    public void finish() {
        super.finish();
        if (isFinishAnim()) {
            overridePendingTransition(R.anim.no_anim, R.anim.push_right_out);
        }
    }

    protected boolean isFinishAnim() {
        return true;
    }

    public Handler getHandler() {
        if (mHandler == null) {
            synchronized (this) {
                if (mHandler == null) {
                    mHandler = new Handler(Looper.getMainLooper());
                }
            }
        }

        return mHandler;
    }

    protected abstract void initData();

    protected abstract void initWidgets();

    protected abstract void initWidgetsActions();
}
