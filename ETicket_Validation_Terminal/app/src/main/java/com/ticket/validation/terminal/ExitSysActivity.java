package com.ticket.validation.terminal;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;

import com.ticket.validation.terminal.adapter.KeyboardAdapter;

/**
 * Created by dengshengjin on 15/5/16.
 */
public class ExitSysActivity extends BaseActivity {
    private EditText mEditText;
    private GridView mGridView;
    private KeyboardAdapter mKeyboardAdapter;
    private ImageView backImg;
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
        setContentView(R.layout.activity_sys_exit);
        mEditText = (EditText) findViewById(R.id.edit_sys_text);
        mGridView = (GridView) findViewById(R.id.grid_view);
        mGridView.setAdapter(mKeyboardAdapter);
        backImg = (ImageView) findViewById(R.id.back_img);
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
        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
}
