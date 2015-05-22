package com.ticket.validation.terminal;

import android.os.Handler;
import android.os.Looper;
import android.widget.ListView;
import android.widget.TextView;

import com.ticket.validation.terminal.adapter.ReportAdapter;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by dengshengjin on 15/5/22.
 */
public class ReportActivity extends BaseActivity {
    private ReportAdapter mAdapter;
    private Handler mHandler;
    private ListView mListView;
    private TextView mTitleText;
    private TextView mEmptyView;
    private Executor mExecutor = Executors.newCachedThreadPool();

    @Override
    protected void initData() {
        mAdapter = new ReportAdapter(getApplicationContext());
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
        mListView.setAdapter(mAdapter);
        mListView.setEmptyView(mEmptyView);
    }

    @Override
    protected void initWidgetsActions() {
        getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadData();
            }
        }, 100);
    }

    private void loadData() {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {

                getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.notifyDataSetChanged();
                    }
                });

            }
        });

    }
}
