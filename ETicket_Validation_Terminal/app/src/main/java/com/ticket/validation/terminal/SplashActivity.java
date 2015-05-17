package com.ticket.validation.terminal;

import android.content.Intent;
import android.os.Handler;

/**
 * Created by dengshengjin on 15/5/15.
 */
public class SplashActivity extends BaseActivity {
    private final int GO_MAIN = -100;
    private final int GO_FINISH = -102;
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case GO_MAIN:
                    if (isFinishing()) {
                        return;
                    }
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                    sendEmptyMessageDelayed(GO_FINISH, 1000);
                    break;
                case GO_FINISH:
                    if (isFinishing()) {
                        return;
                    }
                    finish();
                    break;
                default:
                    break;
            }
        }

        ;
    };

    @Override
    protected void initData() {

    }

    @Override
    protected void initWidgets() {
        setContentView(R.layout.activity_splash);
        mHandler.sendEmptyMessageDelayed(GO_MAIN, 2000);
    }

    @Override
    protected void initWidgetsActions() {

    }
}
