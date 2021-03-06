package com.ticket.validation.terminal;

import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ticket.validation.terminal.adapter.ReportAdapter;
import com.ticket.validation.terminal.db.CacheDBUtil;
import com.ticket.validation.terminal.helper.PrintHelper;
import com.ticket.validation.terminal.helper.PrintStrategy;
import com.ticket.validation.terminal.model.ErrorModel;
import com.ticket.validation.terminal.model.ReportPrintModel;
import com.ticket.validation.terminal.parse.ReportParse;
import com.ticket.validation.terminal.restful.ApiConstants;
import com.ticket.validation.terminal.restful.ReqRestAdapter;
import com.ticket.validation.terminal.restful.RestfulRequest;
import com.ticket.validation.terminal.util.DateUtil;
import com.ticket.validation.terminal.util.LoginInterceporUtil;
import com.ticket.validation.terminal.util.ToastUtil;

import org.json.JSONObject;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by dengshengjin on 15/5/22.
 */
public class ReportActivity extends BaseUserActivity {
    private ReportAdapter mAdapter;
    private Handler mHandler;
    private ListView mListView;
    private TextView mEmptyView;
    private ViewGroup mBackBox;
    private Executor mExecutor = Executors.newCachedThreadPool();
    private RestfulRequest mRestfulRequest;
    private TextView mDateText;
    private RelativeLayout mLoadingBox, mPrintBox;
    private ProgressBar mProgressBar;
    private ReportPrintModel mReportPrintModel;


    @Override
    protected void initData() {
        mAdapter = new ReportAdapter(getApplicationContext());
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
        setContentView(R.layout.activity_report);
        super.initWidgets();
        mListView = (ListView) findViewById(R.id.list_view);
        mEmptyView = (TextView) findViewById(R.id.empty_view);
        mBackBox = (ViewGroup) findViewById(R.id.back_box);
        mPrintBox = (RelativeLayout) findViewById(R.id.print_box);
        mDateText = (TextView) findViewById(R.id.date_text);
        mDateText.setText(String.format(getString(R.string.date), DateUtil.getFormatTime(System.currentTimeMillis())));
        mLoadingBox = (RelativeLayout) findViewById(R.id.loading_data_box);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mListView.setAdapter(mAdapter);
        mEmptyView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);
        mLoadingBox.setVisibility(View.GONE);
        mListView.setVisibility(View.GONE);
    }

    @Override
    protected void initWidgetsActions() {
        mBackBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mPrintBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LoginInterceporUtil.pauseRedirect(getApplicationContext())) {
                    return;
                }
                if (mAdapter.getCount() == 0) {
                    return;
                }
                if (mReportPrintModel == null) {
                    return;
                }
                if (mProgressBar.getVisibility() == View.VISIBLE) {
                    return;
                }
                mProgressBar.setVisibility(View.VISIBLE);
                PrintHelper.getInstance(getApplicationContext()).startPrintViaChar(mReportPrintModel.mPrintStr, new PrintStrategy.PrintCallback() {
                    @Override
                    public void onErrorPrint() {
                        ToastUtil.showToast(getApplicationContext(), R.string.printing);
                    }

                    @Override
                    public void onStartPrint() {

                    }

                    @Override
                    public void onFinishPrint() {
                        mProgressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailPrint(String errorMsg) {
                        ToastUtil.showToast(getApplicationContext(), getString(R.string.print_fail) + errorMsg);
                        mProgressBar.setVisibility(View.GONE);
                    }
                });
            }
        });
        getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mLoadingBox.setVisibility(View.VISIBLE);
                loadData();
            }
        }, 100);
    }

    private void loadData() {

        mRestfulRequest.dailyreportJson(CacheDBUtil.getSessionId(getApplicationContext()), new Callback<JSONObject>() {

            @Override
            public void success(final JSONObject jsonObject, Response response) {
                mExecutor.execute(new Runnable() {
                    @Override
                    public void run() {

                        Object object = ReportParse.parse(getApplicationContext(), jsonObject);
                        if (isFinishing()) {
                            return;
                        }
                        if (object == null) {
                            failureWarn(getString(R.string.loading_fail));
                        } else {
                            if (object instanceof ErrorModel) {
                                failureWarn(((ErrorModel) object).mInfo);
                            } else if (object instanceof ReportPrintModel) {
                                mReportPrintModel = (ReportPrintModel) object;
                                getHandler().post(new Runnable() {
                                    @Override
                                    public void run() {
                                        mListView.setEmptyView(mEmptyView);
                                        mListView.setVisibility(View.VISIBLE);
                                        mLoadingBox.setVisibility(View.GONE);
                                        mEmptyView.setText(getString(R.string.loading_empty_data));
                                        mAdapter.notifyDataSetChanged(mReportPrintModel.mReportList);
                                    }
                                });
                            } else {
                                failureWarn(getString(R.string.loading_fail));
                            }


                        }
                    }
                });
            }

            @Override
            public void failure(RetrofitError error) {
                failureWarn(getString(R.string.loading_fail2));
            }
        });


    }

    private void failureWarn(final String warn) {
        getHandler().post(new Runnable() {
            @Override
            public void run() {
                mListView.setEmptyView(mEmptyView);
                mListView.setVisibility(View.GONE);
                mEmptyView.setText(warn);
                mLoadingBox.setVisibility(View.GONE);
            }
        });
    }

}
