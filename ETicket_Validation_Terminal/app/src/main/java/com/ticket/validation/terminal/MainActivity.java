package com.ticket.validation.terminal;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

/**
 * Created by dengshengjin on 15/5/13.
 */
public class MainActivity extends BaseActivity {
    private TextView mSysInfoText, mSysExitText, mValidationTicketText;

    @Override
    protected void initData() {

    }

    @Override
    protected void initWidgets() {
        setContentView(R.layout.activity_main);
        mSysInfoText = (TextView) findViewById(R.id.sys_info_text);
        mSysExitText = (TextView) findViewById(R.id.sys_exit_text);
        mValidationTicketText = (TextView) findViewById(R.id.validation_ticket_text);
    }

    @Override
    protected void initWidgetsActions() {
        mSysInfoText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SysInfoActivity.class);
                startActivity(intent);
            }
        });
        mSysExitText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ExitSysActivity.class);
                startActivity(intent);
            }
        });
        mValidationTicketText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ValidationTicketActivity.class);
                startActivity(intent);
            }
        });
    }
}
