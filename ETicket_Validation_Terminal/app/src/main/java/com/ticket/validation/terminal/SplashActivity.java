package com.ticket.validation.terminal;

import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;

import com.ticket.validation.terminal.db.CacheDBUtil;
import com.ticket.validation.terminal.helper.SessionHelper;
import com.ticket.validation.terminal.model.LoginModel;
import com.ticket.validation.terminal.model.UserModel;
import com.ticket.validation.terminal.parse.UserParse;

import org.json.JSONObject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

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
        final SessionHelper mSessionHelper = SessionHelper.getInstance(getApplicationContext());
        mSessionHelper.loginUser(new Callback<JSONObject>() {

            @Override
            public void success(JSONObject jsonObject, Response response) {
                LoginModel model = UserParse.parseLogin(jsonObject);
                if (model != null && !TextUtils.isEmpty(model.mSession)) {
                    CacheDBUtil.saveSessionId(getApplicationContext(), model.mSession);
                    CacheDBUtil.saveUserName(getApplicationContext(), model.mUser);
                    Intent intent = new Intent(BaseUserActivity.USER_BROADCASTRECEIVER);
                    intent.setPackage(getPackageName());
                    sendBroadcast(intent);
                    mSessionHelper.updateConfigJson(new Callback<JSONObject>() {
                        @Override
                        public void success(JSONObject jsonObject, Response response) {
                            UserModel model = UserParse.parseUser(jsonObject);
                            if (model != null) {
                                CacheDBUtil.saveAppUrl(getApplicationContext(), model.mUrl);
                                CacheDBUtil.saveName(getApplicationContext(), model.mUser);
                            }
                        }

                        @Override
                        public void failure(RetrofitError error) {

                        }
                    });
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
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
