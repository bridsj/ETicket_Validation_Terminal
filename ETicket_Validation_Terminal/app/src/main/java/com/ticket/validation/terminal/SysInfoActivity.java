package com.ticket.validation.terminal;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ticket.validation.terminal.db.CacheDBUtil;
import com.ticket.validation.terminal.model.ErrorModel;
import com.ticket.validation.terminal.model.SysInfoModel;
import com.ticket.validation.terminal.model.UpgradeModel;
import com.ticket.validation.terminal.parse.SysInfoParse;
import com.ticket.validation.terminal.parse.UpgradeParse;
import com.ticket.validation.terminal.restful.ApiConstants;
import com.ticket.validation.terminal.restful.ReqRestAdapter;
import com.ticket.validation.terminal.restful.RestfulRequest;
import com.ticket.validation.terminal.util.LoginInterceporUtil;
import com.ticket.validation.terminal.util.ToastUtil;
import com.zuiapps.suite.utils.app.AppUtil;
import com.zuiapps.suite.utils.string.StringUtils;

import org.json.JSONObject;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import download.AppDownloadManager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by dengshengjin on 15/5/15.
 */
public class SysInfoActivity extends BaseUserActivity {
    private ImageView backImg;
    private ViewGroup mBackBox;
    private TextView mUrlText, mIdText;
    private RestfulRequest mRestfulRequest;
    private Handler mHandler;
    private ViewGroup mUpgradeBox;
    private ProgressBar mProgressBar;
    private ExecutorService mExecutorService = Executors.newSingleThreadExecutor();

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
        super.initWidgets();
        mBackBox = (ViewGroup) findViewById(R.id.back_box);
        mUrlText = (TextView) findViewById(R.id.url_text);
        mIdText = (TextView) findViewById(R.id.id_text);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mProgressBar.setVisibility(View.GONE);
        mUpgradeBox = (ViewGroup) findViewById(R.id.upgrade_box);
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
        mUpgradeBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LoginInterceporUtil.pauseRedirect(getApplicationContext())) {
                    return;
                }
                if (mProgressBar.getVisibility() == View.VISIBLE) {
                    return;
                }
                mProgressBar.setVisibility(View.VISIBLE);
                String versionName = AppUtil.getVersionName(getApplicationContext());
                mRestfulRequest.checkUpdate(versionName, CacheDBUtil.getSessionId(getApplicationContext()), new Callback<JSONObject>() {

                    @Override
                    public void success(JSONObject jsonObject, Response response) {
                        Object object = UpgradeParse.parse(jsonObject);
                        mProgressBar.setVisibility(View.GONE);
                        if (object == null) {
                            ToastUtil.showToast(getApplicationContext(), R.string.loading_fail2);
                        } else {
                            if (object instanceof ErrorModel) {
                                ToastUtil.showToast(getApplicationContext(), ((ErrorModel) object).mInfo);
                            } else if (object instanceof UpgradeModel) {
                                final UpgradeModel upgradeModel = (UpgradeModel) object;
                                getHandler().post(new Runnable() {
                                    @Override
                                    public void run() {
                                        showUpgradeDialog(upgradeModel.mData);
                                    }
                                });
                            } else {
                                ToastUtil.showToast(getApplicationContext(), R.string.loading_fail);
                            }


                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        mProgressBar.setVisibility(View.GONE);
                        ToastUtil.showToast(getApplicationContext(), R.string.loading_fail2);
                    }
                });
            }
        });
        loadData();
    }

    private void showUpgradeDialog(final String downloadUrl) {
        new AlertDialog.Builder(SysInfoActivity.this, R.style.alert_dialog_style).setTitle(getApplicationContext().getString(R.string.warn_title))
                .setMessage(getApplicationContext().getString(R.string.warn_content_str)).setNegativeButton(R.string.cancel_str, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

            }

        }).setPositiveButton(R.string.ok_str, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (LoginInterceporUtil.pauseRedirect(getApplicationContext())) {
                    return;
                }
                String realDownloadUrl = downloadUrl + "?sessionId=" + CacheDBUtil.getSessionId(getApplicationContext()) + "&outId=" + System.currentTimeMillis() + "&device=2";
                AppDownloadManager.getInstance(getApplicationContext()).download(realDownloadUrl, AppUtil.getAppName(getApplicationContext()));
            }
        }).create().show();
    }

    private void loadData() {
        mExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    String jsonData = StringUtils.inputSteamToString(getAssets().open("default_system_info.txt"), "utf-8");
                    final SysInfoModel model = SysInfoParse.parse(jsonData);
                    if (model != null) {
                        getHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                mUrlText.setText(String.format(getString(R.string.sys_info_address), model.mInfo));
                                mIdText.setText(String.format(getString(R.string.sys_info_id), model.mData));
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
