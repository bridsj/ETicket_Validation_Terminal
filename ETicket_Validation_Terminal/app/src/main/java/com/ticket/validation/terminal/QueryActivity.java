package com.ticket.validation.terminal;

import android.content.Context;
import android.os.Handler;
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

import com.ticket.validation.terminal.adapter.KeyboardAdapter;
import com.ticket.validation.terminal.util.KeyCodeUtil;

/**
 * Created by dengshengjin on 15/5/16.
 */
public class QueryActivity extends BaseActivity {
    private EditText mEditText;
    private GridView mGridView;
    private KeyboardAdapter mKeyboardAdapter;
    private ImageView mClearImg;
    private ViewGroup mBackBox;
    private final static int DEL_CODE = 1000;
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

    @Override
    protected void initData() {
        mKeyboardAdapter = new KeyboardAdapter(getContext());
    }

    @Override
    protected void initWidgets() {
        setContentView(R.layout.activity_query);
        mEditText = (EditText) findViewById(R.id.edit_sys_text);
        mGridView = (GridView) findViewById(R.id.grid_view);
        mGridView.setAdapter(mKeyboardAdapter);
        mClearImg = (ImageView) findViewById(R.id.clear_img);
        mBackBox = (ViewGroup) findViewById(R.id.back_box);
    }

    @Override
    protected void initWidgetsActions() {
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        String key = KeyCodeUtil.getKeyCode(keyCode, false);
        if (!TextUtils.isEmpty(key)) {
            if (key.equals("del")) {
                onDelEvent();
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
