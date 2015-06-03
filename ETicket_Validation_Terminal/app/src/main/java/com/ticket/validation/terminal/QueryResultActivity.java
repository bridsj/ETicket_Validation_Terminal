package com.ticket.validation.terminal;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ticket.validation.terminal.model.GoodsModel;

/**
 * Created by dengshengjin on 15/5/24.
 */
public class QueryResultActivity extends BaseUserActivity {
    public static final String MODEL = "model";
    private GoodsModel mGoodsModel;
    private ViewGroup mBackBox, mCloseBox;

    private TextView row1Title, row1Desc;
    private TextView row2Title, row2Desc;
    private TextView row3Title, row3Desc;
    private TextView row4Title, row4Desc;
    private TextView row5Title, row5Desc;
    private TextView row6Title, row6Desc;
    private TextView row7Title, row7Desc;
    private TextView row8Title, row8Desc;
    private TextView row9Title, row9Desc;

    @Override
    protected void initData() {
        mGoodsModel = (GoodsModel) getIntent().getSerializableExtra(MODEL);
    }

    @Override
    protected void initWidgets() {
        setContentView(R.layout.activity_query_result);
        super.initWidgets();
        mBackBox = (ViewGroup) findViewById(R.id.back_box);
        mCloseBox = (ViewGroup) findViewById(R.id.close_box);

        row1Title = (TextView) findViewById(R.id.row1_title);
        row1Desc = (TextView) findViewById(R.id.row1_desc);
        row1Title.setText(getString(R.string.goodsname));
        row1Desc.setText(mGoodsModel.mGoodsName);

        row2Title = (TextView) findViewById(R.id.row2_title);
        row2Desc = (TextView) findViewById(R.id.row2_desc);
        row2Title.setText(getString(R.string.endtime));
        row2Desc.setText(String.format(getResources().getString(R.string.endtime_desc), mGoodsModel.mStartTime, mGoodsModel.mEndTime));

        row3Title = (TextView) findViewById(R.id.row3_title);
        row3Desc = (TextView) findViewById(R.id.row3_desc);
        row3Title.setText(getString(R.string.acttime));
        row3Desc.setText(mGoodsModel.mActTime);

        row4Title = (TextView) findViewById(R.id.row4_title);
        row4Desc = (TextView) findViewById(R.id.row4_desc);
        row4Title.setText(getString(R.string.piececode));
        row4Desc.setText(mGoodsModel.mPiececode);

        row5Title = (TextView) findViewById(R.id.row5_title);
        row5Desc = (TextView) findViewById(R.id.row5_desc);
        row5Title.setText(getString(R.string.storagestatus));
        row5Desc.setText(mGoodsModel.mStorageStatus);

        row6Title = (TextView) findViewById(R.id.row6_title);
        row6Desc = (TextView) findViewById(R.id.row6_desc);
        row6Title.setText(getString(R.string.username));
        row6Desc.setText(mGoodsModel.mUserName);

        row7Title = (TextView) findViewById(R.id.row7_title);
        row7Desc = (TextView) findViewById(R.id.row7_desc);
        row7Title.setText(getString(R.string.idcard));
        row7Desc.setText(mGoodsModel.mIdCard);

        row8Title = (TextView) findViewById(R.id.row8_title);
        row8Desc = (TextView) findViewById(R.id.row8_desc);
        row8Title.setText(getString(R.string.ordercomments));
        row8Desc.setText(mGoodsModel.mOrderCommments);

        row9Title = (TextView) findViewById(R.id.row9_title);
        row9Desc = (TextView) findViewById(R.id.row9_desc);
        row9Title.setText(getString(R.string.status));
        row9Desc.setText(mGoodsModel.mStatus + "");
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
