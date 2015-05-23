package com.ticket.validation.terminal.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;

import com.ticket.validation.terminal.R;
import com.ticket.validation.terminal.adapter.KeyboardAdapter;
import com.zuiapps.suite.utils.log.LogUtil;

/**
 * Created by dengshengjin on 15/5/17.
 */
public class FragmentValidationElectronic extends BaseFragment {
    private EditText mEditText;
    private GridView mGridView;
    private KeyboardAdapter mKeyboardAdapter;
    private ImageView mClearImg;
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

    public static FragmentValidationElectronic newInstance() {
        FragmentValidationElectronic f = new FragmentValidationElectronic();
        return f;
    }

    @Override
    protected void initData() {
        mKeyboardAdapter = new KeyboardAdapter(getApplicationContext());
    }

    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_validation_ticket_electronic, container, false);
        mEditText = (EditText) view.findViewById(R.id.edit_sys_text);
        mGridView = (GridView) view.findViewById(R.id.grid_view);
        mGridView.setAdapter(mKeyboardAdapter);
        mClearImg = (ImageView) view.findViewById(R.id.clear_img);
        return view;
    }

    @Override
    protected void initWidgetActions() {
        mEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
            }
        });
        mKeyboardAdapter.setOnItemClickListener(new KeyboardAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String str) {
                LogUtil.e("mKeyboardAdapter onItemClick ");
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
}
