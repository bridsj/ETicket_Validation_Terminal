package com.ticket.validation.terminal;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.ticket.validation.terminal.constant.Constants;
import com.ticket.validation.terminal.helper.SessionHelper;
import com.ticket.validation.terminal.util.LoginInterceporUtil;
import com.ticket.validation.terminal.util.ToastUtil;


/**
 * Created by dengshengjin on 15/5/13.
 */
public class MainActivity extends BaseUserActivity {
    private TextView mSysInfoText, mSysExitText, mValidationTicketText, mQueryTicketText, mReportText;

    @Override
    protected void initData() {

    }

    @Override
    protected void initWidgets() {
        setContentView(R.layout.activity_main);
        super.initWidgets();
        mSysInfoText = (TextView) findViewById(R.id.sys_info_text);
        mSysExitText = (TextView) findViewById(R.id.sys_exit_text);
        mValidationTicketText = (TextView) findViewById(R.id.validation_ticket_text);
        mQueryTicketText = (TextView) findViewById(R.id.query_ticket_text);
        mReportText = (TextView) findViewById(R.id.report_text);
    }

    @Override
    protected void initWidgetsActions() {
        mSysInfoText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LoginInterceporUtil.pauseRedirect(getApplicationContext())) {
                    return;
                }
                Intent intent = new Intent(MainActivity.this, SysInfoActivity.class);
                startActivity(intent);
            }
        });
        mSysExitText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LoginInterceporUtil.pauseRedirect(getApplicationContext())) {
                    return;
                }
                Intent intent = new Intent(MainActivity.this, ExitSysActivity.class);
                startActivity(intent);
            }
        });
        mValidationTicketText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LoginInterceporUtil.pauseRedirect(getApplicationContext())) {
                    return;
                }
                Intent intent = new Intent(MainActivity.this, ValidationTicketActivity.class);
                startActivity(intent);
            }
        });
        mQueryTicketText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
//                startActivity(intent);
                if (LoginInterceporUtil.pauseRedirect(getApplicationContext())) {
                    return;
                }
                Intent intent = new Intent(MainActivity.this, QueryActivity.class);
                startActivity(intent);
            }
        });
        mReportText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LoginInterceporUtil.pauseRedirect(getApplicationContext())) {
                    return;
                }
                Intent intent = new Intent(MainActivity.this, ReportActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void finish() {
        SessionHelper.getInstance(getApplicationContext()).closeSession();
        super.finish();
    }

    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - exitTime > 2000) {// 如果两次按键时间间隔大于800毫秒，则不退出
                ToastUtil.showToast(getApplicationContext(), getString(R.string.exit_warn));
                exitTime = System.currentTimeMillis();// 更新firstTime
                return true;
            } else {
                Intent intent = new Intent(Constants.FINISH_ACTION_NAME);
                sendBroadcast(intent);
                finish();
                getHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        android.os.Process.killProcess(android.os.Process.myPid());
                    }
                }, 1000);
            }
        }

        return super.onKeyDown(keyCode, event);
    }
}
