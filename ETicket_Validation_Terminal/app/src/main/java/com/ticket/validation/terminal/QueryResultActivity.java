package com.ticket.validation.terminal;

import android.view.View;
import android.view.ViewGroup;

import com.ticket.validation.terminal.model.TicketModel;

/**
 * Created by dengshengjin on 15/5/24.
 */
public class QueryResultActivity extends BaseActivity {
    public static final String MODEL = "model";
    private TicketModel mTicketModel;
    private ViewGroup mBackBox, mCloseBox;

    @Override
    protected void initData() {
        mTicketModel = (TicketModel) getIntent().getSerializableExtra(MODEL);
    }

    @Override
    protected void initWidgets() {
        setContentView(R.layout.activity_query_result);
        mBackBox = (ViewGroup) findViewById(R.id.back_box);
        mCloseBox = (ViewGroup) findViewById(R.id.close_box);
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
}
