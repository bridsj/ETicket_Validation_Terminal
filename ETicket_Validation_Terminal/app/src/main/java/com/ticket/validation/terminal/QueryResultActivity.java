package com.ticket.validation.terminal;

import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ticket.validation.terminal.adapter.TicketAdapter;
import com.ticket.validation.terminal.model.TicketModel;

import java.util.List;

/**
 * Created by dengshengjin on 15/5/24.
 */
public class QueryResultActivity extends BaseUserActivity {
    public static final String MODEL = "model";
    private ViewGroup mBackBox, mCloseBox;
    private Handler mHandler;
    private RelativeLayout mLoadingBox;
    private ProgressBar mProgressBar;
    private List<TicketModel> mTicketList;
    private ListView mListView;
    private TextView mEmptyView;
    private TicketAdapter mAdapter;

    @Override
    protected void initData() {
        mTicketList = (List<TicketModel>) getIntent().getSerializableExtra(MODEL);
        mAdapter = new TicketAdapter(getApplicationContext());
    }

    public Handler getHandler() {
        if (mHandler == null) {
            mHandler = new Handler(Looper.getMainLooper());
        }
        return mHandler;
    }

    @Override
    protected void initWidgets() {
        setContentView(R.layout.activity_query_result);
        super.initWidgets();
        mBackBox = (ViewGroup) findViewById(R.id.back_box);
        mCloseBox = (ViewGroup) findViewById(R.id.close_box);

        mListView = (ListView) findViewById(R.id.list_view);
        mEmptyView = (TextView) findViewById(R.id.empty_view);
        mBackBox = (ViewGroup) findViewById(R.id.back_box);
        mLoadingBox = (RelativeLayout) findViewById(R.id.loading_data_box);
        mListView.setAdapter(mAdapter);
        mEmptyView.setVisibility(View.GONE);
        mLoadingBox.setVisibility(View.GONE);
        mListView.setVisibility(View.GONE);
        getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadData();
            }
        }, 100);
    }

    @Override
    protected void initWidgetsActions() {
        mBackBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mCloseBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void loadData() {

        getHandler().post(new Runnable() {
            @Override
            public void run() {
                mListView.setEmptyView(mEmptyView);
                mListView.setVisibility(View.VISIBLE);
                mLoadingBox.setVisibility(View.GONE);
                mEmptyView.setText(getString(R.string.loading_empty_data));
                mAdapter.notifyDataSetChanged(mTicketList);
            }
        });


    }


}
