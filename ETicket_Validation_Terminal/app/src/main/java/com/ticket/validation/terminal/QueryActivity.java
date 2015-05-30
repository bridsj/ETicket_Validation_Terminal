package com.ticket.validation.terminal;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.ticket.validation.terminal.adapter.KeyboardAdapter;
import com.ticket.validation.terminal.db.CacheDBUtil;
import com.ticket.validation.terminal.model.ErrorModel;
import com.ticket.validation.terminal.model.ReportPrintModel;
import com.ticket.validation.terminal.parse.ReportParse;
import com.ticket.validation.terminal.restful.ReqRestAdapter;
import com.ticket.validation.terminal.restful.RestfulRequest;
import com.ticket.validation.terminal.util.KeyCodeUtil;
import com.ticket.validation.terminal.util.LoginInterceporUtil;
import com.ticket.validation.terminal.util.ToastUtil;

import org.json.JSONObject;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by dengshengjin on 15/5/16.
 */
public class QueryActivity extends BaseUserActivity {
    private EditText mEditText;
    private GridView mGridView;
    private KeyboardAdapter mKeyboardAdapter;
    private ImageView mClearImg;
    private ViewGroup mBackBox;
    private final static int DEL_CODE = 1000;
    private ViewGroup mQueryBox;
    private ProgressBar mProgressBar;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case DEL_CODE:
                    onDelEvent();
                    mHandler.sendEmptyMessageDelayed(DEL_CODE, 100);
                    break;
            }
        }
    };

    protected RestfulRequest mRestfulRequest;
    protected ExecutorService mExecutorService = Executors.newSingleThreadExecutor();

    public Handler getHandler() {
        if (mHandler == null) {
            mHandler = new Handler(Looper.myLooper());
        }
        return mHandler;
    }

    @Override
    protected void initData() {
        mKeyboardAdapter = new KeyboardAdapter(getContext());
        mRestfulRequest = ReqRestAdapter.getInstance(getApplicationContext()).create(RestfulRequest.class);
    }

    @Override
    protected void initWidgets() {
        setContentView(R.layout.activity_query);
        super.initWidgets();
        mEditText = (EditText) findViewById(R.id.edit_sys_text);
        mGridView = (GridView) findViewById(R.id.grid_view);
        mGridView.setAdapter(mKeyboardAdapter);
        mClearImg = (ImageView) findViewById(R.id.clear_img);
        mBackBox = (ViewGroup) findViewById(R.id.back_box);
        mQueryBox = (ViewGroup) findViewById(R.id.query_box);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    protected void initWidgetsActions() {
        mQueryBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LoginInterceporUtil.pauseRedirect(getApplicationContext())) {
                    return;
                }
                String text = mEditText.getText().toString();
                if (TextUtils.isEmpty(text)) {
                    ToastUtil.showToast(getApplicationContext(), R.string.input_error);
                    return;
                }
                if (mProgressBar.getVisibility() == View.VISIBLE) {
                    return;
                }
                mProgressBar.setVisibility(View.VISIBLE);
                mRestfulRequest.dailyreportJson(CacheDBUtil.getSessionId(getApplicationContext()), new Callback<JSONObject>() {
                    @Override
                    public void success(final JSONObject jsonObject, Response response) {
                        mExecutorService.execute(new Runnable() {
                            @Override
                            public void run() {
                                final Object object = ReportParse.parse(getApplicationContext(), jsonObject);
                                if (isFinishing()) {
                                    return;
                                }
                                getHandler().post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (object == null) {
                                            mProgressBar.setVisibility(View.GONE);
                                            ToastUtil.showToast(getApplicationContext(), R.string.loading_fail2);
                                        } else {
                                            if (object instanceof ErrorModel) {
                                                mProgressBar.setVisibility(View.GONE);
                                                ToastUtil.showToast(getApplicationContext(), ((ErrorModel) object).mInfo);
                                            } else if (object instanceof ReportPrintModel) {
                                                mProgressBar.setVisibility(View.GONE);
                                                Intent intent = new Intent(QueryActivity.this, QueryResultActivity.class);
                                                startActivity(intent);
                                            } else {
                                                mProgressBar.setVisibility(View.GONE);
                                                ToastUtil.showToast(getApplicationContext(), R.string.loading_fail2);
                                            }
                                        }
                                    }
                                });

                            }
                        });
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        mProgressBar.setVisibility(View.GONE);
                        ToastUtil.showToast(getApplicationContext(), R.string.loading_fail2);
                    }
                });
            }
        });
        mEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
            }
        });
        mKeyboardAdapter.setOnItemClickListener(new KeyboardAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String str) {
                if (!str.equals("del")) {
                    String text = mEditText.getText().toString();
                    mEditText.setText(text + str);
                    mEditText.setSelection(text.length() + 1);
                } else {
                    onDelEvent();

                }
            }

            @Override
            public void onLongClick(String text) {
                mHandler.sendEmptyMessage(DEL_CODE);
            }

            @Override
            public void onTouch(MotionEvent event, String text) {
                if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                    mHandler.removeMessages(DEL_CODE);
                }
            }
        });
        mBackBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mClearImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditText.setText("");
            }
        });
    }

    private void onDelEvent() {
        Editable editable = mEditText.getText();
        int start = mEditText.getSelectionStart();
        if (editable != null && editable.length() > 0) {
            if (start > 0) {
                editable.delete(start - 1, start);
            }
        } else if (editable.length() == 0) {
            mEditText.setText("");
            mEditText.setSelection(0);
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (KeyCodeUtil.isEnterKeyCode(event)) {
            mQueryBox.performClick();
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        String key = KeyCodeUtil.getKeyCode(keyCode, event, false);
        if (!TextUtils.isEmpty(key)) {
            if (key.equals("del")) {
                onDelEvent();
            } else if (key.equals("enter")) {
                mQueryBox.performClick();
            } else {
                onAddEvent(key);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void onAddEvent(String key) {
        String text = mEditText.getText().toString();
        mEditText.setText(text + key);
        mEditText.setSelection(text.length() + 1);

    }
}
