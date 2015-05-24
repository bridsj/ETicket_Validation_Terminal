package com.ticket.validation.terminal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.ticket.validation.terminal.db.CacheDBUtil;

/**
 * Created by dengshengjin on 15/5/23.
 */
public abstract class BaseUserActivity extends BaseActivity {
    private TextView mUserText;
    private UserBroadcastReceiver mInstalledBroadcastReceiver;
    public static final String USER_BROADCASTRECEIVER = "USER_BROADCASTRECEIVER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerReceiver();
    }


    @Override
    protected void initWidgets() {
        mUserText = (TextView) findViewById(R.id.user_text);
        updateUserInfo();
    }

    private void updateUserInfo() {
        if (!TextUtils.isEmpty(CacheDBUtil.getUserName(getApplicationContext()))) {
            mUserText.setText(String.format(getString(R.string.login_user_info), CacheDBUtil.getUserName(getApplicationContext())));
        } else {
            mUserText.setText(String.format(getString(R.string.login_user_info), getString(R.string.login_fail)));
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver();
    }

    private void registerReceiver() {
        if (mInstalledBroadcastReceiver == null) {
            mInstalledBroadcastReceiver = new UserBroadcastReceiver();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(USER_BROADCASTRECEIVER);
            registerReceiver(mInstalledBroadcastReceiver, intentFilter);
        }

    }

    private void unregisterReceiver() {
        try {
            if (mInstalledBroadcastReceiver != null) {
                unregisterReceiver(mInstalledBroadcastReceiver);
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public class UserBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && intent.getAction().equals(USER_BROADCASTRECEIVER)) {
                updateUserInfo();
            }
        }
    }
}
