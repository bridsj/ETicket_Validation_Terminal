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
import com.ticket.validation.terminal.model.ReportModel;
import com.ticket.validation.terminal.parse.ReportParse;
import com.ticket.validation.terminal.restful.ApiConstants;
import com.ticket.validation.terminal.restful.ReqRestAdapter;
import com.ticket.validation.terminal.restful.RestfulRequest;
import com.ticket.validation.terminal.util.DateUtil;

import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by dengshengjin on 15/5/22.
 */
public class ReportActivity extends BaseActivity {
    private ReportAdapter mAdapter;
    private Handler mHandler;
    private ListView mListView;
    private TextView mTitleText;
    private TextView mEmptyView;
    private ViewGroup mBackBox;
    private Executor mExecutor = Executors.newCachedThreadPool();
    private RestfulRequest mRestfulRequest;
    private TextView mDateText;
    private RelativeLayout mLoadingBox, mPrintBox;
    private ProgressBar mProgressBar;


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
        mTitleText = (TextView) findViewById(R.id.title_text);
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
                if (mAdapter.getCount() == 0) {
                    return;
                }
                mProgressBar.setVisibility(View.VISIBLE);
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

                        final List<ReportModel> list = ReportParse.parseLogin(jsonObject);
                        if (list == null) {
                            failure(null);
                        } else {
                            if (!list.isEmpty()) {
                                getHandler().post(new Runnable() {
                                    @Override
                                    public void run() {
                                        mListView.setEmptyView(mEmptyView);
                                        mListView.setVisibility(View.VISIBLE);
                                        mLoadingBox.setVisibility(View.GONE);
                                        mEmptyView.setText(getString(R.string.loading_empty_data));
                                        mAdapter.notifyDataSetChanged(list);
                                    }
                                });

                            }
                        }
                    }
                });
            }

            @Override
            public void failure(RetrofitError error) {
                getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        mListView.setEmptyView(mEmptyView);
                        mListView.setVisibility(View.GONE);
                        mEmptyView.setText(getString(R.string.loading_fail));
                        mLoadingBox.setVisibility(View.GONE);
                    }
                });

            }
        });


    }

}
