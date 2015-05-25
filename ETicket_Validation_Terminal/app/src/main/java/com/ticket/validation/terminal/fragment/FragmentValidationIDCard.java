package com.ticket.validation.terminal.fragment;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ticket.validation.terminal.R;
import com.ticket.validation.terminal.ValidationResultActivity;
import com.ticket.validation.terminal.model.ErrorModel;
import com.ticket.validation.terminal.model.GoodsModel;
import com.ticket.validation.terminal.util.LoginInterceporUtil;
import com.ticket.validation.terminal.util.ToastUtil;

import java.util.LinkedList;

/**
 * Created by dengshengjin on 15/5/21.
 */
public class FragmentValidationIDCard extends BaseQueryFragment {
    private View mContentView;
    private String test = "124569874566325541";
    private ViewGroup mIdCardBox;
    private ProgressBar mProgressBar;
    private TextView mStatusText;
    private TextView mIdCardWarnText;

    public static FragmentValidationIDCard newInstance() {
        FragmentValidationIDCard f = new FragmentValidationIDCard();
        return f;
    }

    @Override
    protected void initData() {
        super.initData();
        mIsAnimation = false;
    }

    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_validation_ticket_id_card, container, false);
        mIdCardBox = (ViewGroup) mContentView.findViewById(R.id.id_card_box);
        mProgressBar = (ProgressBar) mContentView.findViewById(R.id.progress_bar);
        mStatusText = (TextView) mContentView.findViewById(R.id.status_text);
        mIdCardWarnText = (TextView) mContentView.findViewById(R.id.id_card_warn_text);
        mStatusText.setText("");
        mProgressBar.setVisibility(View.GONE);
        return mContentView;
    }

    @Override
    protected void initWidgetActions() {
        mIdCardBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LoginInterceporUtil.pauseRedirect(getApplicationContext())) {
                    return;
                }
                if (mProgressBar.getVisibility() == View.VISIBLE) {
                    return;
                }
                if (mIsAnimation) {
                    return;
                }

                mStatusText.setText("");
                animateFadeInOut(mIdCardWarnText);
                getHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pauseAnimation(mIdCardWarnText);
                        boolean isValidationSucc = true;
                        if (getActivity() == null || getActivity().isFinishing()) {
                            return;
                        }
                        if (isValidationSucc) { //识别成功
                            mProgressBar.setVisibility(View.VISIBLE);
                            queryData(test, new RestfulCallback() {
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
                        } else {//识别失败
                            mProgressBar.setVisibility(View.GONE);
                            mStatusText.setText(getString(R.string.validation_id_card_fail));
                        }

                    }
                }, 1000);
            }
        });
    }

    private AlphaAnimation mAlphaAnimation;
    private boolean mIsAnimation;

    public void animateFadeInOut(final View view) {
        mAlphaAnimation = new AlphaAnimation(0.05f, 0.6f);
        mAlphaAnimation.setDuration(1000);
        mAlphaAnimation.setRepeatMode(ValueAnimator.REVERSE);
        mAlphaAnimation.setRepeatCount(ValueAnimator.INFINITE);
        mAlphaAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        mIsAnimation = true;
        view.startAnimation(mAlphaAnimation);
    }

    public void pauseAnimation(final View view) {
        view.clearAnimation();
        view.setAlpha(1.0f);
        mIsAnimation = false;
    }
}
