package com.ticket.validation.terminal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.FragmentActivity;

import com.ticket.validation.terminal.constant.Constants;

/**
 * Created by dengshengjin on 15/5/15.
 */
public abstract class BaseActivity extends FragmentActivity {
    private Context mContext;
    private Handler mHandler;
    protected FinishBroadcastReceiver finishReceiver;

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

        try {
            if (finishReceiver == null) {
                finishReceiver = new FinishBroadcastReceiver();
            }
            IntentFilter finishIntent = new IntentFilter();
            finishIntent.addAction(Constants.FINISH_ACTION_NAME);
            registerReceiver(finishReceiver, finishIntent);
        } catch (Exception e) {

        }
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
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        System.gc();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (finishReceiver != null) {
                unregisterReceiver(finishReceiver);
            }
        } catch (Exception e) {

        }
    }
    protected abstract void initData();

    protected abstract void initWidgets();

    protected abstract void initWidgetsActions();

    final class FinishBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constants.FINISH_ACTION_NAME)) {
                finish();
            }
        }
    }
}
