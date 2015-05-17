package com.ticket.validation.terminal;

import android.view.View;
import android.widget.ImageView;

/**
 * Created by dengshengjin on 15/5/15.
 */
public class SysInfoActivity extends BaseActivity {
    private ImageView backImg;

    @Override
    protected void initData() {

    }

    @Override
    protected void initWidgets() {
        setContentView(R.layout.activity_sys_info);
        backImg = (ImageView) findViewById(R.id.back_img);
    }

    @Override
    protected void initWidgetsActions() {
        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
