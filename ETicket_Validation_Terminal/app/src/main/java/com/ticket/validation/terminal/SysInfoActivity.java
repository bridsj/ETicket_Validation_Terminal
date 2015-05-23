package com.ticket.validation.terminal;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by dengshengjin on 15/5/15.
 */
public class SysInfoActivity extends BaseActivity {
    private ImageView backImg;
    private ViewGroup mBackBox;

    @Override
    protected void initData() {

    }

    @Override
    protected void initWidgets() {
        setContentView(R.layout.activity_sys_info);
        mBackBox = (ViewGroup) findViewById(R.id.back_box);
    }

    @Override
    protected void initWidgetsActions() {
        mBackBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
