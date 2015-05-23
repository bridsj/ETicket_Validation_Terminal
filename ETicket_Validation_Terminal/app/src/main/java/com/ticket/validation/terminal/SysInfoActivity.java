package com.ticket.validation.terminal;

import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ticket.validation.terminal.db.CacheDBUtil;
import com.ticket.validation.terminal.model.UserModel;
import com.ticket.validation.terminal.parse.UserParse;
import com.ticket.validation.terminal.restful.ApiConstants;
import com.ticket.validation.terminal.restful.ReqRestAdapter;
import com.ticket.validation.terminal.restful.RestfulRequest;

import org.json.JSONObject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by dengshengjin on 15/5/15.
 */
public class SysInfoActivity extends BaseActivity {
    private ImageView backImg;
    private ViewGroup mBackBox;
    private TextView mUrlText, mIdText;
    private RestfulRequest mRestfulRequest;
    private Handler mHandler;

    @Override
    protected void initData() {
        mRestfulRequest = ReqRestAdapter.getInstance(getApplicationContext(), ApiConstants.API_BASE_URL).create(RestfulRequest.class);
    }

    public Handler getHandler() {
        if (mHandler == null) {
            mHandler = new Handler(Looper.getMainLooper());
        }
        return mHandler;
    }

    @Override
    protected void initWidgets() {
        setContentView(R.layout.activity_sys_info);
        mBackBox = (ViewGroup) findViewById(R.id.back_box);
        mUrlText = (TextView) findViewById(R.id.url_text);
        mIdText = (TextView) findViewById(R.id.id_text);
        mUrlText.setText(String.format(getString(R.string.sys_info_address), CacheDBUtil.getAppUrl(getApplicationContext())));
        mIdText.setText(String.format(getString(R.string.sys_info_id), CacheDBUtil.getName(getApplicationContext())));
    }

    @Override
    protected void initWidgetsActions() {
        mBackBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadData();
            }
        }, 300);
    }

    private void loadData() {

        mRestfulRequest.updateConfigJson(CacheDBUtil.getSessionId(getApplicationContext()), new Callback<JSONObject>() {

            @Override
            public void success(final JSONObject jsonObject, Response response) {
                final UserModel model = UserParse.parseUser(jsonObject);
                if (model != null) {
                    CacheDBUtil.saveAppUrl(getApplicationContext(), model.mUrl);
                    CacheDBUtil.saveName(getApplicationContext(), model.mUser);
                    getHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            mUrlText.setText(String.format(getString(R.string.sys_info_address), model.mUrl));
                            mIdText.setText(String.format(getString(R.string.sys_info_id), model.mUser));
                        }
                    });
                }
            }

            @Override
            public void failure(RetrofitError error) {


            }
        });


    }
}
