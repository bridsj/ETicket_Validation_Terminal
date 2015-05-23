package com.ticket.validation.terminal;

import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.ticket.validation.terminal.adapter.ValidationResultAdapter;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by dengshengjin on 15/5/23.
 */
public class ValidationResultActivity extends BaseUserActivity {
    private ViewGroup mBackBox;
    private ListView mListView;
    private ValidationResultAdapter mAdapter;
    private Handler mHandler;
    private TextView mEmptyView;
    private Executor mExecutor = Executors.newCachedThreadPool();

    @Override
    protected void initData() {
        mAdapter = new ValidationResultAdapter(getContext());
    }

    public Handler getHandler() {
        if (mHandler == null) {
            mHandler = new Handler(Looper.getMainLooper());
        }
        return mHandler;
    }

    @Override
    protected void initWidgets() {
        setContentView(R.layout.activity_validation_result);
        super.initWidgets();
        mListView = (ListView) findViewById(R.id.list_view);
        mEmptyView = (TextView) findViewById(R.id.empty_view);
        mBackBox = (ViewGroup) findViewById(R.id.back_box);
        mListView.setAdapter(mAdapter);
        mListView.setEmptyView(mEmptyView);
    }

    @Override
    protected void initWidgetsActions() {
        mBackBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
