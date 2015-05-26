package com.ticket.validation.terminal.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.ticket.validation.terminal.R;
import com.ticket.validation.terminal.ValidationResultActivity;
import com.ticket.validation.terminal.adapter.KeyboardAdapter;
import com.ticket.validation.terminal.model.ErrorModel;
import com.ticket.validation.terminal.model.GoodsModel;
import com.ticket.validation.terminal.util.KeyCodeUtil;
import com.ticket.validation.terminal.util.LoginInterceporUtil;
import com.ticket.validation.terminal.util.ToastUtil;

import java.util.LinkedList;

/**
 * Created by dengshengjin on 15/5/17.
 */
public class FragmentValidationElectronic extends BaseQueryFragment {
    private EditText mEditText;
    private GridView mGridView;
    private KeyboardAdapter mKeyboardAdapter;
    private ImageView mClearImg;
    private final static int DEL_CODE = 1000;
    private RelativeLayout mQueryBox;
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

    public static FragmentValidationElectronic newInstance() {
        FragmentValidationElectronic f = new FragmentValidationElectronic();
        return f;
    }

    @Override
    protected void initData() {
        super.initData();
        mKeyboardAdapter = new KeyboardAdapter(getApplicationContext());
    }

    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_validation_ticket_electronic, container, false);
        mEditText = (EditText) view.findViewById(R.id.edit_sys_text);
        mGridView = (GridView) view.findViewById(R.id.grid_view);
        mGridView.setAdapter(mKeyboardAdapter);
        mQueryBox = (RelativeLayout) view.findViewById(R.id.query_box);
        mClearImg = (ImageView) view.findViewById(R.id.clear_img);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        mProgressBar.setVisibility(View.GONE);
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
                queryData(text, new RestfulCallback() {
                    @Override
                    public void success(LinkedList<GoodsModel> list) {
                        mProgressBar.setVisibility(View.GONE);
                        if (list.isEmpty()) {
                            ToastUtil.showToast(getApplicationContext(), R.string.loading_empty_data);
                            return;
                        }
                        Intent intent = new Intent(getActivity(), ValidationResultActivity.class);
                        intent.putExtra(ValidationResultActivity.MODELS, list);
                        startActivity(intent);
                    }

                    @Override
                    public void failureViaLocal() {
                        mProgressBar.setVisibility(View.GONE);
                        ToastUtil.showToast(getApplicationContext(), R.string.loading_fail2);
                    }

                    @Override
                    public void failureViaServer(ErrorModel errorModel) {
                        mProgressBar.setVisibility(View.GONE);
                        if (errorModel == null) {
                            ToastUtil.showToast(getApplicationContext(), R.string.loading_fail);
                            return;
                        }
                        ToastUtil.showToast(getApplicationContext(), errorModel.mInfo);
                    }


                });

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

    public void onKeyDown(int keyCode) {
        String key = KeyCodeUtil.getKeyCode(keyCode, false);
        if (!TextUtils.isEmpty(key)) {
            if (key.equals("del")) {
                onDelEvent();
            } else {
                onAddEvent(key);
            }
        }
    }

    private void onAddEvent(String key) {
        String text = mEditText.getText().toString();
        mEditText.setText(text + key);
        mEditText.setSelection(text.length() + 1);

    }
}
